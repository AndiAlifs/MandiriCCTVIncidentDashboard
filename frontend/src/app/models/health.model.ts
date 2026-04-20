export interface DeviceIssueDto {
  ipAddress: string;
  branchName: string;
  lastSync: string;
}

export interface UnresolvedIssueDto {
  ipAddress: string;
  branchName: string;
  downtime: string;
  status: 'Critical' | 'Medium' | 'Low';
}

export interface BranchRateDto {
  branchName: string;
  offlineRate: number;
  status: 'Critical' | 'Medium' | 'Low';
}

export interface HealthSummary {
  nvrTotal: number;
  nvrOnline: number;
  nvrOffline: number;
  cctvTotal: number;
  cctvOnline: number;
  cctvOffline: number;
  nvrRecentIssues: DeviceIssueDto[];
  nvrUnresolved: UnresolvedIssueDto[];
  cctvRecentIssues: DeviceIssueDto[];
  cctvUnresolved: UnresolvedIssueDto[];
  branchRates: BranchRateDto[];
}

export interface OtherCamera {
  id: number;
  cam: string;
  ipAddress: string | null;
  timestamp: string;
  elapsed: string;
  videoSrc: string;
}
