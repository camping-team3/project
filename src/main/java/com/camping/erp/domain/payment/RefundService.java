package com.camping.erp.domain.payment;

import com.camping.erp.domain.reservation.Reservation;
import com.camping.erp.domain.reservation.ReservationRepository;
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
    private final RefundRepository refundRepository;
    private final PortOneService portOneService;

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
