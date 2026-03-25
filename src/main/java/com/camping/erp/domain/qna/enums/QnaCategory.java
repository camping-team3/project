package com.camping.erp.domain.qna.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QnaCategory {
    RESERVATION("예약문의"), // 예약 신청, 결제, 취소 등 관련 문의
    FACILITY("시설문의"),    // 캠핑장 시설, 구역, 공용 시설 등 관련 문의
    ETC("기타");            // 그 외 기타 일반 문의

    private final String value;
}
