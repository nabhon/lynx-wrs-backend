package com.lynx.lynx_wrs.http.domain.users.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthDto {

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }
}
