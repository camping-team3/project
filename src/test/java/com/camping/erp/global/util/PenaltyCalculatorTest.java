package com.camping.erp.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PenaltyCalculatorTest {

    @ParameterizedTest(name = "체크인 {0}, 취소 {1} -> 예상 비율 {2}")
    @CsvSource({
            "2026-04-01, 2026-03-24, 1.0", // 8일 전 -> 100%
            "2026-03-31, 2026-03-24, 1.0", // 7일 전 -> 100%
            "2026-03-30, 2026-03-24, 0.5", // 6일 전 -> 50%
            "2026-03-27, 2026-03-24, 0.5", // 3일 전 -> 50%
            "2026-03-26, 2026-03-24, 0.0", // 2일 전 -> 0%
            "2026-03-24, 2026-03-24, 0.0"  // 당일 -> 0%
    })
    @DisplayName("체크인 날짜와 취소일의 차이에 따른 환불 비율 검증")
    void calculateRefundRateTest(String checkInStr, String cancelStr, double expectedRate) {
        // given
        LocalDate checkInDate = LocalDate.parse(checkInStr);
        LocalDate cancelDate = LocalDate.parse(cancelStr);

        // when
        double actualRate = PenaltyCalculator.calculateRefundRate(checkInDate, cancelDate);

        // then
        assertThat(actualRate).isEqualTo(expectedRate);
    }
}
