package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.AlertRequest;
import com.mandiri.cctv.dto.IncidentDto;
import com.mandiri.cctv.dto.OtherCameraDto;
import com.mandiri.cctv.entity.Incident;
import com.mandiri.cctv.service.IngestService;
import com.mandiri.cctv.service.IncidentService;
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

@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;
    private final IngestService ingestService;

    @PostMapping
    public ResponseEntity<IncidentDto> create(@Valid @RequestBody AlertRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingestService.handleAlert(req));
    }

    @GetMapping
    public ResponseEntity<Page<IncidentDto>> list(
            @RequestParam(required = false) Incident.Status status,
            @RequestParam(required = false) Incident.Type   type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @PageableDefault(size = 20, sort = "detectedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(incidentService.findAll(status, type, from, to, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.findById(id));
    }

    @GetMapping("/{id}/other-cameras")
    public ResponseEntity<List<OtherCameraDto>> otherCameras(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getOtherCameras(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<IncidentDto> updateStatus(
            @PathVariable Long id,
            @RequestParam Incident.Status status) {
        return ResponseEntity.ok(incidentService.updateStatus(id, status));
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<IncidentDto> getByToken(@PathVariable String token) {
        return ResponseEntity.ok(incidentService.findByToken(token));
    }

    @PostMapping("/token/{token}/clear")
    public ResponseEntity<IncidentDto> clearByToken(@PathVariable String token) {
        return ResponseEntity.ok(incidentService.clearByToken(token));
    }
}
