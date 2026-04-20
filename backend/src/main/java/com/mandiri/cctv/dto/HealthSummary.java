package com.mandiri.cctv.dto;

import java.util.List;

public record HealthSummary(
    long nvrTotal,
    long nvrOnline,
    long nvrOffline,
    long cctvTotal,
    long cctvOnline,
    long cctvOffline,
    List<DeviceIssueDto> nvrRecentIssues,
    List<UnresolvedIssueDto> nvrUnresolved,
    List<DeviceIssueDto> cctvRecentIssues,
    List<UnresolvedIssueDto> cctvUnresolved,
    List<BranchRateDto> branchRates
) {
    public record DeviceIssueDto(String ipAddress, String branchName, String lastSync) {}
    public record UnresolvedIssueDto(String ipAddress, String branchName, String downtime, String status) {}
    public record BranchRateDto(String branchName, int offlineRate, String status) {}
}
