package com.mandiri.cctv.service;

import com.mandiri.cctv.dto.AvailabilityStats;
import com.mandiri.cctv.entity.Device;
import com.mandiri.cctv.entity.Incident;
import com.mandiri.cctv.repository.BranchRepository;
import com.mandiri.cctv.repository.DeviceRepository;
import com.mandiri.cctv.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DeviceRepository deviceRepository;
    private final IncidentRepository incidentRepository;
    private final BranchRepository branchRepository;

    @Transactional(readOnly = true)
    public AvailabilityStats getAvailabilityStats() {
        long total   = deviceRepository.countTotal();
        long online  = deviceRepository.countByStatus(Device.Status.ONLINE);
        long offline = total - online;
        double uptime = total == 0 ? 0 : (online * 100.0 / total);

        long open       = incidentRepository.countByStatus(Incident.Status.OPEN);
        long inProgress = incidentRepository.countByStatus(Incident.Status.IN_PROGRESS);
        long resolved   = incidentRepository.countByStatus(Incident.Status.RESOLVED);

        List<AvailabilityStats.RegionStats> byRegion = buildRegionStats();

        return new AvailabilityStats(total, online, offline, uptime, open, inProgress, resolved, byRegion);
    }

    private List<AvailabilityStats.RegionStats> buildRegionStats() {
        List<Device> allDevices = deviceRepository.findAll();

        Map<String, List<Device>> byRegion = allDevices.stream()
            .collect(Collectors.groupingBy(d -> d.getBranch().getRegion()));

        return byRegion.entrySet().stream()
            .map(entry -> {
                String region = entry.getKey();
                List<Device> devices = entry.getValue();
                long regionTotal  = devices.size();
                long regionOnline = devices.stream().filter(d -> d.getStatus() == Device.Status.ONLINE).count();
                double regionUptime = regionTotal == 0 ? 0 : (regionOnline * 100.0 / regionTotal);
                return new AvailabilityStats.RegionStats(region, regionTotal, regionOnline, regionUptime);
            })
            .toList();
    }
}
