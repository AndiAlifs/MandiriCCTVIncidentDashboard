import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

export interface CameraFeed {
  no: number;
  id: string;
  name: string;
  location: string;
  branch: string;
  type: string;
  status: 'online' | 'offline';
  lastUpdate: Date;
}

const MOCK_CAMERAS: CameraFeed[] = [
  { no: 1, id: 'CAM-SUD-001', name: 'Kamera - 1', location: 'Lobby Utama',      branch: 'KCU Sudirman',      type: 'PTZ',   status: 'online',  lastUpdate: new Date('2026-04-15T09:28:00') },
  { no: 2, id: 'CAM-SUD-002', name: 'Kamera - 2', location: 'Teller Area',      branch: 'KCU Sudirman',      type: 'Fixed', status: 'online',  lastUpdate: new Date('2026-04-15T09:27:00') },
  { no: 3, id: 'CAM-KUN-001', name: 'Kamera - 3', location: 'Pintu Masuk',      branch: 'KCP Kuningan',      type: 'Fixed', status: 'offline', lastUpdate: new Date('2026-04-15T07:45:00') },
  { no: 4, id: 'CAM-GKS-001', name: 'Kamera - 4', location: 'Area ATM',         branch: 'KCU Gatot Subroto', type: 'Dome',  status: 'online',  lastUpdate: new Date('2026-04-15T09:25:00') },
  { no: 5, id: 'CAM-SEN-001', name: 'Kamera - 5', location: 'Parkir Bawah',     branch: 'KCP Senen',         type: 'PTZ',   status: 'online',  lastUpdate: new Date('2026-04-15T09:20:00') },
  { no: 6, id: 'CAM-MGD-001', name: 'Kamera - 6', location: 'Ruang Server',     branch: 'KCU Mangga Dua',    type: 'Fixed', status: 'online',  lastUpdate: new Date('2026-04-15T09:30:00') },
  { no: 7, id: 'CAM-BLM-001', name: 'Kamera - 7', location: 'Lobby',            branch: 'KCP Blok M',        type: 'Dome',  status: 'online',  lastUpdate: new Date('2026-04-15T09:29:00') },
  { no: 8, id: 'CAM-GJM-001', name: 'Kamera - 8', location: 'Area Kasir',       branch: 'KCU Gajah Mada',    type: 'Fixed', status: 'offline', lastUpdate: new Date('2026-04-15T08:15:00') },
];

@Component({
  selector: 'app-incident-monitoring',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './incident-monitoring.component.html',
  styleUrl: './incident-monitoring.component.scss',
})
export class IncidentMonitoringComponent {
  readonly currentDate = new Date();

  filterStatus = 'all';
  filterType = 'all';
  filterBranch = 'all';

  readonly cameras: CameraFeed[] = MOCK_CAMERAS;

  get filteredCameras(): CameraFeed[] {
    return this.cameras.filter(c => {
      if (this.filterStatus !== 'all' && c.status !== this.filterStatus) return false;
      if (this.filterType !== 'all' && c.type !== this.filterType) return false;
      if (this.filterBranch !== 'all' && c.branch !== this.filterBranch) return false;
      return true;
    });
  }

  get onlineCount(): number { return this.cameras.filter(c => c.status === 'online').length; }
  get offlineCount(): number { return this.cameras.filter(c => c.status === 'offline').length; }
}
