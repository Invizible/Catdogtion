import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';
import * as Stomp from 'stompjs';

@Injectable()
export class StompService {
  private stompClient;

  constructor() {
    this.stompClient = Stomp.over(new WebSocket(this.url('ws')));
  }

  private url(s: string): string {
    let l = window.location;
    return ((l.protocol === "https:") ? "wss://" : "ws://") + l.host + l.pathname + s;
  }

  public connect(endpointUrl: string): Observable<any> {
    let stompSubject : Subject<any> = new Subject<any>();

    this.stompClient.connect({}, () => {
      this.stompClient.subscribe(endpointUrl, (stompResponse) => {
        // stompResponse = {command, headers, body with JSON
        // reflecting the object returned by Spring framework}
        stompSubject.next(JSON.parse(stompResponse.body));
      });
    });

    return stompSubject.asObservable();
  }

  public send(url: string, payload: string) {
    this.stompClient.send(url, {}, JSON.stringify({'inputField': payload}));
  }
}
