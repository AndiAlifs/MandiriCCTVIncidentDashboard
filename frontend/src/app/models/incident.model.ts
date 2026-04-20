export type IncidentType =
  | 'FIRE_SMOKE'
  | 'UNAUTHORIZED_ACCESS'
  | 'ATM_LOITERING'
  | 'AFTER_HOURS';

export type IncidentSeverity = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
export type IncidentStatus   = 'OPEN' | 'IN_PROGRESS' | 'RESOLVED';

export interface Incident {
  id: number;
  deviceId: number;
  deviceLocation: string;
  branchName: string;
  region: string;
  areaGroup: string;
  type: IncidentType;
  severity: IncidentSeverity;
  status: IncidentStatus;
  description: string | null;
  activity: string | null;
  evidenceUrl: string | null;
  detectedAt: string;
  clearedAt: string | null;
  clearToken: string;
  clearUrl: string;
}

export interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface IncidentFilter {
  status?: IncidentStatus;
  type?: IncidentType;
  from?: string;
  to?: string;
  page?: number;
  size?: number;
  sort?: string;
}
