package com.mandiri.cctv.service;

import com.mandiri.cctv.dto.HealthSummary;
import com.mandiri.cctv.dto.IncidentDto;
import com.mandiri.cctv.entity.Device;
import com.mandiri.cctv.entity.Incident;
import com.mandiri.cctv.repository.DeviceRepository;
import com.mandiri.cctv.repository.IncidentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final DeviceRepository deviceRepository;
    private final IncidentRepository incidentRepository;
    private final HealthService healthService;

    private static final Map<Incident.Type, Long> TYPE_TO_DEVICE = Map.of(
        Incident.Type.FIRE_SMOKE,           7L,
        Incident.Type.UNAUTHORIZED_ACCESS,  8L,
        Incident.Type.ATM_LOITERING,        9L,
        Incident.Type.AFTER_HOURS,          10L
    );

    @Transactional
    public IncidentDto simulateAlert(Incident.Type type) {
        Long deviceId = TYPE_TO_DEVICE.getOrDefault(type, 7L);
        Device device = deviceRepository.findById(deviceId)
            .orElseThrow(() -> new EntityNotFoundException("Device not found: " + deviceId));

        Instant now = Instant.now();
        Incident incident = Incident.builder()
            .device(device)
            .type(type)
            .severity(Incident.Severity.HIGH)
            .status(Incident.Status.OPEN)
            .description(descriptionFor(type))
            .detectedAt(now)
            .simulatedAt(now)
            .build();

        return IncidentDto.from(incidentRepository.save(incident));
    }

    @Transactional
    public HealthSummary simulateHealthState(String state) {
        Instant now = Instant.now();

        // Reset previous simulation
        deviceRepository.findSimulated().forEach(d -> {
            d.setStatus(Device.Status.ONLINE);
            d.setSimulatedAt(null);
        });
        deviceRepository.flush();

        if ("all-good".equals(state)) {
            return healthService.getSummary();
        }

        List<Device> targets = deviceRepository.findOfflineCctvForSimulation();
        int targetCount = "alert".equals(state) ? 2 : Math.min(14, targets.size());

        // For alert/incident: mark N additional devices offline (already offline in seed, just tag them)
        targets.stream().limit(targetCount).forEach(d -> d.setSimulatedAt(now));
        deviceRepository.flush();

        return healthService.getSummary();
    }

    @Transactional
    public void reset() {
        deviceRepository.findSimulated().forEach(d -> {
            d.setStatus(Device.Status.ONLINE);
            d.setSimulatedAt(null);
        });

        incidentRepository.findSimulated().forEach(i -> {
            i.setStatus(Incident.Status.RESOLVED);
            i.setClearedAt(Instant.now());
        });
    }

    private String descriptionFor(Incident.Type type) {
        return switch (type) {
            case FIRE_SMOKE           -> "Smoke or fire appears in the building.";
            case UNAUTHORIZED_ACCESS  -> "A customer has entered the Teller area.";
            case ATM_LOITERING        -> "Someone is at the ATM for an extended period.";
            case AFTER_HOURS          -> "Detecting activity at a closed branch.";
        };
    }
}
