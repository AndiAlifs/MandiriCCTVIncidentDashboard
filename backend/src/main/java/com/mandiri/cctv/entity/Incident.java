package com.mandiri.cctv.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

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
    private String description;

    @Column(name = "evidence_url", length = 500)
    private String evidenceUrl;

    @Column(name = "detected_at", nullable = false)
    private Instant detectedAt = Instant.now();

    @Column(name = "cleared_at")
    private Instant clearedAt;

    @Column(name = "simulated_at")
    private Instant simulatedAt;
}
