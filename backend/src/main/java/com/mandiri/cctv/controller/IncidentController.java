package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.AlertRequest;
import com.mandiri.cctv.dto.IncidentDto;
import com.mandiri.cctv.dto.OtherCameraDto;
import com.mandiri.cctv.entity.Incident;
import com.mandiri.cctv.service.IngestService;
import com.mandiri.cctv.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Tag(name = "Incidents", description = "CCTV incident reporting, querying, and clearance")
@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;
    private final IngestService ingestService;

    @Operation(summary = "Report a new incident alert", description = "Receives an alert from a CCTV device. If an ongoing incident of the same type already exists at the same branch from a different camera, the camera is added as a secondary source instead of creating a new incident.")
    @PostMapping
    public ResponseEntity<IncidentDto> create(@Valid @RequestBody AlertRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingestService.handleAlert(req));
    }

    @Operation(summary = "List incidents", description = "Returns a paginated list of incidents, optionally filtered by status, type, and detected-at date range")
    @GetMapping
    public ResponseEntity<Page<IncidentDto>> list(
            @RequestParam(required = false) Incident.Status status,
            @RequestParam(required = false) Incident.Type   type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @PageableDefault(size = 20, sort = "detectedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(incidentService.findAll(status, type, from, to, pageable));
    }

    @Operation(summary = "Get incident by ID", description = "Returns the full details of a single incident including its current status, severity, and clear URL")
    @GetMapping("/{id}")
    public ResponseEntity<IncidentDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.findById(id));
    }

    @Operation(summary = "Get other cameras for an incident", description = "Returns additional camera sources that reported the same incident at the same branch")
    @GetMapping("/{id}/other-cameras")
    public ResponseEntity<List<OtherCameraDto>> otherCameras(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getOtherCameras(id));
    }

    @Operation(summary = "Update incident status", description = "Manually transitions an incident to a new status (OPEN, IN_PROGRESS, or RESOLVED)")
    @PatchMapping("/{id}/status")
    public ResponseEntity<IncidentDto> updateStatus(
            @PathVariable Long id,
            @RequestParam Incident.Status status) {
        return ResponseEntity.ok(incidentService.updateStatus(id, status));
    }

    @Operation(summary = "Get incident by clear token", description = "Retrieves an incident using its one-time clear token, used by the clearance page before confirming")
    @GetMapping("/token/{token}")
    public ResponseEntity<IncidentDto> getByToken(@PathVariable String token) {
        return ResponseEntity.ok(incidentService.findByToken(token));
    }

    @Operation(summary = "Clear incident by token", description = "Marks the incident as RESOLVED using its one-time clear token and broadcasts the clearance event via SSE")
    @PostMapping("/token/{token}/clear")
    public ResponseEntity<IncidentDto> clearByToken(@PathVariable String token) {
        return ResponseEntity.ok(incidentService.clearByToken(token));
    }
}
