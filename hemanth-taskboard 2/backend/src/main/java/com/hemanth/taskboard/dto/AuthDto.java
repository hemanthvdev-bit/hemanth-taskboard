package com.hemanth.taskboard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {
        @NotBlank
        @Size(min = 3, max = 30)
        private String username;

        @Email
        @NotBlank
        private String email;

        @NotBlank
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponse {
        private String token;
        private String username;
        private String role;
        private long expiresIn;
    }
}
