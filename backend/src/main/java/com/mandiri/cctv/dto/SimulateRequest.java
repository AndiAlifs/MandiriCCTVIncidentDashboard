package com.mandiri.cctv.dto;

import com.mandiri.cctv.entity.Incident;
import jakarta.validation.constraints.NotNull;

public class SimulateRequest {

    public record AlertRequest(@NotNull Incident.Type type) {}

    public record HealthStateRequest(@NotNull String state) {}
}
