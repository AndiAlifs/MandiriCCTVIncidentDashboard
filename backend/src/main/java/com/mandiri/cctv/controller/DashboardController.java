package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.AvailabilityStats;
import com.mandiri.cctv.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/availability")
    public ResponseEntity<AvailabilityStats> getAvailability() {
        return ResponseEntity.ok(dashboardService.getAvailabilityStats());
    }
}
