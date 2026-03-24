package com.camping.erp.global.util;

import java.util.Arrays;
import java.util.List;

public class ProfanityFilter {
    // 실무에서는 더 방대한 리스트를 DB나 외부 API를 통해 관리하는 것이 좋습니다.
    private static final List<String> BLACKLIST = Arrays.asList(
            "시발", "씨발", "개새끼", "병신", "미친놈", "꺼져", "닥쳐", "존나", "시바", "색기"
    );

    /**
     * 비속어 포함 여부를 확인합니다.
     * 공백 및 특수문자를 제거한 후 체크하여 우회 시도를 방어합니다.
     */
    public static boolean containsProfanity(String text) {
        if (text == null || text.isBlank()) return false;

        // 1. 공백 및 특수문자, 숫자 제거 (예: "시!발", "시 2발" 등 방어)
        String cleanText = text.replaceAll("[\\s!@#$%^&*()\\-_=+\\[\\]{};':\",.<>/?0-9]", "");

        // 2. 금지어 목록 대조
        return BLACKLIST.stream().anyMatch(cleanText::contains);
    }
}
