package com.camping.erp.domain.user;

import com.camping.erp.domain.user.enums.UserRole;
import com.camping.erp.domain.user.enums.UserStatus;
import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_tb")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole role; // USER, ADMIN

    @Enumerated(EnumType.STRING)
    private UserStatus status; // ACTIVE, ANONYMOUS

    @Builder.Default
    private Integer penaltyCount = 0;

    public void updateRole(UserRole role) {
        this.role = role;
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
    }

    // 페널티 카운트 증가
    public void increasePenalty() {
        this.penaltyCount++;}
    // 회원 정보 수정 (이름, 이메일, 전화번호)
    public void updateInfo(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    // 비밀번호 수정
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
