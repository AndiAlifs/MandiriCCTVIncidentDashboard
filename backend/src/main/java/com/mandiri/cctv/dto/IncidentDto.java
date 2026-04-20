package com.mandiri.cctv.dto;

import com.mandiri.cctv.entity.Incident;

import java.time.Instant;

public record IncidentDto(
    Long id,
    Long deviceId,
    String deviceLocation,
    String branchName,
    String region,
    String areaGroup,
    Incident.Type type,
    Incident.Severity severity,
    Incident.Status status,
    String description,
    String evidenceUrl,
    Instant detectedAt,
    Instant clearedAt
) {
    public static IncidentDto from(Incident i) {
        var device = i.getDevice();
        var branch = device.getBranch();
        return new IncidentDto(
            i.getId(),
            device.getId(),
            device.getLocation(),
            branch.getName(),
            branch.getRegion(),
            branch.getAreaGroup(),
            i.getType(),
            i.getSeverity(),
            i.getStatus(),
            i.getDescription(),
            i.getEvidenceUrl(),
            i.getDetectedAt(),
            i.getClearedAt()
        );
    }
}
