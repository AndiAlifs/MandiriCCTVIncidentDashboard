package com.mandiri.cctv.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record AvailabilityStats(
    @Schema(description = "Total number of registered devices", example = "120")
    long totalDevices,

    @Schema(description = "Number of devices currently online", example = "112")
    long onlineDevices,

    @Schema(description = "Number of devices currently offline", example = "8")
    long offlineDevices,

    @Schema(description = "Overall uptime percentage", example = "93.33")
    double uptimePercent,

    @Schema(description = "Number of incidents with OPEN status", example = "3")
    long openIncidents,

    @Schema(description = "Number of incidents with IN_PROGRESS status", example = "1")
    long inProgressIncidents,

    @Schema(description = "Number of incidents with RESOLVED status", example = "47")
    long resolvedIncidents,

    @Schema(description = "Availability breakdown per region")
    List<RegionStats> byRegion
) {
    public record RegionStats(
        @Schema(description = "Region name", example = "DKI Jakarta")
        String region,

        @Schema(description = "Total devices in this region", example = "35")
        long totalDevices,

        @Schema(description = "Online devices in this region", example = "33")
        long onlineDevices,

        @Schema(description = "Uptime percentage for this region", example = "94.29")
        double uptimePercent
    ) {}
}
