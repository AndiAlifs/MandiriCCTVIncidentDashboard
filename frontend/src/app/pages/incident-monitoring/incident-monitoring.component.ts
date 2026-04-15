import { Component, HostListener, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { INCIDENT_TYPES, MOCK_INCIDENTS, MONTHS, IncidentRecord, IncidentType } from '../../data/incidents.data';

interface ActiveAlertState {
  type: IncidentType;
  elapsedSeconds: number;
  footageReady: boolean;
  _footageTimerId: ReturnType<typeof setTimeout> | null;
  _intervalId: ReturnType<typeof setInterval>;
  _timeoutId: ReturnType<typeof setTimeout>;
}

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

  constructor(private router: Router) {}

  // ── Navigation ───────────────────────────────────────────────────────────────

  navigateToIncident(incidentId: number, event?: Event): void {
    event?.stopPropagation();
    this.router.navigate(['/incident-detail', incidentId]);
  }

  navigateToAlertDetail(alert: ActiveAlertState, event?: Event): void {
    event?.stopPropagation();
    this.router.navigate(['/incident-detail', alert.type.simulateIncidentId]);
  }

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
