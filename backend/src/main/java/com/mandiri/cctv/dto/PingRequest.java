package com.mandiri.cctv.dto;

import jakarta.validation.constraints.NotNull;

public record PingRequest(
    @NotNull Long deviceId,
    String ipAddress
) {}
