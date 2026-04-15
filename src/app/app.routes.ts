import { Routes } from '@angular/router';
import { IncidentMonitoringComponent } from './pages/incident-monitoring/incident-monitoring.component';

export const routes: Routes = [
  { path: '', redirectTo: 'incident-monitoring', pathMatch: 'full' },
  { path: 'incident-monitoring', component: IncidentMonitoringComponent },
];
