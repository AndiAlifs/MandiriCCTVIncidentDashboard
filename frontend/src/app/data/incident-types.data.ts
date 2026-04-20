import { IncidentType } from '../models/incident.model';

export interface IncidentTypeUi {
  icon: 'fire' | 'unauthorized' | 'atm' | 'afterhours';
  name: string;
  videoSrc: string | null;
  type: IncidentType;
}

export const INCIDENT_TYPE_UI: IncidentTypeUi[] = [
  { icon: 'fire',         name: 'Fire & Smoke Detection',        videoSrc: '/asset/asap_1.mp4',            type: 'FIRE_SMOKE'          },
  { icon: 'unauthorized', name: 'Unauthorized Area Access',      videoSrc: '/asset/unatorized_access.mp4', type: 'UNAUTHORIZED_ACCESS' },
  { icon: 'atm',          name: 'ATM Loitering Detection',       videoSrc: '/asset/asap_2.mp4',            type: 'ATM_LOITERING'       },
  { icon: 'afterhours',   name: 'After Hours Anomaly Detection', videoSrc: null,                           type: 'AFTER_HOURS'         },
];

export function typeUiFor(type: IncidentType): IncidentTypeUi {
  return INCIDENT_TYPE_UI.find(t => t.type === type) ?? INCIDENT_TYPE_UI[0];
}
