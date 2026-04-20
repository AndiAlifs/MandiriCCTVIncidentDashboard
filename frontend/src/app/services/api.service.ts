import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Incident, IncidentFilter, IncidentType, PagedResponse } from '../models/incident.model';
import { AvailabilityStats } from '../models/availability.model';
import { Device, DeviceCreateRequest } from '../models/device.model';
import { HealthSummary, OtherCamera } from '../models/health.model';

@Injectable({ providedIn: 'root' })
export class ApiService {

  private base = environment.apiUrl;

  constructor(private http: HttpClient) {}

  // --- Dashboard ---
  getAvailabilityStats(): Observable<AvailabilityStats> {
    return this.http.get<AvailabilityStats>(`${this.base}/dashboard/availability`);
  }

  // --- Incidents ---
  getIncidents(filter: IncidentFilter = {}): Observable<PagedResponse<Incident>> {
    let params = new HttpParams();
    if (filter.status) params = params.set('status', filter.status);
    if (filter.type)   params = params.set('type', filter.type);
    if (filter.from)   params = params.set('from', filter.from);
    if (filter.to)     params = params.set('to', filter.to);
    params = params.set('page', filter.page ?? 0);
    params = params.set('size', filter.size ?? 20);
    if (filter.sort)   params = params.set('sort', filter.sort);
    return this.http.get<PagedResponse<Incident>>(`${this.base}/incidents`, { params });
  }

  getIncident(id: number): Observable<Incident> {
    return this.http.get<Incident>(`${this.base}/incidents/${id}`);
  }

  getIncidentByToken(token: string): Observable<Incident> {
    return this.http.get<Incident>(`${this.base}/incidents/token/${token}`);
  }

  clearIncidentByToken(token: string): Observable<Incident> {
    return this.http.post<Incident>(`${this.base}/incidents/token/${token}/clear`, null);
  }

  updateIncidentStatus(id: number, status: string): Observable<Incident> {
    return this.http.patch<Incident>(`${this.base}/incidents/${id}/status`, null, {
      params: { status },
    });
  }

  // --- Admin: Devices ---
  getAdminDevices(page = 0, size = 20): Observable<PagedResponse<Device>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<PagedResponse<Device>>(`${this.base}/admin/devices`, { params });
  }

  createDevice(req: DeviceCreateRequest): Observable<Device> {
    return this.http.post<Device>(`${this.base}/admin/devices`, req);
  }

  updateDevice(id: number, req: DeviceCreateRequest): Observable<Device> {
    return this.http.put<Device>(`${this.base}/admin/devices/${id}`, req);
  }

  deleteDevice(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/admin/devices/${id}`);
  }

  // --- Health Monitoring ---
  getHealthSummary(): Observable<HealthSummary> {
    return this.http.get<HealthSummary>(`${this.base}/health/summary`);
  }

  // --- Other Cameras ---
  getOtherCameras(incidentId: number): Observable<OtherCamera[]> {
    return this.http.get<OtherCamera[]>(`${this.base}/incidents/${incidentId}/other-cameras`);
  }

  // --- Simulation ---
  simulateAlert(type: IncidentType): Observable<Incident> {
    return this.http.post<Incident>(`${this.base}/simulate/alert`, { type });
  }

  simulateHealthState(state: 'all-good' | 'alert' | 'incident'): Observable<HealthSummary> {
    return this.http.post<HealthSummary>(`${this.base}/simulate/health-state`, { state });
  }

  resetSimulation(): Observable<void> {
    return this.http.post<void>(`${this.base}/simulate/reset`, {});
  }
}
