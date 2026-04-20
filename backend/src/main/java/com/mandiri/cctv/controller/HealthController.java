package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.HealthSummary;
import com.mandiri.cctv.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    @GetMapping("/summary")
    public ResponseEntity<HealthSummary> summary() {
        return ResponseEntity.ok(healthService.getSummary());
    }
}
