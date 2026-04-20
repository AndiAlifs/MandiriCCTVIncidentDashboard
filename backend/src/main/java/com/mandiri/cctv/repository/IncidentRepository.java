package com.mandiri.cctv.repository;

import com.mandiri.cctv.entity.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    Page<Incident> findByStatus(Incident.Status status, Pageable pageable);

    Page<Incident> findByType(Incident.Type type, Pageable pageable);

    @Query("""
        SELECT i FROM Incident i
        WHERE (:status IS NULL OR i.status = :status)
          AND (:type   IS NULL OR i.type   = :type)
          AND (:from   IS NULL OR i.detectedAt >= :from)
          AND (:to     IS NULL OR i.detectedAt <= :to)
        """)
    Page<Incident> findFiltered(
        @Param("status") Incident.Status status,
        @Param("type")   Incident.Type   type,
        @Param("from")   Instant from,
        @Param("to")     Instant to,
        Pageable pageable
    );

    long countByStatus(Incident.Status status);

    @Query("SELECT i.type, COUNT(i) FROM Incident i GROUP BY i.type")
    List<Object[]> countByType();
}
