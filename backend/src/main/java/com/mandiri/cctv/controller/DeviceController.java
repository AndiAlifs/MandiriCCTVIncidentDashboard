package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.HeartbeatRequest;
import com.mandiri.cctv.dto.PingRequest;
import com.mandiri.cctv.service.IngestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Devices", description = "Device heartbeat and connectivity endpoints")
@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final IngestService ingestService;

    @Operation(summary = "Send device heartbeat", description = "Called periodically by NVR/CCTV devices to confirm they are online. Updates last ping timestamp and IP address if provided.")
    @RequestBody(required = false, content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = HeartbeatRequest.class)
    ))
    @PostMapping("/{id}/heartbeats")
    public ResponseEntity<Void> heartbeat(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody(required = false) HeartbeatRequest req) {
        ingestService.handlePing(new PingRequest(id, req != null ? req.ipAddress() : null));
        return ResponseEntity.ok().build();
    }
}
