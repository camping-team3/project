package com.camping.erp.domain.payment;

import com.camping.erp.domain.payment.enums.PaymentStatus;
import com.camping.erp.domain.reservation.Reservation;
import com.camping.erp.domain.reservation.ReservationRepository;
import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.domain.site.Site;
import com.camping.erp.domain.site.SiteRepository;
import com.camping.erp.domain.user.User;
import com.camping.erp.domain.user.UserRepository;
import com.camping.erp.global.handler.ex.Exception404;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 결제 검증, 상태 변경 및 조건부 자동 복구를 담당하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final PortOneService portOneService;
    private final SiteRepository siteRepository;
    private final UserRepository userRepository;

    /**
     * 포트원 웹훅 신호를 받아 결제 프로세스 처리
     */
    @Transactional
    public void processWebhook(String impUid, String merchantUid) {
        log.info("[System] 웹훅 처리 시작: impUid={}, merchantUid={}", impUid, merchantUid);

        // 1. 포트원으로부터 결제 상세 정보 조회
        PortOneResponse.PaymentDetail detail = portOneService.getPaymentDetail(impUid);

        // 2. 우리 DB에서 예약 데이터 찾기
        Long reservationId = extractReservationId(merchantUid);
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);

        if (reservation != null) {
            // [Case 1] 예약 데이터가 존재하는 경우 (정상적인 결제 흐름)
            validateAndConfirm(reservation, detail);
        } else {
            // [Case 2] 예약 데이터가 삭제된 경우 (조건부 자동 복구 시도)
            handleDeletedReservation(detail);
        }
    }

    /**
     * imp_uid 기반 실시간 상태 동기화 (웹훅 지연 또는 로컬 환경 대응)
     */
    @Transactional
    public void syncPaymentStatus(String impUid) {
        log.info("[System] 결제 상태 동기화 시작: impUid={}", impUid);

        // 1. 포트원으로부터 결제 상세 정보 조회
        PortOneResponse.PaymentDetail detail = portOneService.getPaymentDetail(impUid);
        if (detail == null) {
            log.error("[Error] 결제 정보를 찾을 수 없습니다: impUid={}", impUid);
            return;
        }

        // 2. 우리 DB에서 예약 데이터 찾기
        Long reservationId = extractReservationId(detail.getMerchantUid());
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);

        // 3. 예약 상태가 PENDING인 경우에만 확정 로직 수행 (중복 처리 방지)
        if (reservation != null) {
            if (reservation.getStatus() == ReservationStatus.PENDING) {
                log.info("[System] 미확정 예약 발견, 수동 확정 진행: reservationId={}", reservationId);
                validateAndConfirm(reservation, detail);
            } else {
                log.info("[System] 이미 처리된 예약입니다. 현재 상태: {}", reservation.getStatus());
            }
        } else {
            // 예약 데이터가 삭제된 경우(선점 만료 등) 복구 시도
            handleDeletedReservation(detail);
        }
    }

    /**
     * 결제 금액 검증 및 예약 확정
     */
    private void validateAndConfirm(Reservation reservation, PortOneResponse.PaymentDetail detail) {
        // 금액 검증 (위변조 방지)
        if (!reservation.getTotalPrice().equals(detail.getAmount())) {
            log.warn("[Alert] 결제 금액 불일치! 예약금액: {}, 결제금액: {}", reservation.getTotalPrice(), detail.getAmount());
            // 인자 개수 불일치 오류 수정: detail.getAmount() 추가
            portOneService.cancelPayment(detail.getImpUid(), detail.getAmount(), "결제 금액 불일치로 인한 자동 환불");
            return;
        }

        // 상태 변경 (PENDING -> CONFIRMED)
        reservation.updateStatus(ReservationStatus.CONFIRMED);

        // 결제 이력 저장
        savePayment(reservation, detail);
        log.info("[System] 예약 확정 완료: reservationId={}", reservation.getId());
    }

    /**
     * 삭제된 예약에 대한 조건부 복구 (오버부킹 가용성 체크)
     */
    private void handleDeletedReservation(PortOneResponse.PaymentDetail detail) {
        log.info("[System] 삭제된 예약 복구 시도: siteId={}, checkIn={}", detail.getSiteId(), detail.getCheckIn());

        // 1. 해당 사이트와 기간이 여전히 예약 가능한지 최종 체크
        List<ReservationStatus> activeStatuses = List.of(
                ReservationStatus.PENDING,
                ReservationStatus.CONFIRMED,
                ReservationStatus.CHANGE_REQ,
                ReservationStatus.CANCEL_REQ
        );

        boolean isExist = reservationRepository.existsBySiteIdAndDateRange(
                detail.getSiteId(), detail.getCheckIn(), detail.getCheckOut(), activeStatuses);

        if (!isExist) {
            // [복구 가능] 해당 자리가 아직 비어 있음 -> 예약 재생성
            User user = userRepository.findById(detail.getUserId())
                    .orElseThrow(() -> new Exception404("유저 정보를 찾을 수 없습니다."));
            Site site = siteRepository.findById(detail.getSiteId())
                    .orElseThrow(() -> new Exception404("사이트 정보를 찾을 수 없습니다."));

            Reservation recovered = Reservation.builder()
                    .user(user)
                    .site(site)
                    .checkIn(detail.getCheckIn())
                    .checkOut(detail.getCheckOut())
                    .peopleCount(detail.getPeopleCount())
                    .totalPrice(detail.getAmount())
                    .status(ReservationStatus.CONFIRMED)
                    .build();

            Reservation saved = reservationRepository.save(recovered);
            savePayment(saved, detail);
            log.info("[Success] 삭제된 예약 자동 복구 및 확정 완료: newReservationId={}", saved.getId());
        } else {
            // [복구 불가능] 선점 만료 후 다른 사람이 이미 예약함 -> 즉시 자동 환불
            log.warn("[Fail] 선점 만료 후 타인 예약 발생(오버부킹). 즉시 자동 환불 처리합니다.");
            // 인자 개수 불일치 오류 수정: detail.getAmount() 추가
            portOneService.cancelPayment(detail.getImpUid(), detail.getAmount(), "선점 시간 만료 및 타인 예약 발생으로 인한 자동 환불");
        }
    }

    /**
     * DB에 결제 성공 이력 기록
     */
    private void savePayment(Reservation reservation, PortOneResponse.PaymentDetail detail) {
        Payment payment = Payment.builder()
                .impUid(detail.getImpUid())
                .merchantUid(detail.getMerchantUid())
                .amount(detail.getAmount())
                .status(PaymentStatus.PAID)
                .payMethod(detail.getPayMethod())
                .payDate(java.time.LocalDateTime.now())
                .reservation(reservation)
                .build();
        paymentRepository.save(payment);
    }

    /**
     * merchant_uid("RSV-123-UUID")에서 예약 ID를 추출
     */
    private Long extractReservationId(String merchantUid) {
        try {
            return Long.parseLong(merchantUid.split("-")[1]);
        } catch (Exception e) {
            log.error("[Error] 주문 번호 파싱 실패: {}", merchantUid);
            return 0L;
        }
    }
}
