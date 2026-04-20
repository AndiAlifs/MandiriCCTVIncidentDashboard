package com.mandiri.cctv.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthDto {

    public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
    ) {}

    public record TokenResponse(
        String token,
        String username,
        String role
    ) {}
}
