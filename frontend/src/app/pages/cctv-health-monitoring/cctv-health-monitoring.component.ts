import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

type SimState = 'all-good' | 'alert' | 'incident';
type StatusLevel = 'Critical' | 'Medium' | 'Low';

interface DeviceIssue {
  ipAddress: string;
  branchName: string;
  lastSync: string;
}

interface UnresolvedIssue {
  ipAddress: string;
  branchName: string;
  downtime: string;
  status: StatusLevel;
}

interface BranchRate {
  branchName: string;
  offlineRate: number;
  status: StatusLevel;
}

const NVR_RECENT_ALERT: DeviceIssue[] = [
  { ipAddress: '192.168.0.45',  branchName: 'KCP Taman Ismail Marzuki', lastSync: '2 Minute Ago'   },
  { ipAddress: '172.16.254.3',  branchName: 'KCP Ciledug Tangerang',    lastSync: '40 minutes ago' },
];

const NVR_RECENT_INCIDENT: DeviceIssue[] = [
  ...NVR_RECENT_ALERT,
  { ipAddress: '203.0.113.9',   branchName: 'KCP Medan JW Marriot (dh Medan Adam Malik)', lastSync: '50 minutes ago' },
  { ipAddress: '198.51.100.27', branchName: 'KCP Jakarta Taman Surya',                    lastSync: '55 minutes ago' },
];

const NVR_UNRESOLVED: UnresolvedIssue[] = [
  { ipAddress: '10.125.1.220', branchName: 'KCP Taman Ismail Marzuki', downtime: '3 Days', status: 'Critical' },
  { ipAddress: '10.125.1.211', branchName: 'KCP Plaza Semanggi',       downtime: '2 Days', status: 'Medium'   },
  { ipAddress: '10.125.1.211', branchName: 'KCP Senayan City',         downtime: '2 Days', status: 'Medium'   },
  { ipAddress: '10.125.1.211', branchName: 'KCP AEON Mall BSD City',   downtime: '1 Days', status: 'Low'      },
];

const CCTV_RECENT: DeviceIssue[] = [
  { ipAddress: '192.168.45.12',  branchName: 'KC Jakarta Cengkareng',     lastSync: '5 Minutes Ago'  },
  { ipAddress: '172.16.254.1',   branchName: 'KC Jakarta Kebayoran Baru', lastSync: '10 Minutes Ago' },
  { ipAddress: '10.0.0.99',      branchName: 'KC Jakarta Senen',          lastSync: '15 Minutes Ago' },
  { ipAddress: '192.0.2.146',    branchName: 'KC Jakarta Pulo Gadung',    lastSync: '20 Minutes Ago' },
  { ipAddress: '203.0.113.76',   branchName: 'KC Jakarta Menteng',        lastSync: '25 Minutes Ago' },
  { ipAddress: '198.51.100.42',  branchName: 'KC Jakarta Palmerah',       lastSync: '30 Minutes Ago' },
  { ipAddress: '10.10.20.30',    branchName: 'KC Jakarta Tanah Abang',    lastSync: '35 Minutes Ago' },
  { ipAddress: '172.31.255.255', branchName: 'KC Jakarta Tanjung Duren',  lastSync: '40 Minutes Ago' },
  { ipAddress: '192.168.0.200',  branchName: 'KC Jakarta Jatinegara',     lastSync: '45 Minutes Ago' },
  { ipAddress: '192.168.0.200',  branchName: 'KC Jakarta Jatinegara',     lastSync: '45 Minutes Ago' },
];

const CCTV_UNRESOLVED: UnresolvedIssue[] = [
  { ipAddress: '10.125.1.220', branchName: 'KCP Taman Ismail Marzuki', downtime: '3 Days', status: 'Critical' },
  { ipAddress: '10.125.1.211', branchName: 'KCP Plaza Semanggi',       downtime: '2 Days', status: 'Medium'   },
  { ipAddress: '10.125.1.211', branchName: 'KCP Senayan City',         downtime: '2 Days', status: 'Medium'   },
  { ipAddress: '10.125.1.211', branchName: 'KCP AEON Mall BSD City',   downtime: '1 Days', status: 'Low'      },
];

const BRANCH_RATES: BranchRate[] = [
  { branchName: 'KCP Kabon Jeruk', offlineRate: 100, status: 'Critical' },
  { branchName: 'KCP Cengkareng',  offlineRate: 80,  status: 'Medium'   },
  { branchName: 'KCP Pondok Gede', offlineRate: 85,  status: 'Medium'   },
];

@Component({
  selector: 'app-cctv-health-monitoring',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cctv-health-monitoring.component.html',
  styleUrl: './cctv-health-monitoring.component.scss',
})
export class CctvHealthMonitoringComponent {
  simState: SimState = 'all-good';
  openDropdown: 'profile' | null = null;

  constructor(private router: Router) {}

  setSimState(state: SimState, event: Event): void {
    event.stopPropagation();
    this.simState = state;
    this.openDropdown = null;
  }

  toggleDropdown(event: Event): void {
    event.stopPropagation();
    this.openDropdown = this.openDropdown === 'profile' ? null : 'profile';
  }

  navigateTo(path: string, event?: Event): void {
    event?.stopPropagation();
    this.openDropdown = null;
    this.router.navigate([path]);
  }

  @HostListener('document:click')
  closeDropdowns(): void {
    this.openDropdown = null;
  }

  get nvrOffline(): number {
    if (this.simState === 'all-good') return 0;
    return this.simState === 'alert' ? 2 : 4;
  }

  get nvrRecentIssues(): DeviceIssue[] {
    if (this.simState === 'all-good') return [];
    return this.simState === 'alert' ? NVR_RECENT_ALERT : NVR_RECENT_INCIDENT;
  }

  get nvrUnresolved(): UnresolvedIssue[] {
    return this.simState === 'incident' ? NVR_UNRESOLVED : [];
  }

  get cctvOffline(): number {
    return this.simState === 'incident' ? 14 : 0;
  }

  get cctvMonitored(): number {
    return this.simState === 'incident' ? 17803 : 17892;
  }

  get cctvOnline(): number {
    return this.simState === 'incident' ? 17790 : 17892;
  }

  get cctvRecentIssues(): DeviceIssue[] {
    return this.simState === 'incident' ? CCTV_RECENT : [];
  }

  get cctvUnresolved(): UnresolvedIssue[] {
    return this.simState === 'incident' ? CCTV_UNRESOLVED : [];
  }

  get branchRates(): BranchRate[] {
    return this.simState === 'incident' ? BRANCH_RATES : [];
  }
}
