import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { HealthSummary } from '../../models/health.model';

type SimState = 'all-good' | 'alert' | 'incident';

@Component({
  selector: 'app-cctv-health-monitoring',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cctv-health-monitoring.component.html',
  styleUrl: './cctv-health-monitoring.component.scss',
})
export class CctvHealthMonitoringComponent implements OnInit {
  simState: SimState = 'all-good';
  openDropdown: 'profile' | null = null;
  summary: HealthSummary | null = null;
  loading = true;
  error: string | null = null;

  constructor(private router: Router, private api: ApiService) {}

  ngOnInit(): void {
    this.loadSummary();
  }

  private loadSummary(): void {
    this.loading = true;
    this.api.getHealthSummary().subscribe({
      next: s => { this.summary = s; this.loading = false; },
      error: () => { this.error = 'Failed to load health data.'; this.loading = false; },
    });
  }

  setSimState(state: SimState, event: Event): void {
    event.stopPropagation();
    this.simState = state;
    this.openDropdown = null;
    this.loading = true;
    this.api.simulateHealthState(state).subscribe({
      next: s => { this.summary = s; this.loading = false; },
      error: () => { this.error = 'Failed to update simulation state.'; this.loading = false; },
    });
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
}
