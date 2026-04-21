package com.mandiri.cctv.service;

import com.mandiri.cctv.dto.IncidentDto;
import com.mandiri.cctv.dto.OtherCameraDto;
import com.mandiri.cctv.entity.Incident;
import com.mandiri.cctv.entity.IncidentCamera;
import com.mandiri.cctv.repository.IncidentCameraRepository;
import com.mandiri.cctv.repository.IncidentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private static final List<String> VIDEO_POOL = List.of(
        "/asset/asap_1.mp4", "/asset/unatorized_access.mp4", "/asset/asap_2.mp4"
    );
    private static final DateTimeFormatter DISPLAY_FMT =
        DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss", Locale.ENGLISH)
            .withZone(ZoneId.of("Asia/Jakarta"));

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    private final IncidentRepository incidentRepository;
    private final IncidentCameraRepository incidentCameraRepository;
    private final SseEmitterService sseEmitterService;

    @Transactional(readOnly = true)
    public Page<IncidentDto> findAll(
            Incident.Status status,
            Incident.Type type,
            Instant from,
            Instant to,
            Pageable pageable) {
        Specification<Incident> spec = Specification.where(null);
        if (status != null) spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), status));
        if (type   != null) spec = spec.and((r, q, cb) -> cb.equal(r.get("type"), type));
        if (from   != null) spec = spec.and((r, q, cb) -> cb.greaterThanOrEqualTo(r.get("detectedAt"), from));
        if (to     != null) spec = spec.and((r, q, cb) -> cb.lessThanOrEqualTo(r.get("detectedAt"), to));
        return incidentRepository.findAll(spec, pageable).map(i -> IncidentDto.from(i, frontendBaseUrl));
    }

    @Transactional(readOnly = true)
    public IncidentDto findById(Long id) {
        return incidentRepository.findById(id)
            .map(i -> IncidentDto.from(i, frontendBaseUrl))
            .orElseThrow(() -> new EntityNotFoundException("Incident not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<OtherCameraDto> getOtherCameras(Long incidentId) {
        incidentRepository.findById(incidentId)
            .orElseThrow(() -> new EntityNotFoundException("Incident not found: " + incidentId));

        Instant now = Instant.now();
        List<IncidentCamera> stored = incidentCameraRepository.findByIncidentId(incidentId);

        // Deduplicate: keep only the latest entry per (ipAddress, cameraName)
        return stored.stream()
            .collect(java.util.stream.Collectors.toMap(
                ic -> ic.getIpAddress() + "|" + ic.getCameraName(),
                ic -> ic,
                (a, b) -> a.getCreatedAt().isAfter(b.getCreatedAt()) ? a : b
            ))
            .values().stream()
            .sorted(Comparator.comparing(IncidentCamera::getCreatedAt).reversed())
            .map(ic -> {
                String ts = DISPLAY_FMT.format(ic.getCreatedAt()) + " WIB";
                String el = formatElapsed(Duration.between(ic.getCreatedAt(), now));
                String videoSrc = VIDEO_POOL.get((int) (ic.getId() % VIDEO_POOL.size()));
                return new OtherCameraDto(ic.getId(), ic.getCameraName(), ic.getIpAddress(), ts, el, videoSrc);
            })
            .toList();
    }

    @Transactional
    public IncidentDto updateStatus(Long id, Incident.Status newStatus) {
        Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Incident not found: " + id));
        incident.setStatus(newStatus);
        if (newStatus == Incident.Status.RESOLVED && incident.getClearedAt() == null) {
            incident.setClearedAt(Instant.now());
        }
        return IncidentDto.from(incidentRepository.save(incident), frontendBaseUrl);
    }

    @Transactional(readOnly = true)
    public IncidentDto findByToken(String token) {
        return incidentRepository.findByClearToken(token)
            .map(i -> IncidentDto.from(i, frontendBaseUrl))
            .orElseThrow(() -> new EntityNotFoundException("Incident not found for token"));
    }

    @Transactional
    public IncidentDto clearByToken(String token) {
        Incident incident = incidentRepository.findByClearToken(token)
            .orElseThrow(() -> new EntityNotFoundException("Incident not found for token"));
        incident.setStatus(Incident.Status.RESOLVED);
        if (incident.getClearedAt() == null) {
            incident.setClearedAt(Instant.now());
        }
        IncidentDto dto = IncidentDto.from(incidentRepository.save(incident), frontendBaseUrl);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                sseEmitterService.broadcast("INCIDENT_CLEARED", dto);
            }
        });
        return dto;
    }

    private String formatElapsed(Duration d) {
        long totalSeconds = d.getSeconds();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        if (minutes > 0) return minutes + "m " + seconds + "s ago";
        return seconds + "s ago";
    }
}
