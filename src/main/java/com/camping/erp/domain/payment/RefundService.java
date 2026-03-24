package com.camping.erp.domain.payment;

import com.camping.erp.domain.reservation.*;
import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.global.handler.ex.Exception400;
import com.camping.erp.global.handler.ex.Exception404;
import com.camping.erp.global.util.PenaltyCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 위약금 계산 및 포트원 연동 자동 환불 처리를 담당하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundService {

    private final ReservationRepository reservationRepository;
    private final ReservationChangeRequestRepository reservationChangeRequestRepository;
    private final ReservationCancelRequestRepository reservationCancelRequestRepository;
    private final RefundRepository refundRepository;
    private final PortOneService portOneService;

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

        if (request.isRefunded()) {
            throw new Exception400("이미 환불 처리가 완료된 요청입니다.");
        }

        // 1. 순수 차액 계산 (변경 전 금액 - 변경 후 금액)
        long diffAmount = request.getOldTotalPrice() - request.getNewTotalPrice();
        if (diffAmount <= 0) {
            throw new Exception400("부분 환불 대상이 아닙니다.");
        }

        // 2. 위약금 규정 적용 (PenaltyCalculator 사용)
        double refundRate = PenaltyCalculator.calculateRefundRate(reservation.getCheckIn(), LocalDate.now());
        long finalRefundAmount = (long) (diffAmount * refundRate);

        if (refundRate < 1.0) {
            log.info("[Partial Refund Info] 위약금 적용으로 차액 일부/전체 소멸: rate={}, diff={}, final={}", 
                     refundRate, diffAmount, finalRefundAmount);
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
                    .cancelledAt(LocalDateTime.now())
                    .build();
            refundRepository.save(refund);
        }

        // 4. 상태 확정
        request.markAsRefunded();

        log.info("[Partial Refund Success] 부분 환불 처리 완료: requestId={}, refundAmount={}",
                requestId, finalRefundAmount);
    }

    /**
     * [관리자용] 취소 요청 승인 및 실제 환불 실행
     * @param requestId ReservationCancelRequest의 ID
     */
    @Transactional
    public void approveCancelRequest(Long requestId) {
        // 1. 요청 정보 및 예약 정보 조회
        ReservationCancelRequest cancelRequest = reservationCancelRequestRepository.findById(requestId)
                .orElseThrow(() -> new Exception404("취소 요청 정보를 찾을 수 없습니다."));
        
        Reservation reservation = cancelRequest.getReservation();
        Payment payment = reservation.getPayment();

        if (payment == null) {
            throw new Exception404("연결된 결제 내역이 없어 환불을 진행할 수 없습니다.");
        }

        if (cancelRequest.isRefunded()) {
            throw new Exception400("이미 환불 처리가 완료된 요청입니다.");
        }

        // 1-1. 이용일 방어 로직: 이미 이용 시작일이거나 지난 경우 취소 불가
        if (!reservation.getCheckIn().isAfter(LocalDate.now())) {
            throw new Exception400("이용 시작일 이후에는 온라인 환불 처리가 불가능합니다. 관리자에게 별도 문의하세요.");
        }

        // 2. 위약금 규정에 따른 최종 환불 금액 계산 (PenaltyCalculator 사용)
        long refundAmount = PenaltyCalculator.calculateRefundAmount(reservation.getTotalPrice(), reservation.getCheckIn());
        log.info("[Refund Process] 예상 환불액: {}, 예약 총액: {}, 체크인: {}", 
                 refundAmount, reservation.getTotalPrice(), reservation.getCheckIn());

        // 3. 포트원 API 연동 환불 실행 (환불 금액이 0보다 큰 경우만)
        if (refundAmount > 0) {
            boolean success = portOneService.cancelPayment(payment.getImpUid(), refundAmount, cancelRequest.getReason());
            if (!success) {
                throw new Exception400("포트원 API를 통한 결제 취소에 실패했습니다.");
            }
        } else {
            log.info("[Refund Process] 위약금 100%로 인해 실제 환불액 0원 처리 (결제 취소 미호출)");
        }

        // 4. 상태 변경 및 이력 저장
        reservation.updateStatus(ReservationStatus.CANCEL_COMP);
        cancelRequest.approve();
        cancelRequest.markAsRefunded();

        Refund refund = Refund.builder()
                .reservation(reservation)
                .refundAmount(refundAmount)
                .reason(cancelRequest.getReason())
                .cancelledAt(LocalDateTime.now())
                .build();
        refundRepository.save(refund);

        log.info("[Refund Success] 최종 환불 완료: reservationId={}, refundAmount={}", 
                 reservation.getId(), refundAmount);
    }

    /**
     * 현재 날짜 기준 예상 환불 정보 계산 (사용자/관리자 확인용)
     */
    public RefundResponse.RefundInfo getRefundInfo(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception404("예약을 찾을 수 없습니다."));

        // 체크인 날짜와 오늘 날짜 사이의 일수 계산
        long daysUntilCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), reservation.getCheckIn());
        
        // 위약금 규정 적용 (PenaltyCalculator 사용)
        double refundRate = PenaltyCalculator.calculateRefundRate(reservation.getCheckIn(), LocalDate.now());
        long refundAmount = (long) (reservation.getTotalPrice() * refundRate);
        
        return RefundResponse.RefundInfo.builder()
                .reservationId(reservationId)
                .totalPrice(reservation.getTotalPrice())
                .refundAmount(refundAmount)
                .refundPercent((int) (refundRate * 100))
                .daysUntilCheckIn((int) daysUntilCheckIn)
                .canRefund(refundRate > 0)
                .build();
    }
}
