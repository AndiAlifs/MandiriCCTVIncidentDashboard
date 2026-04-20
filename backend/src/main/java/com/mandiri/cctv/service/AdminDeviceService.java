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

    @Transactional(readOnly = true)
    public DeviceDto findById(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
            .orElseThrow(() -> new EntityNotFoundException("Device not found: " + deviceId));
        return DeviceDto.from(device);
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
            .deviceType(req.deviceType() != null ? req.deviceType() : Device.DeviceType.CCTV)
            .build();
        return DeviceDto.from(deviceRepository.save(device));
    }

    @Transactional
    public DeviceDto update(Long deviceId, DeviceDto.UpdateRequest req) {
        Device device = deviceRepository.findById(deviceId)
            .orElseThrow(() -> new EntityNotFoundException("Device not found: " + deviceId));
        Branch branch = branchRepository.findById(req.branchId())
            .orElseThrow(() -> new EntityNotFoundException("Branch not found: " + req.branchId()));
        device.setBranch(branch);
        device.setLocation(req.location());
        device.setIpAddress(req.ipAddress());
        if (req.deviceType() != null) device.setDeviceType(req.deviceType());
        if (req.status() != null) device.setStatus(req.status());
        return DeviceDto.from(deviceRepository.save(device));
    }

    @Transactional
    public void delete(Long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new EntityNotFoundException("Device not found: " + deviceId);
        }
        deviceRepository.deleteById(deviceId);
    }
}
