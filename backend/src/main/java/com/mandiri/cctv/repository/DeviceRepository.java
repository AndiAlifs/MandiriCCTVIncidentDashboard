package com.mandiri.cctv.repository;

import com.mandiri.cctv.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByBranchId(Long branchId);

    List<Device> findByStatus(Device.Status status);

    long countByStatus(Device.Status status);

    long countByDeviceType(Device.DeviceType deviceType);

    long countByDeviceTypeAndStatus(Device.DeviceType deviceType, Device.Status status);

    @Query("SELECT COUNT(d) FROM Device d")
    long countTotal();

    @Query("""
        SELECT d FROM Device d JOIN FETCH d.branch
        WHERE d.deviceType = :type AND d.status = 'OFFLINE'
          AND d.lastPing >= :since
        ORDER BY d.lastPing DESC
        LIMIT 10
        """)
    List<Device> findRecentOffline(@Param("type") Device.DeviceType type, @Param("since") Instant since);

    @Query("""
        SELECT d FROM Device d JOIN FETCH d.branch
        WHERE d.deviceType = :type AND d.status = 'OFFLINE'
          AND d.lastPing < :before
        ORDER BY d.lastPing ASC
        LIMIT 10
        """)
    List<Device> findUnresolvedOffline(@Param("type") Device.DeviceType type, @Param("before") Instant before);

    @Query("""
        SELECT d FROM Device d JOIN FETCH d.branch
        WHERE d.branch.id = :branchId
          AND d.id <> :excludeId
          AND d.deviceType = 'CCTV'
        ORDER BY d.id ASC
        LIMIT 4
        """)
    List<Device> findOtherCamerasInBranch(@Param("branchId") Long branchId, @Param("excludeId") Long excludeId);

    @Query("""
        SELECT d FROM Device d
        WHERE d.simulatedAt IS NOT NULL
        """)
    List<Device> findSimulated();

    @Query("""
        SELECT d FROM Device d JOIN FETCH d.branch
        WHERE d.deviceType = 'CCTV' AND d.status = 'OFFLINE'
        ORDER BY d.lastPing DESC
        LIMIT 14
        """)
    List<Device> findOfflineCctvForSimulation();
}
