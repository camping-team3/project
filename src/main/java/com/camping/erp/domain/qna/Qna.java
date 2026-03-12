package com.camping.erp.domain.qna;

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

    private Boolean isAnswered = false;

    @OneToMany(mappedBy = "qna", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Qna(Long id, User user, String title, String content, Boolean isAnswered) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.isAnswered = isAnswered;
    }
}
