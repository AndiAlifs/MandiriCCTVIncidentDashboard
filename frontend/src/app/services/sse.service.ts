import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class SseService {

  private base = environment.apiUrl;

  constructor(private auth: AuthService) {}

  incidents$(): Observable<MessageEvent> {
    return new Observable(observer => {
      const token = this.auth.getToken();
      const es = new EventSource(`${this.base}/events?token=${token}`);

      es.addEventListener('INCIDENT_CREATED', (e: MessageEvent) => observer.next(e));
      es.addEventListener('INCIDENT_CLEARED', (e: MessageEvent) => observer.next(e));

      es.onerror = () => {
        // Browser will auto-reconnect; only complete on explicit teardown
      };

      return () => es.close();
    });
  }
}
