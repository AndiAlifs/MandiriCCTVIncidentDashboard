package com.mandiri.cctv.dto;

import com.mandiri.cctv.entity.Incident;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class SimulateRequest {

    public record AlertRequest(
        @Schema(description = "Type of incident to simulate", example = "ATM_LOITERING", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Incident.Type type
    ) {}

    public record HealthStateRequest(
        @Schema(description = "Health state to simulate (e.g. DEGRADED, CRITICAL, NORMAL)", example = "DEGRADED", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull String state
    ) {}
}
