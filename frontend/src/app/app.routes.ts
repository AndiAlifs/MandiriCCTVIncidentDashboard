import { Routes } from '@angular/router';
import { authGuard, adminGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'incident-monitoring', pathMatch: 'full' },

  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent),
  },
  {
    path: 'incident/clear/:token',
    loadComponent: () => import('./pages/incident-clear/incident-clear.component').then(m => m.IncidentClearComponent),
  },
  {
    path: 'incident-monitoring',
    loadComponent: () => import('./pages/incident-monitoring/incident-monitoring.component').then(m => m.IncidentMonitoringComponent),
    canActivate: [authGuard],
  },
  {
    path: 'incident-detail/:id',
    loadComponent: () => import('./pages/incident-detail/incident-detail.component').then(m => m.IncidentDetailComponent),
    canActivate: [authGuard],
  },
  {
    path: 'cctv-health-monitoring',
    loadComponent: () => import('./pages/cctv-health-monitoring/cctv-health-monitoring.component').then(m => m.CctvHealthMonitoringComponent),
    canActivate: [authGuard],
  },
  {
    path: 'cctv-availability',
    loadComponent: () => import('./pages/cctv-availability/cctv-availability.component').then(m => m.CctvAvailabilityComponent),
    canActivate: [authGuard],
  },
  {
    path: 'admin/devices',
    loadComponent: () => import('./pages/admin-devices/admin-devices.component').then(m => m.AdminDevicesComponent),
    canActivate: [adminGuard],
  },
  { path: '**', redirectTo: 'incident-monitoring' },
];
