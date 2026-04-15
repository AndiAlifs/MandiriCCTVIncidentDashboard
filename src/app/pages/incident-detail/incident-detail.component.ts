import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { INCIDENT_TYPES, MOCK_INCIDENTS, IncidentRecord, IncidentType } from '../../data/incidents.data';

interface OtherCamera {
  cam: string;
  elapsed: string;
  videoSrc: string;
  timestamp: string;
}

const OTHER_CAMERAS: OtherCamera[] = [
  { cam: 'CAM 2 - Lounge Area', elapsed: '4m 1s ago',  videoSrc: '/asset/asap_1.mp4',           timestamp: '28 Mar 2026 10:30:20 WIB' },
  { cam: 'CAM 3 - ATM Area',    elapsed: '2m 7s ago',  videoSrc: '/asset/unatorized_access.mp4', timestamp: '28 Mar 2026 10:31:55 WIB' },
  { cam: 'CAM 4 - Teller Area', elapsed: '4m 30s ago', videoSrc: '/asset/asap_2.mp4',            timestamp: '28 Mar 2026 10:10:29 WIB' },
  { cam: 'CAM 5 - Teller Area', elapsed: '2m 10s ago', videoSrc: '/asset/asap_1.mp4',            timestamp: '28 Mar 2026 04:05:05 WIB' },
];

@Component({
  selector: 'app-incident-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './incident-detail.component.html',
  styleUrl: './incident-detail.component.scss',
})
export class IncidentDetailComponent implements OnInit {
  @ViewChild('mainVideo') mainVideo!: ElementRef<HTMLVideoElement>;

  incident!: IncidentRecord;
  incidentType!: IncidentType;

  isPlaying = false;
  currentTime = '0:00';
  totalDuration = '0:00';
  progressPercent = 0;

  readonly otherCameras = OTHER_CAMERAS;

  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.incident = MOCK_INCIDENTS.find(i => i.id === id) ?? MOCK_INCIDENTS[0];
    this.incidentType = INCIDENT_TYPES.find(t => t.name === this.incident.indication) ?? INCIDENT_TYPES[0];
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
    return '28 Mar 2026';
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
