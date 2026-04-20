package com.mandiri.cctv.repository;

import com.mandiri.cctv.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByBranchId(Long branchId);

    List<Device> findByStatus(Device.Status status);

    long countByStatus(Device.Status status);

    @Query("SELECT COUNT(d) FROM Device d")
    long countTotal();
}
