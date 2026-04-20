import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Incident } from '../../models/incident.model';
import { INCIDENT_TYPE_UI, IncidentTypeUi } from '../../data/incident-types.data';
import { IncidentView, generateMonths, toIncidentView } from '../../services/incident-view.adapter';

interface ActiveAlertState {
  typeUi: IncidentTypeUi;
  elapsedSeconds: number;
  footageReady: boolean;
  incident: Incident;
  _footageTimerId: ReturnType<typeof setTimeout> | null;
  _intervalId: ReturnType<typeof setInterval>;
}

@Component({
  selector: 'app-incident-monitoring',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './incident-monitoring.component.html',
  styleUrl: './incident-monitoring.component.scss',
})
export class IncidentMonitoringComponent implements OnInit, OnDestroy {
  readonly incidentTypeUis: IncidentTypeUi[] = INCIDENT_TYPE_UI;
  readonly months = generateMonths();

  allIncidents: IncidentView[] = [];
  loading = true;
  error: string | null = null;

  openDropdown: 'region' | 'area' | 'cabang' | 'month' | 'profile' | null = null;

  filterRegion = '';
  filterArea   = '';
  filterCabang = '';
  filterMonth  = '';

  activeAlerts: ActiveAlertState[] = [];

  constructor(private router: Router, private api: ApiService) {}

  ngOnInit(): void {
    const now = new Date();
    this.filterMonth = now.toLocaleDateString('en-US', { month: 'long', year: 'numeric' });
    this.loadIncidents();
  }

  private loadIncidents(): void {
    this.loading = true;
    this.api.getIncidents({ size: 200 }).subscribe({
      next: page => {
        this.allIncidents = page.content.map(toIncidentView);
        this.loading = false;
      },
      error: () => { this.error = 'Failed to load incidents.'; this.loading = false; },
    });
  }

  // ── Navigation ───────────────────────────────────────────────────────────────

  navigateTo(path: string, event?: Event): void {
    event?.stopPropagation();
    this.openDropdown = null;
    this.router.navigate([path]);
  }

  navigateToIncident(incidentId: number, event?: Event): void {
    event?.stopPropagation();
    this.router.navigate(['/incident-detail', incidentId]);
  }

  navigateToAlertDetail(alert: ActiveAlertState, event?: Event): void {
    event?.stopPropagation();
    this.router.navigate(['/incident-detail', alert.incident.id]);
  }

  // ── Alert simulation ─────────────────────────────────────────────────────────

  isTypeActive(icon: string): boolean {
    return this.activeAlerts.some(a => a.typeUi.icon === icon);
  }

  elapsedDisplay(seconds: number): string {
    return seconds < 60 ? `${seconds}s ago` : `${Math.floor(seconds / 60)}m ${seconds % 60}s ago`;
  }

  toggleAlert(typeUi: IncidentTypeUi, event: Event): void {
    event.stopPropagation();
    if (this.isTypeActive(typeUi.icon)) {
      this.stopAlert(typeUi.icon);
    } else {
      this.startAlert(typeUi);
    }
  }

  private startAlert(typeUi: IncidentTypeUi): void {
    this.api.simulateAlert(typeUi.type).subscribe({
      next: incident => {
        const state: ActiveAlertState = {
          typeUi,
          elapsedSeconds: 0,
          footageReady: false,
          incident,
          _footageTimerId: null,
          _intervalId: null!,
        };

        state._footageTimerId = setTimeout(() => {
          state.footageReady = true;
          state._footageTimerId = null;
        }, 1500);

        state._intervalId = setInterval(() => { state.elapsedSeconds++; }, 1000);
        this.activeAlerts.push(state);
        this.loadIncidents();
      },
      error: () => { this.error = 'Failed to simulate alert.'; },
    });
  }

  stopAlert(icon: string): void {
    const idx = this.activeAlerts.findIndex(a => a.typeUi.icon === icon);
    if (idx === -1) return;
    const s = this.activeAlerts[idx];
    if (s._footageTimerId) clearTimeout(s._footageTimerId);
    clearInterval(s._intervalId);
    this.activeAlerts.splice(idx, 1);
  }

  clearAllAlerts(event?: Event): void {
    event?.stopPropagation();
    this.openDropdown = null;
    [...this.activeAlerts].forEach(a => this.stopAlert(a.typeUi.icon));
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

  get incidents(): IncidentView[] {
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
