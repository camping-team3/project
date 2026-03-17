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
            return User.builder()
                    .username(username)
                    .password(encodedPassword)
                    .name(name)
                    .email(email)
                    .phone(phone)
                    .role(UserRole.USER) // 기본 권한 USER
                    .status(UserStatus.ACTIVE) // 기본 상태 ACTIVE
                    .build();
        }
    }

    @Getter @Setter
    public static class LoginDTO {
        private String username;
        private String password;
    }
}
