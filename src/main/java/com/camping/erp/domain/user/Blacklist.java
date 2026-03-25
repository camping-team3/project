package com.camping.erp.domain.user;

import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blacklist_tb")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blacklist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 탈퇴한 유저의 아이디 (고유 식별자)
    @Column(nullable = false, unique = true)
    private String username;

    // 강제 탈퇴 사유
    @Column(nullable = false, length = 500)
    private String reason;

    // 관리자 메모 (추후 확장용)
    private String adminMemo;
}
