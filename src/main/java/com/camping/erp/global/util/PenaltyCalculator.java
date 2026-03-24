package com.camping.erp.global.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 위약금 및 환불 금액 계산 유틸리티
 * 비즈니스 규칙:
 * - 이용 7일 전: 100% 환불 (위약금 0%)
 * - 이용 3~6일 전: 50% 환불 (위약금 50%)
 * - 이용 당일~2일 전: 0% 환불 (환불 불가)
 */
public class PenaltyCalculator {

    /**
     * 체크인 날짜와 취소일 사이의 일수 차이에 따른 환불 비율 계산
     * @param checkInDate 예약 시작일
     * @param cancelDate 취소 요청일
     * @return 환불 비율 (1.0, 0.5, 0.0)
     */
    public static double calculateRefundRate(LocalDate checkInDate, LocalDate cancelDate) {
        long daysUntilCheckIn = ChronoUnit.DAYS.between(cancelDate, checkInDate);

        if (daysUntilCheckIn >= 7) {
            return 1.0; // 100% 환불
        } else if (daysUntilCheckIn >= 3) {
            return 0.5; // 50% 환불
        } else {
            return 0.0; // 환불 불가
        }
    }

    /**
     * 최종 환불 금액 계산
     * @param totalAmount 총 결제 금액
     * @param checkInDate 예약 시작일
     * @return 환불 대상 금액
     */
    public static long calculateRefundAmount(long totalAmount, LocalDate checkInDate) {
        double rate = calculateRefundRate(checkInDate, LocalDate.now());
        return (long) (totalAmount * rate);
    }
}
