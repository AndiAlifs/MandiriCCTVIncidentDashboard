package com.mandiri.cctv.controller;

import com.mandiri.cctv.dto.DeviceDto;
import com.mandiri.cctv.service.AdminDeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminDeviceService adminDeviceService;

    @GetMapping("/devices")
    public ResponseEntity<Page<DeviceDto>> listDevices(Pageable pageable) {
        return ResponseEntity.ok(adminDeviceService.findAll(pageable));
    }

    @GetMapping("/devices/{deviceId}")
    public ResponseEntity<DeviceDto> getDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(adminDeviceService.findById(deviceId));
    }

    @PostMapping("/devices")
    public ResponseEntity<DeviceDto> createDevice(@Valid @RequestBody DeviceDto.CreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminDeviceService.create(req));
    }

    @PutMapping("/devices/{deviceId}")
    public ResponseEntity<DeviceDto> updateDevice(
            @PathVariable Long deviceId,
            @Valid @RequestBody DeviceDto.UpdateRequest req) {
        return ResponseEntity.ok(adminDeviceService.update(deviceId, req));
    }

    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long deviceId) {
        adminDeviceService.delete(deviceId);
        return ResponseEntity.noContent().build();
    }
}
