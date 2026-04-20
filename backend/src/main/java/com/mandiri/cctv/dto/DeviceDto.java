package com.mandiri.cctv.dto;

import com.mandiri.cctv.entity.Device;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record DeviceDto(
    Long id,
    Long branchId,
    String branchName,
    String ipAddress,
    String location,
    Device.Status status,
    Device.DeviceType deviceType,
    Instant lastPing
) {
    public static DeviceDto from(Device d) {
        return new DeviceDto(
            d.getId(),
            d.getBranch().getId(),
            d.getBranch().getName(),
            d.getIpAddress(),
            d.getLocation(),
            d.getStatus(),
            d.getDeviceType(),
            d.getLastPing()
        );
    }

    public record CreateRequest(
        @NotNull Long branchId,
        @NotBlank String location,
        String ipAddress
    ) {}
}
