export type DeviceStatus = 'ONLINE' | 'OFFLINE';

export interface Device {
  id: number;
  branchId: number;
  branchName: string;
  ipAddress: string | null;
  location: string;
  status: DeviceStatus;
  lastPing: string | null;
}

export interface DeviceCreateRequest {
  branchId: number;
  location: string;
  ipAddress?: string;
}
