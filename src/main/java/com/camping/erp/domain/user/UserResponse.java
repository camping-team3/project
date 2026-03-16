package com.camping.erp.domain.user;

import com.camping.erp.domain.user.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserResponse {

    @Getter @Setter
    public static class LoginDTO {
        private Long id;
        private String username;
        private String name;
        private UserRole role;

        @Builder
        public LoginDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.name = user.getName();
            this.role = user.getRole();
        }

        public boolean isAdmin() {
            return role == UserRole.ADMIN;
        }
    }

    @Getter @Setter
    public static class DetailDTO {
        private Long id;
        private String username;
        private String name;
        private String email;
        private String phone;
        private UserRole role;

        @Builder
        public DetailDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.name = user.getName();
            this.email = user.getEmail();
            this.phone = user.getPhone();
            this.role = user.getRole();
        }
    }
}
