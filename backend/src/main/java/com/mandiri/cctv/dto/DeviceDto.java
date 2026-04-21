package com.mandiri.cctv.dto;

import com.mandiri.cctv.entity.Device;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record DeviceDto(
    @Schema(description = "Device ID", example = "42")
    Long id,

    @Schema(description = "Branch ID the device belongs to", example = "7")
    Long branchId,

    @Schema(description = "Branch name the device belongs to", example = "KCU Sudirman")
    String branchName,

    @Schema(description = "IP address of the device", example = "192.168.1.101")
    String ipAddress,

    @Schema(description = "Physical location of the device within the branch", example = "ATM Area - Lantai 1")
    String location,

    @Schema(description = "Current status of the device", example = "ONLINE")
    Device.Status status,

    @Schema(description = "Type of device", example = "CCTV")
    Device.DeviceType deviceType,

    @Schema(description = "Timestamp of the last received ping", example = "2026-04-21T08:25:00Z")
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
        @Schema(description = "ID of the branch to assign the device to", example = "7", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Long branchId,

        @Schema(description = "Physical location of the device within the branch", example = "ATM Area - Lantai 1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String location,

        @Schema(description = "IP address of the device", example = "192.168.1.101", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String ipAddress,

        @Schema(description = "Type of device", example = "CCTV", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Device.DeviceType deviceType
    ) {}

    public record UpdateRequest(
        @Schema(description = "ID of the branch to assign the device to", example = "7", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Long branchId,

        @Schema(description = "Physical location of the device within the branch", example = "ATM Area - Lantai 1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String location,

        @Schema(description = "IP address of the device", example = "192.168.1.101", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String ipAddress,

        @Schema(description = "Type of device", example = "NVR", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Device.DeviceType deviceType,

        @Schema(description = "Status of the device", example = "OFFLINE", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Device.Status status
    ) {}
}
