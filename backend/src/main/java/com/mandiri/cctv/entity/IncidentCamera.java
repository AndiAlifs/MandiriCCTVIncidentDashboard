package com.mandiri.cctv.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "incident_cameras")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IncidentCamera {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "camera_name", length = 255)
    private String cameraName;

    @Column(length = 500)
    private String url;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
