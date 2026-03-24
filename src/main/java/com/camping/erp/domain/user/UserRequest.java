package com.camping.erp.domain.user;

import com.camping.erp.domain.user.enums.UserRole;
import com.camping.erp.domain.user.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

public class UserRequest {

    @Getter @Setter
    public static class JoinDTO {
        private String username;
        private String password;
        private String passwordConfirm; // 비밀번호 확인 필드 추가
        private String name;
        private String email;
        private String phone;

        public User toEntity(String encodedPassword) {
            // 테스트 편의를 위해 아이디에 'admin'이 포함되면 관리자, 아니면 일반유저로 가입
            UserRole role = (username != null && username.contains("admin")) ? UserRole.ADMIN : UserRole.USER;
            
            return User.builder()
                    .username(username)
                    .password(encodedPassword)
                    .name(name)
                    .email(email)
                    .phone(phone)
                    .role(role)
                    .status(UserStatus.ACTIVE)
                    .build();
        }
    }

    @Getter @Setter
    public static class LoginDTO {
        private String username;
        private String password;
    }

    @Getter @Setter
    public static class UpdateDTO {
        private String currentPassword;  // 기존 비밀번호 확인용
        private String newPassword;      // 변경할 비밀번호 (선택)
        private String newPasswordConfirm; // 새 비밀번호 확인 (선택)
        private String name;
        private String email;
        private String phone;
    }
}
