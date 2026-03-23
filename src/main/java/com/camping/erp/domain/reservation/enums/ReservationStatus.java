package com.camping.erp.domain.reservation.enums;

/**
 * 예약의 현재 상태를 관리하는 Enum
 * PENDING: 결제 대기
 * CONFIRMED: 예약 확정
 * CHANGE_REQ: 예약 변경 요청 중 (관리자 승인 대기)
 * CANCEL_REQ: 예약 취소 요청 중 (관리자 승인 대기)
 * CANCEL_COMP: 취소 및 환불 완료
 * COMPLETED: 이용 완료 (체크아웃 이후)
 */
public enum ReservationStatus {
    PENDING("결제 대기"), 
    CONFIRMED("예약 확정"), 
    CHANGE_REQ("변경 요청"), 
    CANCEL_REQ("취소 요청"), 
    CANCEL_COMP("취소 완료"), 
    COMPLETED("이용 완료");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

