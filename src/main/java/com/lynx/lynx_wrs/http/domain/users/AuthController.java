package com.lynx.lynx_wrs.http.domain.users;

import com.lynx.lynx_wrs.http.domain.users.dto.AuthDto;
import com.lynx.lynx_wrs.http.domain.users.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
