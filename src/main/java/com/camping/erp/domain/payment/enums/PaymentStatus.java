package com.camping.erp.domain.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    PAID("결제 완료"),
    CANCELLED("결제 취소");

    private final String description;
}
