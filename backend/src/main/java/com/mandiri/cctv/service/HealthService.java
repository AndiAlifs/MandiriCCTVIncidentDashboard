package com.mandiri.cctv.service;

import com.mandiri.cctv.dto.HealthSummary;
import com.mandiri.cctv.dto.HealthSummary.BranchRateDto;
import com.mandiri.cctv.dto.HealthSummary.DeviceIssueDto;
import com.mandiri.cctv.dto.HealthSummary.UnresolvedIssueDto;
import com.mandiri.cctv.entity.Device;
import com.mandiri.cctv.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthService {

    private final DeviceRepository deviceRepository;

    @Transactional(readOnly = true)
    public HealthSummary getSummary() {
        Instant now = Instant.now();
        Instant oneHourAgo = now.minus(Duration.ofHours(1));
        Instant oneDayAgo  = now.minus(Duration.ofDays(1));

        long nvrTotal  = deviceRepository.countByDeviceType(Device.DeviceType.NVR);
        long nvrOnline = deviceRepository.countByDeviceTypeAndStatus(Device.DeviceType.NVR, Device.Status.ONLINE);
        long nvrOffline = nvrTotal - nvrOnline;

        long cctvTotal   = deviceRepository.countByDeviceType(Device.DeviceType.CCTV);
        long cctvOnline  = deviceRepository.countByDeviceTypeAndStatus(Device.DeviceType.CCTV, Device.Status.ONLINE);
        long cctvOffline = cctvTotal - cctvOnline;

        List<DeviceIssueDto> nvrRecent    = toIssues(deviceRepository.findRecentOffline(Device.DeviceType.NVR, oneHourAgo), now);
        List<UnresolvedIssueDto> nvrUnres = toUnresolved(deviceRepository.findUnresolvedOffline(Device.DeviceType.NVR, oneDayAgo), now);

        List<DeviceIssueDto> cctvRecent    = toIssues(deviceRepository.findRecentOffline(Device.DeviceType.CCTV, oneHourAgo), now);
        List<UnresolvedIssueDto> cctvUnres = toUnresolved(deviceRepository.findUnresolvedOffline(Device.DeviceType.CCTV, oneDayAgo), now);

        List<BranchRateDto> branchRates = buildBranchRates();

        return new HealthSummary(
            nvrTotal, nvrOnline, nvrOffline,
            cctvTotal, cctvOnline, cctvOffline,
            nvrRecent, nvrUnres,
            cctvRecent, cctvUnres,
            branchRates
        );
    }

    private List<DeviceIssueDto> toIssues(List<Device> devices, Instant now) {
        return devices.stream()
            .map(d -> new DeviceIssueDto(
                d.getIpAddress() != null ? d.getIpAddress() : "N/A",
                d.getBranch().getName(),
                relativeTime(d.getLastPing(), now)
            ))
            .toList();
    }

    private List<UnresolvedIssueDto> toUnresolved(List<Device> devices, Instant now) {
        return devices.stream()
            .map(d -> {
                Duration offline = Duration.between(d.getLastPing(), now);
                String downtime = formatDowntime(offline);
                String status   = severity(offline);
                return new UnresolvedIssueDto(
                    d.getIpAddress() != null ? d.getIpAddress() : "N/A",
                    d.getBranch().getName(),
                    downtime,
                    status
                );
            })
            .toList();
    }

    private List<BranchRateDto> buildBranchRates() {
        List<Device> allCctv = deviceRepository.findAll().stream()
            .filter(d -> d.getDeviceType() == Device.DeviceType.CCTV)
            .toList();

        Map<String, List<Device>> byBranch = allCctv.stream()
            .collect(Collectors.groupingBy(d -> d.getBranch().getName()));

        List<BranchRateDto> rates = new ArrayList<>();
        for (Map.Entry<String, List<Device>> entry : byBranch.entrySet()) {
            List<Device> devs = entry.getValue();
            long total   = devs.size();
            long offline = devs.stream().filter(d -> d.getStatus() == Device.Status.OFFLINE).count();
            if (total == 0 || offline == 0) continue;
            int rate = (int) (offline * 100 / total);
            rates.add(new BranchRateDto(entry.getKey(), rate, severity(rate)));
        }

        return rates.stream()
            .sorted(Comparator.comparingInt(BranchRateDto::offlineRate).reversed())
            .limit(3)
            .toList();
    }

    private String relativeTime(Instant ping, Instant now) {
        if (ping == null) return "Unknown";
        Duration d = Duration.between(ping, now);
        if (d.toMinutes() < 1)   return d.toSeconds() + " seconds ago";
        if (d.toHours() < 1)     return d.toMinutes() + " minutes ago";
        return d.toHours() + " hours ago";
    }

    private String formatDowntime(Duration d) {
        if (d.toDays() >= 1) return d.toDays() + " Day" + (d.toDays() > 1 ? "s" : "");
        if (d.toHours() >= 1) return d.toHours() + " Hour" + (d.toHours() > 1 ? "s" : "");
        return d.toMinutes() + " Minutes";
    }

    private String severity(Duration offline) {
        long days = offline.toDays();
        if (days >= 3) return "Critical";
        if (days >= 2) return "Medium";
        return "Low";
    }

    private String severity(int offlineRate) {
        if (offlineRate >= 90) return "Critical";
        if (offlineRate >= 50) return "Medium";
        return "Low";
    }
}
