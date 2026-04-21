package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.DeviceDto;
import com.mandiri.cctv.service.AdminDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin - Devices", description = "Admin-only CRUD operations for managing NVR and CCTV devices (requires ADMIN role)")
@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminDeviceService adminDeviceService;

    @Operation(summary = "List all devices", description = "Returns a paginated list of all registered NVR and CCTV devices across all branches", deprecated = true)
    @GetMapping("/devices")
    public ResponseEntity<Page<DeviceDto>> listDevices(Pageable pageable) {
        return ResponseEntity.ok(adminDeviceService.findAll(pageable));
    }

    @Operation(summary = "Get device by ID", description = "Returns the full details of a single device by its ID", deprecated = true)
    @GetMapping("/devices/{deviceId}")
    public ResponseEntity<DeviceDto> getDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(adminDeviceService.findById(deviceId));
    }

    @Operation(summary = "Create a device", description = "Registers a new NVR or CCTV device and assigns it to a branch", deprecated = true)
    @RequestBody(required = true, content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = DeviceDto.CreateRequest.class)
    ))
    @PostMapping("/devices")
    public ResponseEntity<DeviceDto> createDevice(@Valid @org.springframework.web.bind.annotation.RequestBody DeviceDto.CreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminDeviceService.create(req));
    }

    @Operation(summary = "Update a device", description = "Updates the branch assignment, location, IP address, device type, or status of an existing device", deprecated = true)
    @RequestBody(required = true, content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = DeviceDto.UpdateRequest.class)
    ))
    @PutMapping("/devices/{deviceId}")
    public ResponseEntity<DeviceDto> updateDevice(
            @PathVariable Long deviceId,
            @Valid @org.springframework.web.bind.annotation.RequestBody DeviceDto.UpdateRequest req) {
        return ResponseEntity.ok(adminDeviceService.update(deviceId, req));
    }

    @Operation(summary = "Delete a device", description = "Permanently removes a device from the system", deprecated = true)
    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long deviceId) {
        adminDeviceService.delete(deviceId);
        return ResponseEntity.noContent().build();
    }
}
