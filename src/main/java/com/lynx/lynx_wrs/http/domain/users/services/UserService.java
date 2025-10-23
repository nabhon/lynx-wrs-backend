package com.lynx.lynx_wrs.http.domain.users.services;

import com.lynx.lynx_wrs.db.entities.Role;
import com.lynx.lynx_wrs.db.entities.Users;
import com.lynx.lynx_wrs.db.repositories.UserRepository;
import com.lynx.lynx_wrs.http.domain.users.dto.UserDto;
import com.lynx.lynx_wrs.http.exception.AppException;
import com.lynx.lynx_wrs.http.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AuthService authService;

    public List<UserDto> getAllUsers() {
        Users admin = authService.getUserByToken();
        if (admin.getRole() == Role.USER) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    private UserDto mapToDto(Users user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getDisplayName())
                .role(user.getRole().name()) // assuming enum
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLoginAt())
                .build();
    }

    public void deleteUser(Long id) {
        Users admin = authService.getUserByToken();
        if (admin.getRole() == Role.USER) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        userRepository.deleteById(id);
    }
}
