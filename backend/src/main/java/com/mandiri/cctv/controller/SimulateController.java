package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.HealthSummary;
import com.mandiri.cctv.dto.IncidentDto;
import com.mandiri.cctv.dto.SimulateRequest;
import com.mandiri.cctv.service.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Simulation", description = "Endpoints for simulating incidents and device health states during testing")
@RestController
@RequestMapping("/api/v1/simulate")
@RequiredArgsConstructor
public class SimulateController {

    private final SimulationService simulationService;

    @Operation(summary = "Simulate an incident alert", description = "Triggers a simulated CCTV incident of the given type using a randomly selected branch device. Broadcasts the incident via SSE.")
    @RequestBody(required = true, content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = SimulateRequest.AlertRequest.class)
    ))
    @PostMapping("/alert")
    public ResponseEntity<IncidentDto> alert(@Valid @org.springframework.web.bind.annotation.RequestBody SimulateRequest.AlertRequest req) {
        return ResponseEntity.ok(simulationService.simulateAlert(req.type()));
    }

    @Operation(summary = "Simulate a device health state", description = "Forces a specific health state (e.g. DEGRADED, CRITICAL, NORMAL) on simulated devices to test dashboard behaviour")
    @RequestBody(required = true, content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = SimulateRequest.HealthStateRequest.class)
    ))
    @PostMapping("/health-state")
    public ResponseEntity<HealthSummary> healthState(@Valid @org.springframework.web.bind.annotation.RequestBody SimulateRequest.HealthStateRequest req) {
        return ResponseEntity.ok(simulationService.simulateHealthState(req.state()));
    }

    @Operation(summary = "Reset simulation", description = "Clears all simulated incidents and resets device health states back to their defaults")
    @PostMapping("/reset")
    public ResponseEntity<Void> reset() {
        simulationService.reset();
        return ResponseEntity.ok().build();
    }
}
