package com.mandiri.cctv.dto;

import com.mandiri.cctv.entity.Incident;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AlertRequest(
    @NotNull Long deviceId,
    @NotNull Incident.Type type,
    Incident.Severity severity,
    String activity,
    String evidenceUrl,
    List<CameraEntry> otherCameras
) {
    public record CameraEntry(
        @NotNull Long deviceId,
        String url
    ) {}
}
