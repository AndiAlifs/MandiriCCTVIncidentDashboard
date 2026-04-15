import { Routes } from '@angular/router';
import { IncidentAlertComponent } from './pages/incident-alert/incident-alert.component';

export const routes: Routes = [
  { path: '', redirectTo: 'incident-alert', pathMatch: 'full' },
  { path: 'incident-alert', component: IncidentAlertComponent },
];
