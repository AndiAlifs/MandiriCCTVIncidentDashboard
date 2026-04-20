package com.mandiri.cctv.dto;

public record OtherCameraDto(
    Long id,
    String cam,
    String ipAddress,
    String timestamp,
    String elapsed,
    String videoSrc
) {}
