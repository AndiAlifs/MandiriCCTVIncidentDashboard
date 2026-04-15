import { Component, HostListener, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

export interface IncidentRecord {
  id: number;
  region: string;
  areaGroup: string;
  branch: string;
  timeDetected: string;
  timeCleared: string;
  duration: string;
  area: string;
  indication: string;
  activity: string;
  month: string;
}

export interface IncidentType {
  icon: 'fire' | 'unauthorized' | 'atm' | 'afterhours';
  name: string;
  videoSrc: string | null;
  simulateBranch: string;
  simulateArea: string;
}

interface ActiveAlertState {
  type: IncidentType;
  elapsedSeconds: number;
  footageReady: boolean;
  _footageTimerId: ReturnType<typeof setTimeout> | null;
  _intervalId: ReturnType<typeof setInterval>;
  _timeoutId: ReturnType<typeof setTimeout>;
}

const INCIDENT_TYPES: IncidentType[] = [
  { icon: 'fire',         name: 'Fire & Smoke Detection',        videoSrc: '/asset/asap_1.mp4',              simulateBranch: 'KCP Taman Ismail Marzuki',       simulateArea: 'CAM 1 - Lounge Area' },
  { icon: 'unauthorized', name: 'Unauthorized Area Access',      videoSrc: '/asset/unatorized_access.mp4',   simulateBranch: 'KCP Jakarta Pamulang',           simulateArea: 'CAM 2 - Teller Area' },
  { icon: 'atm',          name: 'ATM Loitering Detection',       videoSrc: '/asset/asap_2.mp4',              simulateBranch: 'KC Jakarta Daan Mogot',          simulateArea: 'CAM 1 - ATM Area'    },
  { icon: 'afterhours',   name: 'After Hours Anomaly Detection', videoSrc: null,                             simulateBranch: 'KCP Tangerang Villa Melati Mas', simulateArea: 'CAM 3 - Lounge Area' },
];

const MOCK_INCIDENTS: IncidentRecord[] = [
  { id: 1,  region: 'Jakarta',   areaGroup: 'Jakarta Pusat',       branch: 'KCP Taman Ismail Marzuki',      month: 'March 2026', timeDetected: '11:30:20 WIB', timeCleared: '11:50:20 WIB', duration: '20m 5s',  area: 'CAM 1 - Lounge Area', indication: 'Fire & Smoke Detection',        activity: 'Smoke or fire appears in the building.'         },
  { id: 2,  region: 'Jakarta',   areaGroup: 'Jakarta Selatan',     branch: 'KCP Jakarta Pamulang',          month: 'March 2026', timeDetected: '10:30:20 WIB', timeCleared: '10:42:22 WIB', duration: '12m 2s',  area: 'CAM 2 - Teller Area', indication: 'Unauthorized Area Access',      activity: 'A customer has entered the Teller area.'       },
  { id: 3,  region: 'Jakarta',   areaGroup: 'Jakarta Barat',       branch: 'KC Jakarta Daan Mogot',         month: 'March 2026', timeDetected: '09:30:20 WIB', timeCleared: '09:35:55 WIB', duration: '5m 35s',  area: 'CAM 1 - ATM Area',    indication: 'ATM Loitering Detection',       activity: 'Someone is at the ATM for an extended period.' },
  { id: 4,  region: 'Tangerang', areaGroup: 'Tangerang Selatan',   branch: 'KCP Tangerang Villa Melati Mas',month: 'March 2026', timeDetected: '18:30:20 WIB', timeCleared: '18:33:10 WIB', duration: '3m 10s',  area: 'CAM 3 - Lounge Area', indication: 'After Hours Anomaly Detection', activity: 'Detecting activity at a closed branch.'        },
  { id: 5,  region: 'Jakarta',   areaGroup: 'Jakarta Timur',       branch: 'KC Jakarta Pondok Kelapa',      month: 'March 2026', timeDetected: '08:10:12 WIB', timeCleared: '08:22:12 WIB', duration: '12m 2s',  area: 'CAM 2 - Teller Area', indication: 'Fire & Smoke Detection',        activity: 'Smoke or fire appears in the building.'         },
  { id: 6,  region: 'Jakarta',   areaGroup: 'Jakarta Barat',       branch: 'KCP Jakarta Tomang',            month: 'March 2026', timeDetected: '08:00:55 WIB', timeCleared: '08:10:33 WIB', duration: '10m 22s', area: 'CAM 1 - Lounge Area', indication: 'Fire & Smoke Detection',        activity: 'Smoke or fire appears in the building.'         },
  { id: 7,  region: 'Jakarta',   areaGroup: 'Jakarta Barat',       branch: 'KCP Jakarta R.S. Pelni',        month: 'March 2026', timeDetected: '07:50:00 WIB', timeCleared: '08:00:55 WIB', duration: '10m 55s', area: 'CAM 2 - Teller Area', indication: 'Unauthorized Area Access',      activity: 'A customer has entered the Teller area.'       },
  { id: 8,  region: 'Jakarta',   areaGroup: 'Jakarta Utara',       branch: 'KC Jakarta Pluit Selatan',      month: 'March 2026', timeDetected: '07:44:55 WIB', timeCleared: '07:54:22 WIB', duration: '12m 33s', area: 'CAM 1 - ATM Area',    indication: 'ATM Loitering Detection',       activity: 'Someone is at the ATM for an extended period.' },
  { id: 9,  region: 'Jakarta',   areaGroup: 'Jakarta Utara',       branch: 'KCP Jakarta Kelapa Gading',     month: 'March 2026', timeDetected: '07:40:12 WIB', timeCleared: '07:55:12 WIB', duration: '15m 3s',  area: 'CAM 3 - Lounge Area', indication: 'After Hours Anomaly Detection', activity: 'Detecting activity at a closed branch.'        },
  { id: 10, region: 'Jakarta',   areaGroup: 'Jakarta Pusat',       branch: 'KCP Jakarta Graha Rekso',       month: 'March 2026', timeDetected: '07:20:39 WIB', timeCleared: '07:40:22 WIB', duration: '20m 15s', area: 'CAM 2 - Teller Area', indication: 'Fire & Smoke Detection',        activity: 'Smoke or fire appears in the building.'         },
];

const MONTHS = [
  'January 2026', 'February 2026', 'March 2026',    'April 2026',
  'May 2026',     'June 2026',     'July 2026',      'August 2026',
  'September 2026','October 2026', 'November 2026',  'December 2026',
];

@Component({
  selector: 'app-incident-monitoring',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './incident-monitoring.component.html',
  styleUrl: './incident-monitoring.component.scss',
})
export class IncidentMonitoringComponent implements OnDestroy {
  readonly incidentTypes: IncidentType[] = INCIDENT_TYPES;
  readonly months = MONTHS;

  private readonly allIncidents: IncidentRecord[] = MOCK_INCIDENTS;

  openDropdown: 'region' | 'area' | 'cabang' | 'month' | 'profile' | null = null;

  filterRegion = '';
  filterArea   = '';
  filterCabang = '';
  filterMonth  = 'March 2026';

  // ── Alert simulation ─────────────────────────────────────────────────────────

  activeAlerts: ActiveAlertState[] = [];

  isTypeActive(icon: string): boolean {
    return this.activeAlerts.some(a => a.type.icon === icon);
  }

  elapsedDisplay(seconds: number): string {
    return seconds < 60 ? `${seconds}s ago` : `${Math.floor(seconds / 60)}m ${seconds % 60}s ago`;
  }

  toggleAlert(type: IncidentType, event: Event): void {
    event.stopPropagation();
    if (this.isTypeActive(type.icon)) {
      this.stopAlert(type.icon);
    } else {
      this.startAlert(type);
    }
  }

  private startAlert(type: IncidentType): void {
    const state: ActiveAlertState = {
      type,
      elapsedSeconds: 0,
      footageReady: false,
      _footageTimerId: null,
      _intervalId: null!,
      _timeoutId: null!,
    };

    state._footageTimerId = setTimeout(() => {
      state.footageReady = true;
      state._footageTimerId = null;
    }, 1500);

    state._intervalId = setInterval(() => { state.elapsedSeconds++; }, 1000);
    state._timeoutId  = setTimeout(() => { this.stopAlert(type.icon); }, 60_000);

    this.activeAlerts.push(state);
  }

  stopAlert(icon: string): void {
    const idx = this.activeAlerts.findIndex(a => a.type.icon === icon);
    if (idx === -1) return;
    const s = this.activeAlerts[idx];
    if (s._footageTimerId) clearTimeout(s._footageTimerId);
    clearInterval(s._intervalId);
    clearTimeout(s._timeoutId);
    this.activeAlerts.splice(idx, 1);
  }

  clearAllAlerts(event?: Event): void {
    event?.stopPropagation();
    this.openDropdown = null;
    [...this.activeAlerts].forEach(a => this.stopAlert(a.type.icon));
  }

  ngOnDestroy(): void {
    this.clearAllAlerts();
  }

  // ── Derived option lists (cascade) ──────────────────────────────────────────

  get regions(): string[] {
    return [...new Set(this.allIncidents.map(i => i.region))].sort();
  }

  get areas(): string[] {
    const src = this.filterRegion
      ? this.allIncidents.filter(i => i.region === this.filterRegion)
      : this.allIncidents;
    return [...new Set(src.map(i => i.areaGroup))].sort();
  }

  get branches(): string[] {
    const src = this.allIncidents.filter(i => {
      if (this.filterRegion && i.region    !== this.filterRegion) return false;
      if (this.filterArea   && i.areaGroup !== this.filterArea)   return false;
      return true;
    });
    return [...new Set(src.map(i => i.branch))].sort();
  }

  // ── Filtered table data ──────────────────────────────────────────────────────

  get incidents(): IncidentRecord[] {
    return this.allIncidents.filter(i => {
      if (this.filterRegion && i.region    !== this.filterRegion) return false;
      if (this.filterArea   && i.areaGroup !== this.filterArea)   return false;
      if (this.filterCabang && i.branch    !== this.filterCabang) return false;
      if (this.filterMonth  && i.month     !== this.filterMonth)  return false;
      return true;
    });
  }

  // ── Dropdown control ─────────────────────────────────────────────────────────

  toggleDropdown(name: 'region' | 'area' | 'cabang' | 'month' | 'profile', event: Event): void {
    event.stopPropagation();
    this.openDropdown = this.openDropdown === name ? null : name;
  }

  selectRegion(value: string, event: Event): void {
    event.stopPropagation();
    this.filterRegion = value;
    this.filterArea   = '';
    this.filterCabang = '';
    this.openDropdown = null;
  }

  selectArea(value: string, event: Event): void {
    event.stopPropagation();
    this.filterArea   = value;
    this.filterCabang = '';
    this.openDropdown = null;
  }

  selectCabang(value: string, event: Event): void {
    event.stopPropagation();
    this.filterCabang = value;
    this.openDropdown = null;
  }

  selectMonth(value: string, event: Event): void {
    event.stopPropagation();
    this.filterMonth  = value;
    this.openDropdown = null;
  }

  @HostListener('document:click')
  closeDropdowns(): void {
    this.openDropdown = null;
  }
}
