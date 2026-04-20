package com.mandiri.cctv.service;

import com.mandiri.cctv.dto.DeviceDto;
import com.mandiri.cctv.entity.Branch;
import com.mandiri.cctv.entity.Device;
import com.mandiri.cctv.repository.BranchRepository;
import com.mandiri.cctv.repository.DeviceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminDeviceService {

    private final DeviceRepository deviceRepository;
    private final BranchRepository branchRepository;

    @Transactional(readOnly = true)
    public Page<DeviceDto> findAll(Pageable pageable) {
        return deviceRepository.findAll(pageable).map(DeviceDto::from);
    }

    @Transactional
    public DeviceDto create(DeviceDto.CreateRequest req) {
        Branch branch = branchRepository.findById(req.branchId())
            .orElseThrow(() -> new EntityNotFoundException("Branch not found: " + req.branchId()));
        Device device = Device.builder()
            .branch(branch)
            .location(req.location())
            .ipAddress(req.ipAddress())
            .status(Device.Status.ONLINE)
            .build();
        return DeviceDto.from(deviceRepository.save(device));
    }

    @Transactional
    public DeviceDto update(Long id, DeviceDto.CreateRequest req) {
        Device device = deviceRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Device not found: " + id));
        Branch branch = branchRepository.findById(req.branchId())
            .orElseThrow(() -> new EntityNotFoundException("Branch not found: " + req.branchId()));
        device.setBranch(branch);
        device.setLocation(req.location());
        device.setIpAddress(req.ipAddress());
        return DeviceDto.from(deviceRepository.save(device));
    }

    @Transactional
    public void delete(Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new EntityNotFoundException("Device not found: " + id);
        }
        deviceRepository.deleteById(id);
    }
}
