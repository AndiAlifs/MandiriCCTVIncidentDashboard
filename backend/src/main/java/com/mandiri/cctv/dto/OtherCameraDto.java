package com.mandiri.cctv.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OtherCameraDto(
    @Schema(description = "Camera record ID", example = "305")
    Long id,

    @Schema(description = "Camera name", example = "CAM-ATM-02")
    String cam,

    @Schema(description = "IP address of the camera", example = "192.168.1.102")
    String ipAddress,

    @Schema(description = "Formatted timestamp of detection", example = "21 Apr 2026, 08:32")
    String timestamp,

    @Schema(description = "Elapsed time since detection", example = "5 minutes ago")
    String elapsed,

    @Schema(description = "URL to the evidence video clip from this camera", example = "http://192.168.1.102/recordings/cam02_20260421_083200.mp4")
    String videoSrc
) {}
