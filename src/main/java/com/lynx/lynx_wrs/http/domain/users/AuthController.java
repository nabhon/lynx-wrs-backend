package com.lynx.lynx_wrs.http.domain.users;

import com.lynx.lynx_wrs.http.domain.users.dto.AuthDto;
import com.lynx.lynx_wrs.http.domain.users.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginRequest loginRequest) {
        AuthDto.LoginResponse res = authService.login(loginRequest);
        return ResponseEntity.ok(Map.of("accessToken", res.getAccessToken(),
                "refreshToken", res.getRefreshToken(),
                "userId", res.getUserId(),
                "userRole", res.getUserRole(),
                "userDisplayName", res.getUserDisplayName(),
                "userEmail", res.getUserEmail()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody AuthDto.RefreshRequest req) {
        String refreshToken = req.getRefreshToken();
        if (refreshToken == null) {
            throw new RuntimeException("refreshToken is null");
        }
        AuthDto.RefreshResponse res = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(Map.of("message","success"
                ,"accessToken",res.getAccessToken(), "refreshToken",res.getRefreshToken()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDto.RegisterRequest req) {
        String password = authService.registerMember(req);
        return  ResponseEntity.ok(Map.of("message","success","password",password));
    }
}
