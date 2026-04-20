import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Incident } from '../../models/incident.model';
import { OtherCamera } from '../../models/health.model';
import { IncidentTypeUi, typeUiFor } from '../../data/incident-types.data';

@Component({
  selector: 'app-incident-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './incident-detail.component.html',
  styleUrl: './incident-detail.component.scss',
})
export class IncidentDetailComponent implements OnInit {
  @ViewChild('mainVideo') mainVideo!: ElementRef<HTMLVideoElement>;

  incident: Incident | null = null;
  incidentType: IncidentTypeUi | null = null;
  otherCameras: OtherCamera[] = [];
  loading = true;
  error: string | null = null;

  isPlaying = false;
  currentTime = '0:00';
  totalDuration = '0:00';
  progressPercent = 0;

  constructor(private route: ActivatedRoute, private router: Router, private api: ApiService) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.api.getIncident(id).subscribe({
      next: inc => {
        this.incident = inc;
        this.incidentType = typeUiFor(inc.type);
        this.loading = false;
      },
      error: () => { this.error = 'Failed to load incident.'; this.loading = false; },
    });
    this.api.getOtherCameras(id).subscribe({
      next: cams => { this.otherCameras = cams; },
      error: () => { /* non-critical, just skip */ },
    });
  }

  goBack(): void {
    this.router.navigate(['/incident-monitoring']);
  }

  get detectionLabel(): string {
    switch (this.incidentType?.icon) {
      case 'fire':         return 'SMOKE DETECTED';
      case 'unauthorized': return 'UNAUTHORIZED ACCESS';
      case 'atm':          return 'ATM LOITERING';
      case 'afterhours':   return 'ANOMALY DETECTED';
      default:             return 'ALERT DETECTED';
    }
  }

  get incidentDateDisplay(): string {
    if (!this.incident) return '';
    return new Date(this.incident.detectedAt).toLocaleDateString('en-GB', {
      day: '2-digit', month: 'short', year: 'numeric', timeZone: 'Asia/Jakarta',
    });
  }

  get timeDetected(): string {
    if (!this.incident) return '';
    return new Date(this.incident.detectedAt).toLocaleTimeString('id-ID', {
      hour: '2-digit', minute: '2-digit', second: '2-digit',
      timeZone: 'Asia/Jakarta', hour12: false,
    }) + ' WIB';
  }

  get timeCleared(): string {
    if (!this.incident?.clearedAt) return '—';
    return new Date(this.incident.clearedAt).toLocaleTimeString('id-ID', {
      hour: '2-digit', minute: '2-digit', second: '2-digit',
      timeZone: 'Asia/Jakarta', hour12: false,
    }) + ' WIB';
  }

  get duration(): string {
    if (!this.incident?.clearedAt) return '—';
    const ms = new Date(this.incident.clearedAt).getTime() - new Date(this.incident.detectedAt).getTime();
    const totalSec = Math.floor(ms / 1000);
    const m = Math.floor(totalSec / 60);
    const s = totalSec % 60;
    return m > 0 ? `${m}m ${s}s` : `${s}s`;
  }

  togglePlay(): void {
    const video = this.mainVideo?.nativeElement;
    if (!video) return;
    if (video.paused) {
      video.play();
      this.isPlaying = true;
    } else {
      video.pause();
      this.isPlaying = false;
    }
  }

  onTimeUpdate(event: Event): void {
    const video = event.target as HTMLVideoElement;
    this.currentTime = this.formatTime(video.currentTime);
    this.progressPercent = video.duration ? (video.currentTime / video.duration) * 100 : 0;
  }

  onLoadedMetadata(event: Event): void {
    const video = event.target as HTMLVideoElement;
    this.totalDuration = this.formatTime(video.duration);
  }

  onVideoEnded(): void {
    this.isPlaying = false;
    this.progressPercent = 0;
    this.currentTime = '0:00';
  }

  private formatTime(seconds: number): string {
    if (!isFinite(seconds) || isNaN(seconds)) return '0:00';
    const m = Math.floor(seconds / 60);
    const s = Math.floor(seconds % 60);
    return `${m}:${s.toString().padStart(2, '0')}`;
  }
}
