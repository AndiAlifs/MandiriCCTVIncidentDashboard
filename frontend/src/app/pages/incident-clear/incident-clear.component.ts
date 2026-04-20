import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Incident } from '../../models/incident.model';
import { IncidentTypeUi, typeUiFor } from '../../data/incident-types.data';

@Component({
  selector: 'app-incident-clear',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './incident-clear.component.html',
  styleUrl: './incident-clear.component.scss',
})
export class IncidentClearComponent implements OnInit {
  incident: Incident | null = null;
  incidentType: IncidentTypeUi | null = null;
  loading = true;
  error: string | null = null;
  clearing = false;
  cleared = false;

  private token = '';

  constructor(private route: ActivatedRoute, private api: ApiService) {}

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get('token') ?? '';
    if (!this.token) {
      this.error = 'Invalid link.';
      this.loading = false;
      return;
    }
    this.api.getIncidentByToken(this.token).subscribe({
      next: inc => {
        this.incident = inc;
        this.incidentType = typeUiFor(inc.type);
        this.cleared = inc.status === 'RESOLVED';
        this.loading = false;
      },
      error: () => {
        this.error = 'Incident not found or link is invalid.';
        this.loading = false;
      },
    });
  }

  markAsCleared(): void {
    if (this.clearing || this.cleared) return;
    this.clearing = true;
    this.api.clearIncidentByToken(this.token).subscribe({
      next: inc => {
        this.incident = inc;
        this.cleared = true;
        this.clearing = false;
      },
      error: () => {
        this.clearing = false;
        this.error = 'Failed to clear incident. Please try again.';
      },
    });
  }

  get timeDetected(): string {
    if (!this.incident) return '';
    return new Date(this.incident.detectedAt).toLocaleString('id-ID', {
      day: '2-digit', month: 'short', year: 'numeric',
      hour: '2-digit', minute: '2-digit', second: '2-digit',
      timeZone: 'Asia/Jakarta', hour12: false,
    }) + ' WIB';
  }

  get timeCleared(): string {
    if (!this.incident?.clearedAt) return '';
    return new Date(this.incident.clearedAt).toLocaleString('id-ID', {
      day: '2-digit', month: 'short', year: 'numeric',
      hour: '2-digit', minute: '2-digit', second: '2-digit',
      timeZone: 'Asia/Jakarta', hour12: false,
    }) + ' WIB';
  }
}
