package com.mandiri.cctv.repository;

import com.mandiri.cctv.entity.IncidentCamera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentCameraRepository extends JpaRepository<IncidentCamera, Long> {
    List<IncidentCamera> findByIncidentId(Long incidentId);
}
