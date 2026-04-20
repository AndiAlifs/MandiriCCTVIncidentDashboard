package com.mandiri.cctv.service;

import com.mandiri.cctv.dto.IncidentDto;
import com.mandiri.cctv.entity.Incident;
import com.mandiri.cctv.repository.IncidentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;

    @Transactional(readOnly = true)
    public Page<IncidentDto> findAll(
            Incident.Status status,
            Incident.Type type,
            Instant from,
            Instant to,
            Pageable pageable) {
        return incidentRepository.findFiltered(status, type, from, to, pageable)
            .map(IncidentDto::from);
    }

    @Transactional(readOnly = true)
    public IncidentDto findById(Long id) {
        return incidentRepository.findById(id)
            .map(IncidentDto::from)
            .orElseThrow(() -> new EntityNotFoundException("Incident not found: " + id));
    }

    @Transactional
    public IncidentDto updateStatus(Long id, Incident.Status newStatus) {
        Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Incident not found: " + id));
        incident.setStatus(newStatus);
        if (newStatus == Incident.Status.RESOLVED && incident.getClearedAt() == null) {
            incident.setClearedAt(Instant.now());
        }
        return IncidentDto.from(incidentRepository.save(incident));
    }
}
