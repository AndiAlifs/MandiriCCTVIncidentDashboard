export interface RegionStats {
  region: string;
  totalDevices: number;
  onlineDevices: number;
  uptimePercent: number;
}

export interface AvailabilityStats {
  totalDevices: number;
  onlineDevices: number;
  offlineDevices: number;
  uptimePercent: number;
  openIncidents: number;
  inProgressIncidents: number;
  resolvedIncidents: number;
  byRegion: RegionStats[];
}
