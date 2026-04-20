import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Device, DeviceCreateRequest } from '../../models/device.model';

@Component({
  selector: 'app-admin-devices',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-devices.component.html',
  styleUrl: './admin-devices.component.scss',
})
export class AdminDevicesComponent implements OnInit {

  devices: Device[] = [];
  totalElements = 0;
  page = 0;
  pageSize = 20;
  loading = true;
  error: string | null = null;

  showForm = false;
  editingId: number | null = null;
  form: DeviceCreateRequest = { branchId: 0, location: '' };

  constructor(private api: ApiService) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.api.getAdminDevices(this.page, this.pageSize).subscribe({
      next: res => {
        this.devices = res.content;
        this.totalElements = res.totalElements;
        this.loading = false;
      },
      error: () => { this.error = 'Failed to load devices.'; this.loading = false; },
    });
  }

  openCreate(): void {
    this.editingId = null;
    this.form = { branchId: 0, location: '' };
    this.showForm = true;
  }

  openEdit(d: Device): void {
    this.editingId = d.id;
    this.form = { branchId: d.branchId, location: d.location, ipAddress: d.ipAddress ?? undefined };
    this.showForm = true;
  }

  save(): void {
    const action = this.editingId
      ? this.api.updateDevice(this.editingId, this.form)
      : this.api.createDevice(this.form);
    action.subscribe({ next: () => { this.showForm = false; this.load(); } });
  }

  delete(id: number): void {
    if (!confirm('Delete this device?')) return;
    this.api.deleteDevice(id).subscribe({ next: () => this.load() });
  }

  nextPage(): void { this.page++; this.load(); }
  prevPage(): void { if (this.page > 0) { this.page--; this.load(); } }
}
