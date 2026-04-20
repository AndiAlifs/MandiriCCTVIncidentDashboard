import { Component, OnInit } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { ApiService } from '../../services/api.service';
import { AvailabilityStats } from '../../models/availability.model';

@Component({
  selector: 'app-cctv-availability',
  standalone: true,
  imports: [CommonModule, DecimalPipe],
  templateUrl: './cctv-availability.component.html',
  styleUrl: './cctv-availability.component.scss',
})
export class CctvAvailabilityComponent implements OnInit {

  stats: AvailabilityStats | null = null;
  loading = true;
  error: string | null = null;

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.api.getAvailabilityStats().subscribe({
      next: data => { this.stats = data; this.loading = false; },
      error: () => { this.error = 'Failed to load availability data.'; this.loading = false; },
    });
  }
}
