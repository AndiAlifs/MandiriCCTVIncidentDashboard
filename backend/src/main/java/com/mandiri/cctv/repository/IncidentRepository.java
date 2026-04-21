package com.mandiri.cctv.repository;

import com.mandiri.cctv.entity.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long>, JpaSpecificationExecutor<Incident> {

    Page<Incident> findByStatus(Incident.Status status, Pageable pageable);

    Page<Incident> findByType(Incident.Type type, Pageable pageable);

    long countByStatus(Incident.Status status);

    @Query("SELECT i.type, COUNT(i) FROM Incident i GROUP BY i.type")
    List<Object[]> countByType();

    @Query("SELECT i FROM Incident i WHERE i.simulatedAt IS NOT NULL")
    List<Incident> findSimulated();

    Optional<Incident> findByClearToken(String clearToken);

    @Query("""
        SELECT i FROM Incident i
        WHERE i.type = :type
          AND i.branchName = :branchName
          AND i.status IN ('OPEN', 'IN_PROGRESS')
        ORDER BY i.detectedAt DESC
        """)
    List<Incident> findOngoingByTypeAndBranch(
        @Param("type") Incident.Type type,
        @Param("branchName") String branchName
    );
}
