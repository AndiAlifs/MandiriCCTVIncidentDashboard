package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.AlertRequest;
import com.mandiri.cctv.dto.IncidentDto;
import com.mandiri.cctv.dto.PingRequest;
import com.mandiri.cctv.entity.Incident;
import com.mandiri.cctv.service.IngestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ingest")
@RequiredArgsConstructor
public class IngestController {

    private final IngestService ingestService;

    @PostMapping("/ping")
    public ResponseEntity<Void> ping(@Valid @RequestBody PingRequest req) {
        ingestService.handlePing(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/alert")
    public ResponseEntity<IncidentDto> alert(@Valid @RequestBody AlertRequest req) {
        Incident saved = ingestService.handleAlert(req);
        return ResponseEntity.ok(IncidentDto.from(saved));
    }
}
