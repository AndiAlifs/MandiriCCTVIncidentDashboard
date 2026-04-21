package com.mandiri.cctv.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "incidents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Incident {

    public enum Type {
        FIRE_SMOKE, UNAUTHORIZED_ACCESS, ATM_LOITERING, AFTER_HOURS
    }

    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }

    public enum Status { OPEN, IN_PROGRESS, RESOLVED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "branch_name", length = 255)
    private String branchName;

    @Column(name = "camera_name", length = 255)
    private String cameraName;

    @Column(length = 100)
    private String region;

    @Column(name = "area_group", length = 100)
    private String areaGroup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Severity severity = Severity.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.OPEN;

    @Column(columnDefinition = "TEXT")
    private String activity;

    @Column(name = "evidence_url", length = 500)
    private String evidenceUrl;

    @Column(name = "detected_at", nullable = false)
    private Instant detectedAt = Instant.now();

    @Column(name = "cleared_at")
    private Instant clearedAt;

    @Column(name = "simulated_at")
    private Instant simulatedAt;

    @Builder.Default
    @Column(name = "clear_token", unique = true, length = 36, nullable = false)
    private String clearToken = UUID.randomUUID().toString();

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<IncidentCamera> otherCameras = new ArrayList<>();
}
