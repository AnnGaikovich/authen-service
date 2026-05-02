package org.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.util.JwtTokenProvider;
import org.example.authservice.dto.LoginRequestDTO;
import org.example.authservice.dto.AuthResponseDTO;
import org.example.authservice.entity.User;
import org.example.authservice.exception.AuthException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userService.findByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AuthException("Неверный пароль");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .role(user.getRole().name())
                .userId(user.getId())
                .patientId(user.getPatientId())
                .doctorId(user.getDoctorId())
                .build();
    }

    public AuthResponseDTO refresh(String refreshToken) {
        try {
            String username = jwtTokenProvider.extractUsername(refreshToken);
            if (username == null || jwtTokenProvider.isTokenExpired(refreshToken)) {
                throw new AuthException("Недействительный refresh токен");
            }

            User user = userService.findByUsername(username);
            String newAccessToken = jwtTokenProvider.generateAccessToken(username, user.getRole().name());
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

            return AuthResponseDTO.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .role(user.getRole().name())
                    .userId(user.getId())
                    .patientId(user.getPatientId())
                    .doctorId(user.getDoctorId())
                    .build();
        } catch (Exception e) {
            throw new AuthException("Ошибка обновления токена: " + e.getMessage());
        }
    }

    public boolean validateToken(String token) {
        try {
            String username = jwtTokenProvider.extractUsername(token);
            return username != null && !jwtTokenProvider.isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}