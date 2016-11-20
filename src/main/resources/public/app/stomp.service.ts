import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

import * as Stomp from 'stompjs';

@Injectable()
export class StompService {
  private stompClient;
  private stompSubject : Subject<any> = new Subject<any>();

  constructor() {
    this.stompClient = Stomp.over(new WebSocket('ws://localhost:9991/ws'));
  }

  public connect(endpointUrl: string): void {
    this.stompClient.connect({}, () => {
      this.stompClient.subscribe(endpointUrl, (stompResponse) => {
        // stompResponse = {command, headers, body with JSON
        // reflecting the object returned by Spring framework}
        this.stompSubject.next(JSON.parse(stompResponse.body));
      });
    });
  }

  public send(url: string, payload: string) {
    this.stompClient.send(url, {}, JSON.stringify({'inputField': payload}));
  }

  public getObservable() : Observable<any> {
    return this.stompSubject.asObservable();
  }
}
