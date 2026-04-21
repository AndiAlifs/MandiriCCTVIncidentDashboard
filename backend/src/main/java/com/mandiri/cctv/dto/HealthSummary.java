package com.mandiri.cctv.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record HealthSummary(
    @Schema(description = "Total number of NVR devices", example = "20")
    long nvrTotal,

    @Schema(description = "Number of NVR devices online", example = "18")
    long nvrOnline,

    @Schema(description = "Number of NVR devices offline", example = "2")
    long nvrOffline,

    @Schema(description = "Total number of CCTV devices", example = "100")
    long cctvTotal,

    @Schema(description = "Number of CCTV devices online", example = "94")
    long cctvOnline,

    @Schema(description = "Number of CCTV devices offline", example = "6")
    long cctvOffline,

    @Schema(description = "NVR devices with recent connectivity issues")
    List<DeviceIssueDto> nvrRecentIssues,

    @Schema(description = "NVR devices with unresolved offline status")
    List<UnresolvedIssueDto> nvrUnresolved,

    @Schema(description = "CCTV devices with recent connectivity issues")
    List<DeviceIssueDto> cctvRecentIssues,

    @Schema(description = "CCTV devices with unresolved offline status")
    List<UnresolvedIssueDto> cctvUnresolved,

    @Schema(description = "Offline rate per branch")
    List<BranchRateDto> branchRates
) {
    public record DeviceIssueDto(
        @Schema(description = "IP address of the device", example = "192.168.1.105")
        String ipAddress,

        @Schema(description = "Branch name the device belongs to", example = "KCU Sudirman")
        String branchName,

        @Schema(description = "Last sync timestamp (formatted)", example = "21 Apr 2026, 07:55")
        String lastSync
    ) {}

    public record UnresolvedIssueDto(
        @Schema(description = "IP address of the device", example = "192.168.1.110")
        String ipAddress,

        @Schema(description = "Branch name the device belongs to", example = "KCP Blok M")
        String branchName,

        @Schema(description = "Duration the device has been offline", example = "2 hours 15 minutes")
        String downtime,

        @Schema(description = "Current device status", example = "OFFLINE")
        String status
    ) {}

    public record BranchRateDto(
        @Schema(description = "Branch name", example = "KCP Blok M")
        String branchName,

        @Schema(description = "Percentage of offline devices at this branch", example = "25")
        int offlineRate,

        @Schema(description = "Health status label for this branch", example = "CRITICAL")
        String status
    ) {}
}
