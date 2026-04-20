package com.mandiri.cctv.repository;

import com.mandiri.cctv.entity.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long>, JpaSpecificationExecutor<Incident> {

    Page<Incident> findByStatus(Incident.Status status, Pageable pageable);

    Page<Incident> findByType(Incident.Type type, Pageable pageable);

    long countByStatus(Incident.Status status);

    @Query("SELECT i.type, COUNT(i) FROM Incident i GROUP BY i.type")
    List<Object[]> countByType();

    @Query("""
        SELECT i FROM Incident i JOIN FETCH i.device d JOIN FETCH d.branch
        WHERE i.simulatedAt IS NOT NULL
        """)
    List<Incident> findSimulated();
}
