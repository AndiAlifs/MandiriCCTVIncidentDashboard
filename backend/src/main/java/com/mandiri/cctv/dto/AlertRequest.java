package com.mandiri.cctv.dto;

import com.mandiri.cctv.entity.Incident;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Schema(description = "Payload for reporting a new CCTV incident alert")
public record AlertRequest(
    @Schema(description = "IP address of the reporting device", example = "192.168.1.101", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank String ipAddress,

    @Schema(description = "Branch name where the incident occurred", example = "KCU Sudirman", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank String branchName,

    @Schema(description = "Branch code identifier", example = "KCU001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String branchCode,

    @Schema(description = "Camera name that detected the incident", example = "CAM-ATM-01", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank String cameraName,

    @Schema(description = "Region of the branch", example = "DKI Jakarta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String region,

    @Schema(description = "Area group classification", example = "Jabodetabek", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String areaGroup,

    @Schema(description = "Type of incident detected", example = "ATM_LOITERING", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull Incident.Type type,

    @Schema(description = "Timestamp when the incident was detected (ISO-8601); defaults to server time if omitted", example = "2026-04-21T08:30:00Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    Instant time,

    @Schema(description = "Activity description at the time of detection", example = "Suspicious individual loitering near ATM for over 10 minutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String activity,

    @Schema(description = "URL to the evidence image or video clip", example = "http://192.168.1.101/recordings/cam01_20260421_083000.mp4", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String evidenceUrl
) {}
