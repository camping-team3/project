package com.camping.erp.domain.review;

import com.camping.erp.domain.image.Image;
import com.camping.erp.domain.reservation.Reservation;
import com.camping.erp.domain.user.User;
import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review_tb")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Builder.Default
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    // AI 비방 위험도 (1~5)
    @Builder.Default
    @Column(name = "ai_danger_score")
    private Integer aiDangerScore = 0;

    // 관리자 검토 완료 여부
    @Builder.Default
    @Column(name = "is_reviewed")
    private boolean isReviewed = false;

    // 논리 삭제 여부
    @Builder.Default
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    // 관리자 삭제 사유
    @Column(name = "admin_reason")
    private String adminReason;

    public void update(Integer rating, String content) {
        this.rating = rating;
        this.content = content;
    }

    // 관리자 조치 (삭제/유지) 메서드
    public void processByAdmin(boolean isDeleted, String adminReason) {
        this.isDeleted = isDeleted;
        this.adminReason = adminReason;
        this.isReviewed = true;
    }

    // AI 점수 업데이트 메서드
    public void updateAiScore(Integer score) {
        this.aiDangerScore = score;
    }
}
