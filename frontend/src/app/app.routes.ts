import { Routes } from '@angular/router';
import { IncidentMonitoringComponent } from './pages/incident-monitoring/incident-monitoring.component';
import { IncidentDetailComponent } from './pages/incident-detail/incident-detail.component';

export const routes: Routes = [
  { path: '', redirectTo: 'incident-monitoring', pathMatch: 'full' },
  { path: 'incident-monitoring', component: IncidentMonitoringComponent },
  { path: 'incident-detail/:id', component: IncidentDetailComponent },
];
