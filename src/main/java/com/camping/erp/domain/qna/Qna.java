package com.camping.erp.domain.qna;

import com.camping.erp.domain.qna.enums.QnaCategory;
import com.camping.erp.domain.user.User;
import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "qna_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qna extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QnaCategory category;

    private Integer hits = 0;

    private Boolean isAnswered = false;

    @OneToMany(mappedBy = "qna", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Qna(Long id, User user, String title, String content, QnaCategory category, Integer hits, Boolean isAnswered) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.category = category;
        this.hits = (hits == null) ? 0 : hits;
        this.isAnswered = (isAnswered == null) ? false : isAnswered;
    }

    // 질문 수정 (답변 완료 시 호출되지 않도록 서비스에서 제어)
    public void update(String title, String content, QnaCategory category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    // 답변 완료 처리
    public void answered() {
        this.isAnswered = true;
    }

    // 조회수 증가
    public void increaseHits() {
        if (this.hits == null) {
            this.hits = 0;
        }
        this.hits++;
    }
}
