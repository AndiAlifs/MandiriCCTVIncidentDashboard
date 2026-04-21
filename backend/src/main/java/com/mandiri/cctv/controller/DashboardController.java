package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.AvailabilityStats;
import com.mandiri.cctv.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dashboard", description = "Aggregated statistics for the monitoring dashboard")
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Get availability statistics", description = "Returns overall device online/offline counts, uptime percentage, incident counts by status, and a per-region breakdown")
    @GetMapping("/availability")
    public ResponseEntity<AvailabilityStats> getAvailability() {
        return ResponseEntity.ok(dashboardService.getAvailabilityStats());
    }
}
