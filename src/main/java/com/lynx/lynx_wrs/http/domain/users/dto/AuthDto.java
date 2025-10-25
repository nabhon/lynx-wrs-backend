package com.lynx.lynx_wrs.http.domain.users.dto;

import com.lynx.lynx_wrs.db.entities.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthDto {

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class RefreshRequest {
        private String refreshToken;
    }

    @Data
    public static class RefreshResponse {
        private String accessToken;
        private String refreshToken;
    }

    @Data
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;
        private String userEmail;
        private String userDisplayName;
        private Long userId;
        private Role userRole;
    }

    @Data
    public static class RegisterRequest {
        private String email;
        private String name;
        private String surname;
        private Role role;
    }
}
