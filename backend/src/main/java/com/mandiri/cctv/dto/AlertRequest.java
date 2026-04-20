package com.mandiri.cctv.dto;

import com.mandiri.cctv.entity.Incident;
import jakarta.validation.constraints.NotNull;

public record AlertRequest(
    @NotNull Long deviceId,
    @NotNull Incident.Type type,
    Incident.Severity severity,
    String activity,
    String evidenceUrl
) {}
