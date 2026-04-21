package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.HealthSummary;
import com.mandiri.cctv.service.HealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health", description = "Device health monitoring for NVR and CCTV units")
@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    @Operation(summary = "Get health summary", description = "Returns NVR and CCTV online/offline counts, recent connectivity issues, unresolved offline devices, and per-branch offline rates")
    @GetMapping("/summary")
    public ResponseEntity<HealthSummary> summary() {
        return ResponseEntity.ok(healthService.getSummary());
    }
}
