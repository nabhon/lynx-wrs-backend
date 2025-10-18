package com.lynx.lynx_wrs.http.domain.users.services;

import com.lynx.lynx_wrs.db.entities.Role;
import com.lynx.lynx_wrs.db.entities.Users;
import com.lynx.lynx_wrs.db.repositories.UserRepository;
import com.lynx.lynx_wrs.http.domain.users.dto.AuthDto;
import com.lynx.lynx_wrs.http.exception.AppException;
import com.lynx.lynx_wrs.http.exception.ErrorCode;
import com.lynx.lynx_wrs.security.CustomUserDetails;
import com.lynx.lynx_wrs.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshSessionService refreshSessions;

    private final UserRepository userRepository;

    public AuthDto.LoginResponse login(AuthDto.LoginRequest req) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(),req.getPassword()));
        } catch ( AuthenticationException e) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS, "Email or password incorrect");
        }
        Users users = userRepository.findByEmail(req.getEmail());
        if (users == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "User not found");
        }
        Long userId = users.getId();
        Role role = users.getRole();
        String access = jwtUtils.generateToken(req.getEmail(), userId);
        String refresh = jwtUtils.generateRefreshToken(req.getEmail(),  userId);
        refreshSessions.setLatest(req.getEmail(), jwtUtils.extractJti(refresh));
        AuthDto.LoginResponse res = new AuthDto.LoginResponse();
        res.setAccessToken(access);
        res.setRefreshToken(refresh);
        res.setUserId(userId);
        res.setRole(role);
        return res;
    }

    public Users getUserByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Token Invalid");
        }
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED, "User not found"));
    }

    public AuthDto.RefreshResponse refreshToken(String refreshToken) {
        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_JWT, "Refresh token ไม่ถูกต้องหรือหมดอายุ");
        }
        String email = jwtUtils.extractSubject(refreshToken);
        String jti   = jwtUtils.extractJti(refreshToken);

        refreshSessions.assertLatest(email, jti);
        refreshSessions.revoke(jti);

        Long userId = jwtUtils.extractUserId(refreshToken);
        String newAccess  = jwtUtils.generateToken(email, userId);
        String newRefresh = jwtUtils.generateRefreshToken(email, userId);
        refreshSessions.setLatest(email, jwtUtils.extractJti(newRefresh));
        AuthDto.RefreshResponse res = new AuthDto.RefreshResponse();
        res.setAccessToken(newAccess);
        res.setRefreshToken(newRefresh);
        return res;
    }
}
