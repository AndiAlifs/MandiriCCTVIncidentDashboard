package com.mandiri.cctv.service;

import com.mandiri.cctv.dto.IncidentDto;
import com.mandiri.cctv.dto.OtherCameraDto;
import com.mandiri.cctv.entity.Device;
import com.mandiri.cctv.entity.Incident;
import com.mandiri.cctv.repository.DeviceRepository;
import com.mandiri.cctv.repository.IncidentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    private final IncidentRepository incidentRepository;
    private final DeviceRepository deviceRepository;

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
        return incidentRepository.findAll(spec, pageable).map(IncidentDto::from);
    }

    @Transactional(readOnly = true)
    public IncidentDto findById(Long id) {
        return incidentRepository.findById(id)
            .map(IncidentDto::from)
            .orElseThrow(() -> new EntityNotFoundException("Incident not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<OtherCameraDto> getOtherCameras(Long incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
            .orElseThrow(() -> new EntityNotFoundException("Incident not found: " + incidentId));
        Device incidentDevice = incident.getDevice();
        Long branchId = incidentDevice.getBranch().getId();
        Instant detectedAt = incident.getDetectedAt();
        Instant now = Instant.now();

        return deviceRepository.findOtherCamerasInBranch(branchId, incidentDevice.getId())
            .stream()
            .map(d -> {
                String videoSrc = VIDEO_POOL.get((int) (d.getId() % VIDEO_POOL.size()));
                String timestamp = DISPLAY_FMT.format(detectedAt) + " WIB";
                Duration elapsed = Duration.between(detectedAt, now);
                String elapsedStr = formatElapsed(elapsed);
                return new OtherCameraDto(d.getId(), d.getLocation(), d.getIpAddress(), timestamp, elapsedStr, videoSrc);
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
        return IncidentDto.from(incidentRepository.save(incident));
    }

    private String formatElapsed(Duration d) {
        long totalSeconds = d.getSeconds();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        if (minutes > 0) return minutes + "m " + seconds + "s ago";
        return seconds + "s ago";
    }
}
