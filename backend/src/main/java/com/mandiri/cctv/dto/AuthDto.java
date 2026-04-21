package com.mandiri.cctv.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class AuthDto {

    public record LoginRequest(
        @Schema(description = "Username for login", example = "admin")
        @NotBlank String username,

        @Schema(description = "Password for login", example = "P@ssw0rd!")
        @NotBlank String password
    ) {}

    public record TokenResponse(
        @Schema(description = "JWT bearer token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.abc123")
        String token,

        @Schema(description = "Authenticated username", example = "admin")
        String username,

        @Schema(description = "Role assigned to the user", example = "ADMIN")
        String role
    ) {}
}
