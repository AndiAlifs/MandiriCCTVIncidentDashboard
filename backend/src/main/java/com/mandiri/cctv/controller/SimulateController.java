package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.HealthSummary;
import com.mandiri.cctv.dto.IncidentDto;
import com.mandiri.cctv.dto.SimulateRequest;
import com.mandiri.cctv.service.SimulationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/simulate")
@RequiredArgsConstructor
public class SimulateController {

    private final SimulationService simulationService;

    @PostMapping("/alert")
    public ResponseEntity<IncidentDto> alert(@Valid @RequestBody SimulateRequest.AlertRequest req) {
        return ResponseEntity.ok(simulationService.simulateAlert(req.type()));
    }

    @PostMapping("/health-state")
    public ResponseEntity<HealthSummary> healthState(@Valid @RequestBody SimulateRequest.HealthStateRequest req) {
        return ResponseEntity.ok(simulationService.simulateHealthState(req.state()));
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reset() {
        simulationService.reset();
        return ResponseEntity.ok().build();
    }
}
