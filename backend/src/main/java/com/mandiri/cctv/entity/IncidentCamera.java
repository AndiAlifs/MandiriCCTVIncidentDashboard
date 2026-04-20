package com.mandiri.cctv.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "incident_cameras")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IncidentCamera {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(length = 500)
    private String url;
}
