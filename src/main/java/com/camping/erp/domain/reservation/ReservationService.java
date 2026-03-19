package com.camping.erp.domain.reservation;

import com.camping.erp.domain.admin.AdminRequest;
import com.camping.erp.domain.admin.AdminResponse;
import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.domain.site.Site;
import com.camping.erp.domain.site.SiteRepository;
import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.site.Zone;
import com.camping.erp.domain.user.User;
import com.camping.erp.domain.user.UserRepository;
import com.camping.erp.domain.user.UserResponse;
import com.camping.erp.global.handler.ex.Exception400;
import com.camping.erp.global.handler.ex.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

        private final ReservationRepository reservationRepository;
        private final SiteRepository siteRepository;
        private final UserRepository userRepository;

        // 예약 가능한 사이트 목록 조회
        public List<SiteResponse.ResevationAvailableListDTO> findAvailableSites(
                        ReservationRequest.SearchDTO searchDTO) {
                LocalDate checkIn = searchDTO.getCheckIn();
                LocalDate checkOut = searchDTO.getCheckOut();

                List<ReservationStatus> activeStatuses = List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED,
                                ReservationStatus.CANCEL_REQ);

                List<Site> availableSites = reservationRepository.findAvailableSites(
                                checkIn, checkOut, activeStatuses, searchDTO.getZoneId(), searchDTO.getPeopleCount());

                return availableSites.stream()
                                .map(site -> new SiteResponse.ResevationAvailableListDTO(site)) // 람다식 적용
                                .toList();
        }

        // 관리자용 전체 예약 내역 조회 (검색 필터 및 페이징 적용)
        public AdminResponse.ReservationPageDTO findAllForAdmin(AdminRequest.ReservationSearchDTO searchDTO,
                        Pageable pageable) {
                Page<Reservation> page = reservationRepository.findAllAdminSearch(
                                searchDTO.getKeyword(),
                                searchDTO.getCheckIn(),
                                searchDTO.getStatus(),
                                pageable);

                List<AdminResponse.ReservationListDTO> dtoList = page.getContent().stream()
                                .map(r -> {
                                        long nights = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());
                                        String statusText = "";
                                        String statusClass = "";

                                        switch (r.getStatus()) {
                                                case PENDING -> {
                                                        statusText = "대기 중";
                                                        statusClass = "warning";
                                                }
                                                case CONFIRMED -> {
                                                        statusText = "확정됨";
                                                        statusClass = "success";
                                                }
                                                case CANCEL_REQ -> {
                                                        statusText = "취소 요청";
                                                        statusClass = "info";
                                                }
                                                case CANCEL_COMP -> {
                                                        statusText = "취소 완료";
                                                        statusClass = "secondary";
                                                }
                                        }

                                        return AdminResponse.ReservationListDTO.builder()
                                                        .id(r.getId())
                                                        .reservationIdDisplay("RSV-" + String.format("%04d", r.getId()))
                                                        .username(r.getUser().getName())
                                                        .siteName(r.getSite().getSiteName())
                                                        .checkIn(r.getCheckIn())
                                                        .checkOut(r.getCheckOut())
                                                        .nights(nights)
                                                        .totalPrice(r.getTotalPrice())
                                                        .status(r.getStatus())
                                                        .statusText(statusText)
                                                        .statusClass(statusClass)
                                                        .build();
                                })
                                .toList();

                // 페이징 메타데이터 계산 (예: 5개씩 번호 표시)
                int totalPages = page.getTotalPages();
                int currentPage = page.getNumber();
                int startPage = Math.max(0, (currentPage / 5) * 5);
                int endPage = Math.min(startPage + 4, totalPages - 1);

                List<AdminResponse.PageNumberDTO> pageNumbers = IntStream.rangeClosed(startPage, endPage)
                                .mapToObj(n -> AdminResponse.PageNumberDTO.builder()
                                                .number(n)
                                                .displayDigit(n + 1)
                                                .isCurrent(n == currentPage)
                                                .build())
                                .toList();

                AdminResponse.PaginationDTO pagination = AdminResponse.PaginationDTO.builder()
                                .totalPages(totalPages)
                                .totalElements(page.getTotalElements())
                                .currentPage(currentPage)
                                .pageNumbers(pageNumbers)
                                .hasPrev(page.hasPrevious())
                                .hasNext(page.hasNext())
                                .prevPage(currentPage - 1)
                                .nextPage(currentPage + 1)
                                .build();

                return AdminResponse.ReservationPageDTO.builder()
                                .reservations(dtoList)
                                .pagination(pagination)
                                .build();
        }

        // 결제 폼 데이터 준비
        public ReservationResponse.PaymentFormDTO getPaymentForm(ReservationRequest.ReserveDTO request) {
                Site site = siteRepository.findById(request.getSiteId())
                                .orElseThrow(() -> new Exception404("해당 사이트를 찾을 수 없습니다."));
                Zone zone = site.getZone();

                long nights = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
                Integer basePeople = zone.getBasePeople();
                Integer extraPeople = Math.max(0, request.getPeopleCount() - basePeople);

                Long pricePerNight = zone.getNormalPrice();
                Long extraPersonFee = zone.getExtraPersonFee();
                Long extraPriceTotal = extraPersonFee * extraPeople * nights;
                Long totalPrice = (pricePerNight + (extraPeople * extraPersonFee)) * nights;

                return ReservationResponse.PaymentFormDTO.builder()
                                .siteId(site.getId())
                                .siteName(site.getSiteName())
                                .zoneName(zone.getName())
                                .checkIn(request.getCheckIn())
                                .checkOut(request.getCheckOut())
                                .nights(nights)
                                .peopleCount(request.getPeopleCount())
                                .basePeople(basePeople)
                                .pricePerNight(pricePerNight)
                                .extraPrice(extraPriceTotal)
                                .totalPrice(totalPrice)
                                .extraPersonFee(extraPersonFee)
                                .build();
        }

        // 예약 실행 (최종 검증 및 저장)
        @Transactional
        public ReservationResponse.ReserveDTO reserve(ReservationRequest.ReserveDTO request,
                        UserResponse.LoginDTO sessionUser) {
                // 1. 사이트 존재 확인
                Site site = siteRepository.findById(request.getSiteId())
                                .orElseThrow(() -> new Exception404("해당 사이트를 찾을 수 없습니다."));
                Zone zone = site.getZone();

                // 2. 최종 중복 체크
                List<ReservationStatus> activeStatuses = List.of(ReservationStatus.PENDING,
                                ReservationStatus.CONFIRMED);
                boolean isExist = reservationRepository.existsBySiteIdAndDateRange(
                                site.getId(), request.getCheckIn(), request.getCheckOut(), activeStatuses);

                if (isExist) {
                        throw new Exception400("이미 예약된 기간입니다.");
                }

                // 3. 서버 측 가격 재계산 및 검증 (보안)
                long nights = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
                int extraPeople = Math.max(0, request.getPeopleCount() - zone.getBasePeople());
                long calculatedPrice = (zone.getNormalPrice() + ((long) extraPeople * zone.getExtraPersonFee()))
                                * nights;

                // 4. 세션 유저가 있을 경우 유저 엔티티 조회 (연관관계 설정용)
                User user = null;
                if (sessionUser != null) {
                        user = userRepository.findById(sessionUser.getId())
                                        .orElseThrow(() -> new Exception404("유저 정보를 찾을 수 없습니다."));
                }

                // 5. 엔티티 생성 및 저장
                Reservation reservation = Reservation.builder()
                                .user(user)
                                .site(site)
                                .checkIn(request.getCheckIn())
                                .checkOut(request.getCheckOut())
                                .peopleCount(request.getPeopleCount())
                                .totalPrice(calculatedPrice)
                                .visitorName(request.getVisitorName())
                                .visitorPhone(request.getVisitorPhone())
                                .status(ReservationStatus.CONFIRMED)
                                .build();

                Reservation saved = reservationRepository.save(reservation);

                return ReservationResponse.ReserveDTO.builder()
                                .id(saved.getId())
                                .siteName(site.getSiteName())
                                .checkIn(saved.getCheckIn())
                                .checkOut(saved.getCheckOut())
                                .totalPrice(saved.getTotalPrice())
                                .build();
        }

        // 예약 완료 상세 정보 조회
        public ReservationResponse.CompleteDTO getCompleteDetails(Long id) {
                Reservation reservation = reservationRepository.findById(id)
                                .orElseThrow(() -> new Exception404("예약 내역을 찾을 수 없습니다."));

                long nights = ChronoUnit.DAYS.between(reservation.getCheckIn(), reservation.getCheckOut());

                return ReservationResponse.CompleteDTO.builder()
                                .id(reservation.getId())
                                .siteName(reservation.getSite().getSiteName())
                                .zoneName(reservation.getSite().getZone().getName())
                                .checkIn(reservation.getCheckIn())
                                .checkOut(reservation.getCheckOut())
                                .nights(nights)
                                .peopleCount(reservation.getPeopleCount())
                                .totalPrice(reservation.getTotalPrice())
                                .username(reservation.getUser().getUsername())
                                .build();
        }

        @Transactional
        public void cancel(Long id) {
                // 취소 로직은 추후 구현
        }
}
