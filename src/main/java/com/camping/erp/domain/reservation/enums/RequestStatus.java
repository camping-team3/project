package com.camping.erp.domain.reservation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 관리자 승인 프로세스를 위한 요청 처리 상태 Enum
 * PENDING: 관리자 승인 대기 중
 * APPROVED: 관리자 승인 완료
 * REJECTED: 관리자 거절 완료
 */
@Getter
@RequiredArgsConstructor
public enum RequestStatus {
    PENDING("승인 대기"),
    APPROVED("승인 완료"),
    REJECTED("거절됨");

    private final String description;
}
