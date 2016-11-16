import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

import * as Stomp from 'stompjs';

@Injectable()
export class StompService {
  private stompClient;
  private stompSubject : Subject<any> = new Subject<any>();

  public connect(webSocketUrl: string, endpointUrl: string) : void {
    let self = this;
    let webSocket = new WebSocket(webSocketUrl);
    this.stompClient = Stomp.over(webSocket);
    this.stompClient.connect({}, () => {
      self.stompClient.subscribe(endpointUrl, function (stompResponse) {
        // stompResponse = {command, headers, body with JSON
        // reflecting the object returned by Spring framework}
        self.stompSubject.next(JSON.parse(stompResponse.body));
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
