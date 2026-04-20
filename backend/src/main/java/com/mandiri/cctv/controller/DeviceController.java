package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.HeartbeatRequest;
import com.mandiri.cctv.dto.PingRequest;
import com.mandiri.cctv.service.IngestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final IngestService ingestService;

    @PostMapping("/{id}/heartbeats")
    public ResponseEntity<Void> heartbeat(
            @PathVariable Long id,
            @RequestBody(required = false) HeartbeatRequest req) {
        ingestService.handlePing(new PingRequest(id, req != null ? req.ipAddress() : null));
        return ResponseEntity.ok().build();
    }
}
