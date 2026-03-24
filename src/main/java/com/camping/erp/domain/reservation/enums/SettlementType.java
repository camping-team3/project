package com.camping.erp.domain.reservation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 예약 변경 시 발생하는 정산 타입 (추가 결제, 부분 환불, 차액 없음)
 */
@Getter
@RequiredArgsConstructor
public enum SettlementType {
    ADDITIONAL_PAY("추가 결제"),
    PARTIAL_REFUND("부분 환불"),
    NONE("차액 없음");

    private final String description;
}
