package com.camping.erp.domain.reservation;

import com.camping.erp.domain.admin.AdminRequest;
import com.camping.erp.domain.admin.AdminResponse;
import com.camping.erp.domain.reservation.enums.RequestStatus;
import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.domain.review.Review;
import com.camping.erp.domain.review.ReviewRepository;
import com.camping.erp.domain.site.Site;
import com.camping.erp.domain.site.SiteRepository;
import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.site.Zone;
import com.camping.erp.domain.user.User;
import com.camping.erp.domain.user.UserRepository;
import com.camping.erp.domain.user.UserResponse.LoginDTO;
import com.camping.erp.global.handler.ex.Exception400;
import com.camping.erp.global.handler.ex.Exception401;
import com.camping.erp.global.handler.ex.Exception403;
import com.camping.erp.global.handler.ex.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

        private final ReservationRepository reservationRepository;
        private final SiteRepository siteRepository;
        private final UserRepository userRepository; // 유저 조회를 위해 추가
        private final ReservationChangeRequestRepository reservationChangeRequestRepository; // 변경 요청 처리를 위해 추가
        private final ReservationCancelRequestRepository reservationCancelRequestRepository; // 취소 요청 처리를 위해 추가

        // 예약 가능한 사이트 목록 조회 (가예약 Lock 로직 포함됨)
        public List<SiteResponse.ResevationAvailableListDTO> findAvailableSites(
                        ReservationRequest.SearchDTO searchDTO) {
                LocalDate checkIn = searchDTO.getCheckIn();
                LocalDate checkOut = searchDTO.getCheckOut();

                List<ReservationStatus> activeStatuses = List.of(
                                ReservationStatus.PENDING,
                                ReservationStatus.CONFIRMED,
                                ReservationStatus.CHANGE_REQ,
                                ReservationStatus.CANCEL_REQ);

                List<Site> availableSites = reservationRepository.findAvailableSites(
                                checkIn, checkOut, activeStatuses, searchDTO.getZoneId(), searchDTO.getPeopleCount());

                return availableSites.stream()
                                .map(site -> new SiteResponse.ResevationAvailableListDTO(site))
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
                                                case CHANGE_REQ -> {
                                                        statusText = "변경 요청";
                                                        statusClass = "info";
                                                }
                                                case CANCEL_REQ -> {
                                                        statusText = "취소 요청";
                                                        statusClass = "info";
                                                }
                                                case CANCEL_COMP -> {
                                                        statusText = "취소 완료";
                                                        statusClass = "secondary";
                                                }
                                                case COMPLETED -> {
                                                        statusText = "이용 완료";
                                                        statusClass = "primary";
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

        // 결제 폼 데이터 준비 및 가예약(Lock) 생성
        @Transactional
        public ReservationResponse.PaymentFormDTO getPaymentForm(ReservationRequest.ReserveDTO request, LoginDTO sessionUser) {
                if (sessionUser == null) {
                        throw new Exception401("로그인이 필요한 서비스입니다.");
                }
                User user = userRepository.findById(sessionUser.getId())
                                .orElseThrow(() -> new Exception404("인증된 유저 정보를 찾을 수 없습니다."));

                Site site = siteRepository.findById(request.getSiteId())
                                .orElseThrow(() -> new Exception404("해당 사이트를 찾을 수 없습니다."));
                Zone zone = site.getZone();

                // 1. 중복 체크 (선점 중인 PENDING 포함)
                List<ReservationStatus> activeStatuses = List.of(
                                ReservationStatus.PENDING,
                                ReservationStatus.CONFIRMED,
                                ReservationStatus.CHANGE_REQ,
                                ReservationStatus.CANCEL_REQ
                );
                boolean isExist = reservationRepository.existsBySiteIdAndDateRange(
                                site.getId(), request.getCheckIn(), request.getCheckOut(), activeStatuses);

                if (isExist) {
                        throw new Exception400("이미 예약되었거나 결제 진행 중인 자리입니다.");
                }

                // 2. 가격 계산
                long nights = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
                Integer basePeople = zone.getBasePeople();
                Integer extraPeople = Math.max(0, request.getPeopleCount() - basePeople);
                Long pricePerNight = zone.getNormalPrice();
                Long extraPersonFee = zone.getExtraPersonFee();
                Long extraPriceTotal = extraPersonFee * extraPeople * nights;
                Long totalPrice = (pricePerNight + (extraPeople * extraPersonFee)) * nights;

                // 3. 가예약(PENDING) 생성 - 10분간 선점됨
                Reservation reservation = Reservation.builder()
                                .user(user)
                                .site(site)
                                .checkIn(request.getCheckIn())
                                .checkOut(request.getCheckOut())
                                .peopleCount(request.getPeopleCount())
                                .totalPrice(totalPrice)
                                .status(ReservationStatus.PENDING)
                                .build();

                Reservation saved = reservationRepository.save(reservation);

                return ReservationResponse.PaymentFormDTO.builder()
                                .reservationId(saved.getId())
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
        public ReservationResponse.ReserveDTO reserve(ReservationRequest.ReserveDTO request, LoginDTO sessionUser) {
                // 1. 유저 엔티티 조회 (DTO -> Entity 변환)
                if (sessionUser == null) {
                        throw new Exception401("로그인이 필요한 서비스입니다.");
                }
                User user = userRepository.findById(sessionUser.getId())
                                .orElseThrow(() -> new Exception404("인증된 유저 정보를 찾을 수 없습니다."));

                // 2. 사이트 존재 확인
                Site site = siteRepository.findById(request.getSiteId())
                                .orElseThrow(() -> new Exception404("해당 사이트를 찾을 수 없습니다."));
                Zone zone = site.getZone();

                // 3. 최종 중복 체크 (자신이 생성한 PENDING 제외)
                List<ReservationStatus> activeStatuses = List.of(
                                ReservationStatus.CONFIRMED,
                                ReservationStatus.CHANGE_REQ,
                                ReservationStatus.CANCEL_REQ);
                boolean isExist = reservationRepository.existsBySiteIdAndDateRange(
                                site.getId(), request.getCheckIn(), request.getCheckOut(), activeStatuses);

                if (isExist) {
                        throw new Exception400("이미 예약된 기간입니다.");
                }

                // 4. 서버 측 가격 재계산 및 검증 (보안)
                long nights = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
                int extraPeople = Math.max(0, request.getPeopleCount() - zone.getBasePeople());
                long calculatedPrice = (zone.getNormalPrice() + ((long) extraPeople * zone.getExtraPersonFee()))
                                * nights;

                // 5. 기존 PENDING 예약 찾기 (없으면 신규 생성)
                Reservation reservation = reservationRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                                .stream()
                                .filter(r -> r.getStatus() == ReservationStatus.PENDING)
                                .filter(r -> r.getSite().getId().equals(site.getId()))
                                .filter(r -> r.getCheckIn().equals(request.getCheckIn()))
                                .findFirst()
                                .orElseGet(() -> Reservation.builder()
                                                .user(user)
                                                .site(site)
                                                .checkIn(request.getCheckIn())
                                                .checkOut(request.getCheckOut())
                                                .build());

                // 6. 데이터 업데이트 및 상태 확정
                reservation.updateReservationInfo(
                                request.getCheckIn(),
                                request.getCheckOut(),
                                site,
                                request.getPeopleCount(),
                                request.getVisitorName(),
                                request.getVisitorPhone());
                reservation.updateStatus(ReservationStatus.CONFIRMED);
                // totalPrice 업데이트 필드가 엔티티에 직접적인 setter가 없으므로 builder 재사용 고려하거나 필드 추가 필요
                // 여기서는 기존 빌더 로직의 구조를 유지하며 필드 세팅을 확실히 함

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

        /**
         * 선점 즉시 해제 (결제창 이탈 시)
         */
        @Transactional
        public void releaseLock(Long id, LoginDTO sessionUser) {
                Reservation reservation = reservationRepository.findById(id)
                                .orElseThrow(() -> new Exception404("해당 예약을 찾을 수 없습니다."));

                // 본인 확인 (세션 유저와 예약자가 일치하는지)
                if (!reservation.getUser().getId().equals(sessionUser.getId())) {
                        throw new Exception403("본인의 선점 데이터만 해제할 수 있습니다.");
                }

                // PENDING 상태인 경우에만 물리적 삭제
                if (reservation.getStatus() == ReservationStatus.PENDING) {
                        reservationRepository.delete(reservation);
                }
        }

        /**
         * 마이페이지 예약 목록 조회
         */
        public List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId) {
                return reservationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }

        /**
         * 예약 상세 정보 조회 (DTO 변환 포함)
         */
        public ReservationResponse.DetailDTO getReservationDetail(Long id) {
                Reservation reservation = reservationRepository.findById(id)
                                .orElseThrow(() -> new Exception404("해당 예약을 찾을 수 없습니다."));
                return ReservationResponse.DetailDTO.fromEntity(reservation, LocalDate.now());
        }

        /**
         * 예약 변경 폼 정보 조회
         */
        public ReservationResponse.ChangeFormDTO getChangeForm(Long id) {
                Reservation reservation = reservationRepository.findById(id)
                                .orElseThrow(() -> new Exception404("해당 예약을 찾을 수 없습니다."));
                return ReservationResponse.ChangeFormDTO.fromEntity(reservation);
        }

        /**
         * 예약 변경 완료 상세 정보 조회
         */
        public ReservationResponse.ChangeDoneDTO getChangeDoneDetails(Long reservationId) {
                // 해당 예약의 가장 최근 변경 요청(PENDING) 조회
                List<ReservationChangeRequest> requests = reservationChangeRequestRepository
                                .findByReservationId(reservationId);

                ReservationChangeRequest latest = requests.stream()
                                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                                .findFirst()
                                .orElseThrow(() -> new Exception404("변경 요청 내역을 찾을 수 없습니다."));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

                return ReservationResponse.ChangeDoneDTO.builder()
                                .reservationId(reservationId)
                                .newSiteName(latest.getNewSite().getSiteName())
                                .newZoneName(latest.getNewSite().getZone().getName())
                                .newCheckIn(latest.getNewCheckIn().format(formatter))
                                .newCheckOut(latest.getNewCheckOut().format(formatter))
                                .newPeopleCount(latest.getNewPeopleCount())
                                .requestDate(latest.getCreatedAt().format(formatter))
                                .build();
        }

        /**
         * 예약 변경 요청 처리
         */
        @Transactional
        public void requestChange(ReservationRequest.ChangeDTO dto, LoginDTO sessionUser) {
                // 1. 원본 예약 조회 및 소유권 검증
                Reservation reservation = reservationRepository.findById(dto.getReservationId())
                                .orElseThrow(() -> new Exception404("예약을 찾을 수 없습니다."));

                if (!reservation.getUser().getId().equals(sessionUser.getId())) {
                        throw new Exception403("본인의 예약만 변경 요청할 수 있습니다.");
                }

                // 2. 새로운 사이트 조회
                Site newSite = siteRepository.findById(dto.getNewSiteId())
                                .orElseThrow(() -> new Exception404("변경하려는 사이트를 찾을 수 없습니다."));

                // 3. 중복 예약 최종 체크 (가예약 Lock 로직 포함됨)
                List<ReservationStatus> activeStatuses = List.of(
                                ReservationStatus.PENDING,
                                ReservationStatus.CONFIRMED,
                                ReservationStatus.CHANGE_REQ,
                                ReservationStatus.CANCEL_REQ);
                boolean isExist = reservationRepository.existsBySiteIdAndDateRange(
                                newSite.getId(), dto.getNewCheckIn(), dto.getNewCheckOut(), activeStatuses);

                if (isExist) {
                        throw new Exception400("해당 기간은 이미 예약되었거나 변경 요청 중인 자리입니다.");
                }

                // 4. 원본 예약 상태 변경 (CONFIRMED -> CHANGE_REQ)
                reservation.updateStatus(ReservationStatus.CHANGE_REQ);

                // 5. 변경 요청 기록 생성 및 저장
                ReservationChangeRequest changeRequest = ReservationChangeRequest.builder()
                                .reservation(reservation)
                                .newCheckIn(dto.getNewCheckIn())
                                .newCheckOut(dto.getNewCheckOut())
                                .newSite(newSite)
                                .newPeopleCount(dto.getNewPeopleCount())
                                .status(com.camping.erp.domain.reservation.enums.RequestStatus.PENDING)
                                .build();

                reservationChangeRequestRepository.save(changeRequest);
        }

        /**
         * 예약 취소 요청 처리
         */
        @Transactional
        public void requestCancel(ReservationRequest.CancelDTO dto, LoginDTO sessionUser) {
                // 1. 원본 예약 조회 및 소유권 검증
                Reservation reservation = reservationRepository.findById(dto.getReservationId())
                                .orElseThrow(() -> new Exception404("예약을 찾을 수 없습니다."));

                if (!reservation.getUser().getId().equals(sessionUser.getId())) {
                        throw new Exception403("본인의 예약만 취소 요청할 수 있습니다.");
                }

                // 2. 비즈니스 규칙 검증 (이용일 3일 전 체크)
                LocalDate limitDate = reservation.getCheckIn().minusDays(3);
                if (LocalDate.now().isAfter(limitDate)) {
                        throw new Exception400("이용일 3일 전까지만 온라인 취소가 가능합니다. 고객센터로 문의해주세요.");
                }

                // 3. 원본 예약 상태 변경 (CONFIRMED -> CANCEL_REQ)
                reservation.updateStatus(ReservationStatus.CANCEL_REQ);

                // 4. 취소 요청 기록 생성 및 저장
                ReservationCancelRequest cancelRequest = ReservationCancelRequest.builder()
                                .reservation(reservation)
                                .reason(dto.getReason())
                                .refundBank(dto.getRefundBank())
                                .refundAccount(dto.getRefundAccount())
                                .refundAccountHolder(dto.getRefundAccountHolder())
                                .status(RequestStatus.PENDING)
                                .build();

                reservationCancelRequestRepository.save(cancelRequest);
        }

        /**
         * 예약 취소 요청 완료 정보 조회
         */
        public ReservationResponse.CancelDoneDTO getCancelDoneDetails(Long reservationId) {
                List<ReservationCancelRequest> requests = reservationCancelRequestRepository
                                .findByReservationId(reservationId);

                ReservationCancelRequest latest = requests.stream()
                                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                                .findFirst()
                                .orElseThrow(() -> new Exception404("취소 요청 내역을 찾을 수 없습니다."));

                return ReservationResponse.CancelDoneDTO.builder()
                                .reservationId(reservationId)
                                .reason(latest.getReason())
                                .requestDate(latest.getCreatedAt().toLocalDate().toString().replace("-", "."))
                                .build();
        }

        /**
         * [Task 4-2] 관리자용 예약 변경 상세 데이터 조회 (비교용)
         */
        public AdminResponse.AdminChangeDetailDTO getAdminChangeDetail(Long reservationId) {
                Reservation r = reservationRepository.findById(reservationId)
                                .orElseThrow(() -> new Exception404("예약 내역을 찾을 수 없습니다."));

                ReservationChangeRequest req = reservationChangeRequestRepository.findByReservationId(reservationId)
                                .stream()
                                .filter(c -> c.getStatus() == RequestStatus.PENDING)
                                .findFirst()
                                .orElseThrow(() -> new Exception404("진행 중인 변경 요청이 없습니다."));

                long oldNights = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());
                long newNights = ChronoUnit.DAYS.between(req.getNewCheckIn(), req.getNewCheckOut());

                List<ReservationResponse.ChangeRequestHistoryDTO> changeHistories = r.getChangeRequests() == null ? List.of() : r.getChangeRequests().stream()
                                .map(ReservationResponse.ChangeRequestHistoryDTO::fromEntity)
                                .toList();

                List<ReservationResponse.CancelRequestHistoryDTO> cancelHistories = r.getCancelRequests() == null ? List.of() : r.getCancelRequests().stream()
                                .map(ReservationResponse.CancelRequestHistoryDTO::fromEntity)
                                .toList();

                return AdminResponse.AdminChangeDetailDTO.builder()
                                .id(r.getId())
                                .userId(r.getUser().getId())
                                .username(r.getUser().getName() + "(" + r.getUser().getUsername() + ")")
                                .visitorName(r.getVisitorName())
                                .visitorPhone(r.getVisitorPhone())
                                .userRole(r.getUser().getRole().name())
                                .totalPrice(r.getTotalPrice())
                                .oldSiteName(r.getSite().getSiteName())
                                .oldCheckIn(r.getCheckIn().toString())
                                .oldCheckOut(r.getCheckOut().toString())
                                .oldNights(oldNights)
                                .oldPeopleCount(r.getPeopleCount())
                                .newSiteName(req.getNewSite().getSiteName())
                                .newCheckIn(req.getNewCheckIn().toString())
                                .newCheckOut(req.getNewCheckOut().toString())
                                .newNights(newNights)
                                .newPeopleCount(req.getNewPeopleCount())
                                .changeRequests(changeHistories)
                                .cancelRequests(cancelHistories)
                                .build();
        }

        /**
         * [Task 4-2] 관리자용 예약 취소 상세 데이터 조회
         */
        public AdminResponse.AdminCancelDetailDTO getAdminCancelDetail(Long reservationId) {
                Reservation r = reservationRepository.findById(reservationId)
                                .orElseThrow(() -> new Exception404("예약 내역을 찾을 수 없습니다."));

                ReservationCancelRequest req = reservationCancelRequestRepository.findByReservationId(reservationId)
                                .stream()
                                .filter(c -> c.getStatus() == RequestStatus.PENDING)
                                .findFirst()
                                .orElseThrow(() -> new Exception404("진행 중인 취소 요청이 없습니다."));

                long nights = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());

                List<ReservationResponse.ChangeRequestHistoryDTO> changeHistories = r.getChangeRequests() == null ? List.of() : r.getChangeRequests().stream()
                                .map(ReservationResponse.ChangeRequestHistoryDTO::fromEntity)
                                .toList();

                List<ReservationResponse.CancelRequestHistoryDTO> cancelHistories = r.getCancelRequests() == null ? List.of() : r.getCancelRequests().stream()
                                .map(ReservationResponse.CancelRequestHistoryDTO::fromEntity)
                                .toList();

                return AdminResponse.AdminCancelDetailDTO.builder()
                                .id(r.getId())
                                .userId(r.getUser().getId())
                                .username(r.getUser().getName() + "(" + r.getUser().getUsername() + ")")
                                .visitorName(r.getVisitorName())
                                .visitorPhone(r.getVisitorPhone())
                                .userRole(r.getUser().getRole().name())
                                .siteName(r.getSite().getSiteName())
                                .checkIn(r.getCheckIn().toString())
                                .checkOut(r.getCheckOut().toString())
                                .nights(nights)
                                .peopleCount(r.getPeopleCount())
                                .totalPrice(r.getTotalPrice())
                                .reason(req.getReason())
                                .refundBank(req.getRefundBank())
                                .refundAccount(req.getRefundAccount())
                                .refundAccountHolder(req.getRefundAccountHolder())
                                .changeRequests(changeHistories)
                                .cancelRequests(cancelHistories)
                                .build();
        }

        /**
         * [Task 4-3] 관리자용 예약 상세 정보 조회 (통합 이력 포함)
         */
        public AdminResponse.AdminReservationDetailDTO getAdminReservationDetail(Long id) {
                Reservation r = reservationRepository.findById(id)
                                .orElseThrow(() -> new Exception404("해당 예약을 찾을 수 없습니다."));

                long nights = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());

                List<ReservationResponse.ChangeRequestHistoryDTO> changeHistories = r.getChangeRequests() == null ? List.of()
                                : r.getChangeRequests().stream()
                                                .map(ReservationResponse.ChangeRequestHistoryDTO::fromEntity)
                                                .toList();

                List<ReservationResponse.CancelRequestHistoryDTO> cancelHistories = r.getCancelRequests() == null ? List.of()
                                : r.getCancelRequests().stream()
                                                .map(ReservationResponse.CancelRequestHistoryDTO::fromEntity)
                                                .toList();

                return AdminResponse.AdminReservationDetailDTO.builder()
                                .id(r.getId())
                                .userId(r.getUser().getId())
                                .username(r.getUser().getName() + "(" + r.getUser().getUsername() + ")")
                                .visitorName(r.getVisitorName())
                                .visitorPhone(r.getVisitorPhone())
                                .userRole(r.getUser().getRole().name())
                                .siteName(r.getSite().getSiteName())
                                .checkIn(r.getCheckIn().toString().replace("-", "."))
                                .checkOut(r.getCheckOut().toString().replace("-", "."))
                                .nights(nights)
                                .peopleCount(r.getPeopleCount())
                                .totalPrice(r.getTotalPrice())
                                .statusText(r.getStatus().getDescription())
                                .statusClass(getStatusClass(r.getStatus()))
                                .changeRequests(changeHistories)
                                .cancelRequests(cancelHistories)
                                .build();
        }

        private String getStatusClass(ReservationStatus status) {
                return switch (status) {
                        case PENDING -> "warning";
                        case CONFIRMED -> "primary";
                        case COMPLETED -> "success";
                        case CANCEL_REQ, CHANGE_REQ -> "info";
                        case CANCEL_COMP -> "danger";
                        default -> "secondary";
                };
        }

        /**
         * [Task 4-4] 관리자 예약 승인 처리 (변경/취소 공통)
         */
        @Transactional
        public void approveRequest(Long id) {
                Reservation r = reservationRepository.findById(id)
                                .orElseThrow(() -> new Exception404("해당 예약을 찾을 수 없습니다."));

                if (r.getStatus() == ReservationStatus.CHANGE_REQ) {
                        ReservationChangeRequest req = reservationChangeRequestRepository.findByReservationId(id)
                                        .stream()
                                        .filter(c -> c.getStatus() == RequestStatus.PENDING)
                                        .findFirst()
                                        .orElseThrow(() -> new Exception404("진행 중인 변경 요청이 없습니다."));

                        // 1. 예약 정보 업데이트 (새로운 사이트, 일정, 인원 반영)
                        r.updateReservationInfo(
                                        req.getNewCheckIn(),
                                        req.getNewCheckOut(),
                                        req.getNewSite(),
                                        req.getNewPeopleCount(),
                                        r.getVisitorName(),
                                        r.getVisitorPhone());
                        // 2. 요청 상태 및 예약 상태 확정
                        req.approve();
                        r.updateStatus(ReservationStatus.CONFIRMED);

                } else if (r.getStatus() == ReservationStatus.CANCEL_REQ) {
                        ReservationCancelRequest req = reservationCancelRequestRepository.findByReservationId(id)
                                        .stream()
                                        .filter(c -> c.getStatus() == RequestStatus.PENDING)
                                        .findFirst()
                                        .orElseThrow(() -> new Exception404("진행 중인 취소 요청이 없습니다."));

                        // 1. 요청 승인 및 예약 상태를 '취소 완료'로 변경
                        req.approve();
                        r.updateStatus(ReservationStatus.CANCEL_COMP);
                }
        }

        /**
         * [Task 4-4] 관리자 예약 거절 처리 (변경/취소 공통)
         */
        @Transactional
        public void rejectRequest(Long id, AdminRequest.RejectDTO dto) {
                Reservation r = reservationRepository.findById(id)
                                .orElseThrow(() -> new Exception404("해당 예약을 찾을 수 없습니다."));

                if (r.getStatus() == ReservationStatus.CHANGE_REQ) {
                        ReservationChangeRequest req = reservationChangeRequestRepository.findByReservationId(id)
                                        .stream()
                                        .filter(c -> c.getStatus() == RequestStatus.PENDING)
                                        .findFirst()
                                        .orElseThrow(() -> new Exception404("진행 중인 변경 요청이 없습니다."));

                        req.reject(dto.getRejectionReason());

                } else if (r.getStatus() == ReservationStatus.CANCEL_REQ) {
                        ReservationCancelRequest req = reservationCancelRequestRepository.findByReservationId(id)
                                        .stream()
                                        .filter(c -> c.getStatus() == RequestStatus.PENDING)
                                        .findFirst()
                                        .orElseThrow(() -> new Exception404("진행 중인 취소 요청이 없습니다."));

                        req.reject(dto.getRejectionReason());
                }

                // 공통: 예약 상태를 다시 '확정' 상태로 복원
                r.updateStatus(ReservationStatus.CONFIRMED);
        }
}
