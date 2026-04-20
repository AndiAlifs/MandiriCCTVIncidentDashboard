package com.mandiri.cctv.dto;

import java.util.List;

public record AvailabilityStats(
    long totalDevices,
    long onlineDevices,
    long offlineDevices,
    double uptimePercent,
    long openIncidents,
    long inProgressIncidents,
    long resolvedIncidents,
    List<RegionStats> byRegion
) {
    public record RegionStats(
        String region,
        long totalDevices,
        long onlineDevices,
        double uptimePercent
    ) {}
}
