import { Incident, IncidentType } from '../models/incident.model';

const TYPE_LABEL: Record<IncidentType, string> = {
  FIRE_SMOKE:           'Fire & Smoke Detection',
  UNAUTHORIZED_ACCESS:  'Unauthorized Area Access',
  ATM_LOITERING:        'ATM Loitering Detection',
  AFTER_HOURS:          'After Hours Anomaly Detection',
};

const TYPE_ACTIVITY: Record<IncidentType, string> = {
  FIRE_SMOKE:           'Smoke or fire appears in the building.',
  UNAUTHORIZED_ACCESS:  'A customer has entered the Teller area.',
  ATM_LOITERING:        'Someone is at the ATM for an extended period.',
  AFTER_HOURS:          'Detecting activity at a closed branch.',
};

function formatWib(iso: string): string {
  const d = new Date(iso);
  return d.toLocaleTimeString('id-ID', {
    hour: '2-digit', minute: '2-digit', second: '2-digit',
    timeZone: 'Asia/Jakarta', hour12: false,
  }) + ' WIB';
}

function formatDuration(detectedAt: string, clearedAt: string | null): string {
  if (!clearedAt) return '—';
  const ms = new Date(clearedAt).getTime() - new Date(detectedAt).getTime();
  const totalSec = Math.floor(ms / 1000);
  const m = Math.floor(totalSec / 60);
  const s = totalSec % 60;
  return m > 0 ? `${m}m ${s}s` : `${s}s`;
}

function formatMonth(iso: string): string {
  return new Date(iso).toLocaleDateString('en-US', {
    month: 'long', year: 'numeric', timeZone: 'Asia/Jakarta',
  });
}

export interface IncidentView {
  id: number;
  region: string;
  areaGroup: string;
  branch: string;
  area: string;
  indication: string;
  activity: string;
  timeDetected: string;
  timeCleared: string;
  duration: string;
  month: string;
  type: IncidentType;
  detectedAtRaw: string;
  isCleared: boolean;
}

export function toIncidentView(i: Incident): IncidentView {
  return {
    id: i.id,
    region: i.region,
    areaGroup: i.areaGroup,
    branch: i.branchName,
    area: i.deviceLocation,
    indication: TYPE_LABEL[i.type],
    activity: TYPE_ACTIVITY[i.type],
    timeDetected: formatWib(i.detectedAt),
    timeCleared: i.clearedAt ? formatWib(i.clearedAt) : '—',
    duration: formatDuration(i.detectedAt, i.clearedAt),
    month: formatMonth(i.detectedAt),
    type: i.type,
    detectedAtRaw: i.detectedAt,
    isCleared: i.clearedAt !== null,
  };
}

export function generateMonths(): string[] {
  const months: string[] = [];
  const now = new Date();
  for (let offset = 11; offset >= 0; offset--) {
    const d = new Date(now.getFullYear(), now.getMonth() - offset, 1);
    months.push(d.toLocaleDateString('en-US', { month: 'long', year: 'numeric' }));
  }
  return months;
}
