package com.mandiri.cctv.service;

import com.mandiri.cctv.dto.AlertRequest;
import com.mandiri.cctv.dto.IncidentDto;
import com.mandiri.cctv.dto.PingRequest;
import com.mandiri.cctv.entity.Device;
import com.mandiri.cctv.entity.Incident;
import com.mandiri.cctv.entity.IncidentCamera;
import com.mandiri.cctv.repository.DeviceRepository;
import com.mandiri.cctv.repository.IncidentCameraRepository;
import com.mandiri.cctv.repository.IncidentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngestService {

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    private final DeviceRepository deviceRepository;
    private final IncidentRepository incidentRepository;
    private final IncidentCameraRepository incidentCameraRepository;
    private final SseEmitterService sseEmitterService;

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
    public IncidentDto handleAlert(AlertRequest req) {
        List<Incident> ongoing = incidentRepository.findOngoingByTypeAndBranch(req.type(), req.branchName());

        if (!ongoing.isEmpty()) {
            Incident existing = ongoing.get(0);

            // Always append a new entry — deduplication (latest per camera) is handled on read
            IncidentCamera cam = IncidentCamera.builder()
                .incident(existing)
                .ipAddress(req.ipAddress())
                .cameraName(req.cameraName())
                .url(req.evidenceUrl())
                .build();
            incidentCameraRepository.save(cam);

            return IncidentDto.from(existing, frontendBaseUrl);
        }

        // No ongoing incident — create a new one
        Incident incident = Incident.builder()
            .ipAddress(req.ipAddress())
            .branchName(req.branchName())
            .branchCode(req.branchCode())
            .cameraName(req.cameraName())
            .region(req.region())
            .areaGroup(req.areaGroup())
            .type(req.type())
            .severity(Incident.Severity.MEDIUM)
            .status(Incident.Status.OPEN)
            .activity(req.activity())
            .evidenceUrl(req.evidenceUrl())
            .detectedAt(req.time() != null ? req.time() : Instant.now())
            .build();

        Incident saved = incidentRepository.save(incident);
        IncidentDto dto = IncidentDto.from(saved, frontendBaseUrl);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                sseEmitterService.broadcast("INCIDENT_CREATED", dto);
            }
        });

        return dto;
    }
}
