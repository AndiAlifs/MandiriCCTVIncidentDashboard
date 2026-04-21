package com.mandiri.cctv.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PingRequest(
    @Schema(description = "ID of the device sending the ping", example = "42", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull Long deviceId,

    @Schema(description = "Current IP address of the device", example = "192.168.1.101", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String ipAddress
) {}
