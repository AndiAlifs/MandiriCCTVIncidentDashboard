package com.mandiri.cctv.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record HeartbeatRequest(
    @Schema(description = "IP address of the device sending the heartbeat", example = "192.168.1.101")
    String ipAddress
) {}
