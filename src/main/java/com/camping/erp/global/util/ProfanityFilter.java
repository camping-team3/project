package com.camping.erp.global.util;

import java.util.Arrays;
import java.util.List;

public class ProfanityFilter {
    // 실제 운영 시 더 방대한 리스트를 DB나 파일에서 로드할 수 있습니다.
    private static final List<String> BLACKLIST = Arrays.asList(
            "시발", "씨발", "개새끼", "병신", "미친놈", "꺼져", "닥쳐"
    );

    public static boolean containsProfanity(String text) {
        if (text == null || text.isBlank()) return false;
        // 공백 제거 후 체크하여 우회 방지
        String cleanText = text.replaceAll("\\s", "");
        return BLACKLIST.stream().anyMatch(cleanText::contains);
    }
}
