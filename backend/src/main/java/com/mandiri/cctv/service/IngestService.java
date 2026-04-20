package com.mandiri.cctv.service;

import com.mandiri.cctv.dto.AlertRequest;
import com.mandiri.cctv.dto.PingRequest;
import com.mandiri.cctv.entity.Device;
import com.mandiri.cctv.entity.Incident;
import com.mandiri.cctv.repository.DeviceRepository;
import com.mandiri.cctv.repository.IncidentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class IngestService {

    private final DeviceRepository deviceRepository;
    private final IncidentRepository incidentRepository;

    @Transactional
    public void handlePing(PingRequest req) {
        Device device = deviceRepository.findById(req.deviceId())
            .orElseThrow(() -> new EntityNotFoundException("Device not found: " + req.deviceId()));
        device.setStatus(Device.Status.ONLINE);
        device.setLastPing(Instant.now());
        if (req.ipAddress() != null) device.setIpAddress(req.ipAddress());
        deviceRepository.save(device);
    }

    @Transactional
    public Incident handleAlert(AlertRequest req) {
        Device device = deviceRepository.findById(req.deviceId())
            .orElseThrow(() -> new EntityNotFoundException("Device not found: " + req.deviceId()));

        Incident incident = Incident.builder()
            .device(device)
            .type(req.type())
            .severity(req.severity() != null ? req.severity() : Incident.Severity.MEDIUM)
            .status(Incident.Status.OPEN)
            .description(req.description())
            .evidenceUrl(req.evidenceUrl())
            .detectedAt(Instant.now())
            .build();

        return incidentRepository.save(incident);
    }
}
