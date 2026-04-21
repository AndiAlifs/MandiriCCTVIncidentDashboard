package com.mandiri.cctv.dto;

import com.mandiri.cctv.entity.Incident;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record IncidentDto(
    @Schema(description = "Incident ID", example = "1001")
    Long id,

    @Schema(description = "IP address of the reporting camera device", example = "192.168.1.101")
    String ipAddress,

    @Schema(description = "Camera name that detected the incident", example = "CAM-ATM-01")
    String cameraName,

    @Schema(description = "Branch name where the incident occurred", example = "KCU Sudirman")
    String branchName,

    @Schema(description = "Branch code identifier", example = "KCU001")
    String branchCode,

    @Schema(description = "Region of the branch", example = "DKI Jakarta")
    String region,

    @Schema(description = "Area group classification", example = "Jabodetabek")
    String areaGroup,

    @Schema(description = "Type of incident", example = "ATM_LOITERING")
    Incident.Type type,

    @Schema(description = "Severity level of the incident", example = "MEDIUM")
    Incident.Severity severity,

    @Schema(description = "Current status of the incident", example = "OPEN")
    Incident.Status status,

    @Schema(description = "Activity description at the time of detection", example = "Suspicious individual loitering near ATM for over 10 minutes")
    String activity,

    @Schema(description = "URL to the evidence video clip", example = "http://192.168.1.101/recordings/cam01_20260421_083000.mp4")
    String evidenceUrl,

    @Schema(description = "Timestamp when the incident was detected", example = "2026-04-21T08:30:00Z")
    Instant detectedAt,

    @Schema(description = "Timestamp when the incident was cleared", example = "2026-04-21T09:00:00Z")
    Instant clearedAt,

    @Schema(description = "One-time token used to clear the incident", example = "550e8400-e29b-41d4-a716-446655440000")
    String clearToken,

    @Schema(description = "Full URL to clear the incident via browser", example = "http://localhost:4200/incident/clear/550e8400-e29b-41d4-a716-446655440000")
    String clearUrl
) {
    public static IncidentDto from(Incident i, String frontendBaseUrl) {
        String token = i.getClearToken();
        return new IncidentDto(
            i.getId(),
            i.getIpAddress(),
            i.getCameraName(),
            i.getBranchName(),
            i.getBranchCode(),
            i.getRegion(),
            i.getAreaGroup(),
            i.getType(),
            i.getSeverity(),
            i.getStatus(),
            i.getActivity(),
            i.getEvidenceUrl(),
            i.getDetectedAt(),
            i.getClearedAt(),
            token,
            frontendBaseUrl + "/incident/clear/" + token
        );
    }
}
