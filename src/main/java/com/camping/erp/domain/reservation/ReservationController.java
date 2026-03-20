package com.camping.erp.domain.reservation;

import com.camping.erp.domain.site.SiteResponse;
import com.camping.erp.domain.site.SiteService;
import com.camping.erp.domain.user.UserResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class ReservationController {
    private final SiteService siteService;

    private final ReservationService reservationService;
    private final HttpSession session;

    // 예약 페이지 (예약 가능 사이트 목록 조회)
    @GetMapping("/reservations/new")
    public String newForm(ReservationRequest.SearchDTO searchDTO, Model model) {
        if (searchDTO.getCheckIn() == null) {
            searchDTO.setCheckIn(LocalDate.now());
        }
        if (searchDTO.getCheckOut() == null) {
            searchDTO.setCheckOut(searchDTO.getCheckIn().plusDays(1));
        }
        if (!searchDTO.getCheckOut().isAfter(searchDTO.getCheckIn())) {
            searchDTO.setCheckOut(searchDTO.getCheckIn().plusDays(1));
        }
        if (searchDTO.getPeopleCount() == null) {
            searchDTO.setPeopleCount(2);
        }

        List<SiteResponse.ResevationAvailableListDTO> sites = reservationService.findAvailableSites(searchDTO);

        long nights = ChronoUnit.DAYS.between(searchDTO.getCheckIn(), searchDTO.getCheckOut());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E)", Locale.KOREAN);

        model.addAttribute("sites", sites);
        model.addAttribute("search", searchDTO);
        model.addAttribute("nights", nights);
        model.addAttribute("checkInDisplay", searchDTO.getCheckIn().format(formatter));
        model.addAttribute("checkOutDisplay", searchDTO.getCheckOut().format(formatter));

        return "reservation/new";
    }

    // 결제 페이지 (결제 폼 데이터 준비)
    @GetMapping("/reservations/payment")
    public String paymentForm(ReservationRequest.ReserveDTO request, Model model) {
        ReservationResponse.PaymentFormDTO paymentInfo = reservationService.getPaymentForm(request);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E)", Locale.KOREAN);
        String dateRange = String.format("%s - %s (%d박)",
                paymentInfo.getCheckIn().format(formatter),
                paymentInfo.getCheckOut().format(formatter),
                paymentInfo.getNights());

        model.addAttribute("payment", paymentInfo);
        model.addAttribute("dateRange", dateRange);

        return "reservation/payment";
    }

    // 예약 실행 (최종 검증 및 저장)
    @PostMapping("/reservations/reserve")
    public String reserve(ReservationRequest.ReserveDTO request) {
        // 세션에서 DTO 타입으로 안전하게 꺼내기 (Casting 오류 해결)
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");

        // 서비스를 통해 예약 처리 (서비스 내부에서 유저 엔티티 조회)
        ReservationResponse.ReserveDTO response = reservationService.reserve(request, sessionUser);
        return "redirect:/reservations/complete?id=" + response.getId();
    }

    // 예약 완료 페이지 (예약 정보 확인 가능)
    @GetMapping("/reservations/complete")
    public String complete(@RequestParam("id") Long id, Model model) {
        ReservationResponse.CompleteDTO reservation = reservationService.getCompleteDetails(id);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E)", Locale.KOREAN);
        model.addAttribute("reservation", reservation);
        model.addAttribute("checkInDisplay", reservation.getCheckIn().format(formatter));
        model.addAttribute("checkOutDisplay", reservation.getCheckOut().format(formatter));

        return "reservation/complete";
    }

    // 마이페이지 예약 목록 조회
    @GetMapping("/mypage/reservations")
    public String reservations(Model model) {
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        // 본인 예약 목록 조회 (최신순)
        List<Reservation> reservationList = reservationService.findByUserIdOrderByCreatedAtDesc(sessionUser.getId());

        // DTO 변환 및 오늘 날짜 기준 버튼 노출 로직 계산
        LocalDate today = LocalDate.now();
        List<ReservationResponse.ListDTO> dtos = reservationList.stream()
                .map(r -> ReservationResponse.ListDTO.fromEntity(r, today))
                .toList();

        model.addAttribute("reservations", dtos);
        model.addAttribute("userName", sessionUser.getName());
        return "mypage/reservations";
    }

    // 마이페이지 예약 상세 조회
    @GetMapping("/mypage/reservations/{id}/detail")
    public String reservationDetail(@PathVariable("id") Long id, Model model) {
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        ReservationResponse.DetailDTO reservation = reservationService.getReservationDetail(id);
        model.addAttribute("reservation", reservation);
        return "mypage/reservation-detail";
    }

    // 예약 변경 폼 조회
    @GetMapping("/mypage/reservations/{id}/change-form")
    public String changeForm(@PathVariable("id") Long id, Model model) {
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        ReservationResponse.ChangeFormDTO reservation = reservationService.getChangeForm(id);
        model.addAttribute("reservation", reservation);
        model.addAttribute("userName", sessionUser.getName()); // 사용자 이름 추가
        return "mypage/reservation-change";
    }

    // [API] 예약 변경을 위한 가용 사이트 실시간 조회 (AJAX)
    @GetMapping("/api/reservations/available-sites")
    @ResponseBody
    public List<SiteResponse.ResevationAvailableListDTO> getAvailableSites(ReservationRequest.SearchDTO searchDTO) {
        // 기존 서비스 로직 재활용
        return reservationService.findAvailableSites(searchDTO);
    }

    // 예약 변경 요청 처리
    @PostMapping("/mypage/reservations/{id}/change-request")
    public String changeRequest(@PathVariable("id") Long id, ReservationRequest.ChangeDTO dto) {
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        dto.setReservationId(id);
        reservationService.requestChange(dto, sessionUser);
        return "redirect:/mypage/reservations/" + id + "/change-done";
    }

    // 예약 변경 완료 페이지
    @GetMapping("/mypage/reservations/{id}/change-done")
    public String changeDone(@PathVariable("id") Long id, Model model) {
        UserResponse.LoginDTO sessionUser = (UserResponse.LoginDTO) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }

        ReservationResponse.ChangeDoneDTO changeRequest = reservationService.getChangeDoneDetails(id);
        model.addAttribute("changeRequest", changeRequest);
        return "mypage/reservation-change-done";
    }
    }
