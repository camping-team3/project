package com.camping.erp.global.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * 모든 도메인에서 공통으로 사용할 수 있는 페이징 응답 DTO
 * Mustache 템플릿의 제약을 극복하기 위해 뷰 전용 데이터를 가공하여 제공합니다.
 */
@Getter
public class PageResponse<T> {
    private List<T> content;          // 실제 데이터 리스트
    private int number;               // 현재 페이지 번호 (1-based, 화면 노출용)
    private int totalPages;           // 전체 페이지 수
    private long totalElements;       // 전체 데이터 수
    private boolean hasPrevious;      // 이전 페이지 존재 여부
    private boolean hasNext;          // 다음 페이지 존재 여부
    private int previousPage;         // 이전 페이지 인덱스 (0-based, URL 요청용)
    private int nextPage;             // 다음 페이지 인덱스 (0-based, URL 요청용)
    private List<PageNumber> pageNumbers; // 페이지 번호 목록 (1, 2, 3...)

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.number = page.getNumber() + 1; // 0-based -> 1-based
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        
        // 이전/다음 페이지 존재 여부
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        
        // 이전/다음 페이지 인덱스 계산 (0-based)
        this.previousPage = page.getNumber() - 1;
        this.nextPage = page.getNumber() + 1;

        // 페이지 번호 리스트 생성 (한 번에 5개씩 노출)
        this.pageNumbers = new ArrayList<>();
        int startPage = (((number - 1) / 5) * 5) + 1;
        int endPage = Math.min(startPage + 4, totalPages);

        for (int i = startPage; i <= endPage; i++) {
            // i-1은 0-based 인덱스 (Spring Data JPA 요청용)
            this.pageNumbers.add(new PageNumber(i, i == number, i - 1));
        }
    }

    /**
     * 개별 페이지 번호 정보를 담는 내부 클래스
     */
    @Getter
    public static class PageNumber {
        private int number;      // 화면에 표시될 번호 (1, 2, 3...)
        private boolean active;  // 현재 페이지 여부 (CSS active 클래스 적용용)
        private int pageIndex;   // 실제 서버에 요청할 인덱스 (0-based)

        public PageNumber(int number, boolean active, int pageIndex) {
            this.number = number;
            this.active = active;
            this.pageIndex = pageIndex;
        }
    }
}
