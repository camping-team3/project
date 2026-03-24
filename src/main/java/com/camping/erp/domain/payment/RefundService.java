package com.camping.erp.domain.payment;

import com.camping.erp.domain.reservation.Reservation;
import com.camping.erp.domain.reservation.ReservationChangeRequest;
import com.camping.erp.domain.reservation.ReservationChangeRequestRepository;
import com.camping.erp.domain.reservation.ReservationRepository;
import com.camping.erp.domain.reservation.enums.RequestStatus;
import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.global.handler.ex.Exception400;
import com.camping.erp.global.handler.ex.Exception404;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 위약금 계산 및 자동 환불 처리를 담당하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundService {

    private final ReservationRepository reservationRepository;
    private final ReservationChangeRequestRepository reservationChangeRequestRepository;
    private final RefundRepository refundRepository;
    private final PortOneService portOneService;

    // ... (기존 메서드 생략)
    /**
     * 예약 변경 시 발생하는 차액에 대한 부분 환불 처리 (위약금 규정 적용)
     */
    @Transactional
    public void processPartialRefund(Long requestId) {
        ReservationChangeRequest request = reservationChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new Exception404("변경 요청 정보를 찾을 수 없습니다."));

        Reservation reservation = request.getReservation();
        Payment payment = reservation.getPayment();

        if (payment == null) {
            throw new Exception404("연결된 결제 내역이 없어 환불을 진행할 수 없습니다.");
        }

        // 1. 순수 차액 계산 (기존 금액 - 변경 후 금액)
        long diffAmount = reservation.getTotalPrice() - request.getNewTotalPrice();
        if (diffAmount <= 0) {
            throw new Exception400("부분 환불 대상이 아닙니다.");
        }

        // 2. 위약금 규정 적용 (체크인 일수 기준)
        long daysUntilCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), reservation.getCheckIn());
        long finalRefundAmount = 0;

        if (daysUntilCheckIn >= 7) {
            finalRefundAmount = diffAmount; // 100% 환불
        } else if (daysUntilCheckIn >= 3) {
            finalRefundAmount = diffAmount / 2; // 50% 환불
        } else {
            finalRefundAmount = 0; // 0~2일 전: 환불 불가 (차액 소멸)
            log.info("[Refund Info] 이용일 2일 이내 변경으로 차액 환불 불가: requestId={}", requestId);
        }

        // 3. 포트원 부분 취소 API 호출 (환불액이 있는 경우에만)
        if (finalRefundAmount > 0) {
            boolean success = portOneService.cancelPayment(payment.getImpUid(), finalRefundAmount,
                    "예약 변경으로 인한 차액 부분 환불(위약금 제외)");

            if (!success) {
                throw new Exception400("포트원 결제 취소 요청이 실패했습니다.");
            }

            // 환불 이력 저장
            Refund refund = Refund.builder()
                    .reservation(reservation)
                    .refundAmount(finalRefundAmount)
                    .reason("예약 변경 차액 환불 (위약금 적용)")
                    .cancelledAt(java.time.LocalDateTime.now())
                    .build();
            refundRepository.save(refund);
        }

        // 4. 예약 정보 실제 업데이트 및 상태 확정
        // 실제 환불된 금액과 무관하게, 예약의 기준 총액은 새로운 조건(newTotalPrice)으로 업데이트함
        reservation.updateReservationInfo(
                request.getNewCheckIn(),
                request.getNewCheckOut(),
                request.getNewSite(),
                request.getNewPeopleCount(),
                reservation.getVisitorName(),
                reservation.getVisitorPhone(),
                request.getNewTotalPrice());

        request.approve();
        reservation.updateStatus(ReservationStatus.CONFIRMED);

        log.info("[Partial Refund Success] 부분 환불 처리 및 예약 변경 완료: requestId={}, refundAmount={}",
                requestId, finalRefundAmount);
    }

    /**
     * 현재 날짜 기준 예상 환불 정보 계산 (관리자 확인용)
     */
    public RefundResponse.RefundInfo getRefundInfo(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception404("예약을 찾을 수 없습니다."));

        // 체크인 날짜와 오늘 날짜 사이의 일수 계산
        long daysUntilCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), reservation.getCheckIn());
        long totalPrice = reservation.getTotalPrice();
        long refundAmount = 0;
        int refundPercent = 0;
        boolean canRefund = true;

        // 환불 규정 적용
        if (daysUntilCheckIn >= 7) {
            refundPercent = 100;
            refundAmount = totalPrice;
        } else if (daysUntilCheckIn >= 3) {
            refundPercent = 50;
            refundAmount = totalPrice / 2;
        } else {
            // 0~2일 전: 자동 환불 불가 (위약금 100%)
            refundPercent = 0;
            refundAmount = 0;
            canRefund = false;
        }

        return RefundResponse.RefundInfo.builder()
                .reservationId(reservationId)
                .totalPrice(totalPrice)
                .refundAmount(refundAmount)
                .refundPercent(refundPercent)
                .daysUntilCheckIn((int) daysUntilCheckIn)
                .canRefund(canRefund)
                .build();
    }

    /**
     * 실제 환불 승인 처리 및 결제 취소 연동
     */
    @Transactional
    public void approveRefund(Long reservationId, String reason) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception404("예약을 찾을 수 없습니다."));

        // 1. 환불 금액 재계산 (날짜 기준 최종 검증)
        RefundResponse.RefundInfo info = getRefundInfo(reservationId);

        if (!info.isCanRefund() && info.getRefundAmount() == 0) {
            throw new Exception400("이용일 2일 이내의 예약은 시스템 자동 환불 대상이 아닙니다. 고객센터에 문의하세요.");
        }

        // 2. 포트원 결제 취소 API 호출
        Payment payment = reservation.getPayment();
        if (payment == null) {
            throw new Exception404("연결된 결제 내역이 없어 환불을 진행할 수 없습니다.");
        }

        boolean success = portOneService.cancelPayment(payment.getImpUid(), info.getRefundAmount(), reason);

        if (!success) {
            throw new Exception400("포트원 결제 취소 요청이 실패했습니다. (외부 API 오류)");
        }

        // 3. 예약 상태 변경 (CANCEL_REQ -> CANCEL_COMP)
        reservation.updateStatus(ReservationStatus.CANCEL_COMP);

        // 4. 환불 이력 저장
        Refund refund = Refund.builder()
                .reservation(reservation)
                .refundAmount(info.getRefundAmount())
                .reason(reason)
                .cancelledAt(java.time.LocalDateTime.now())
                .build();
        refundRepository.save(refund);

        log.info("[Refund Success] 환불 처리 완료: reservationId={}, amount={}, reason={}",
                reservationId, info.getRefundAmount(), reason);
    }
}
