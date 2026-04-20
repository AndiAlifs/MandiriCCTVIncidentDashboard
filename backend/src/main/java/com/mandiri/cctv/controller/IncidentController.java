package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.IncidentDto;
import com.mandiri.cctv.entity.Incident;
import com.mandiri.cctv.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

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

    @PatchMapping("/{id}/status")
    public ResponseEntity<IncidentDto> updateStatus(
            @PathVariable Long id,
            @RequestParam Incident.Status status) {
        return ResponseEntity.ok(incidentService.updateStatus(id, status));
    }
}
