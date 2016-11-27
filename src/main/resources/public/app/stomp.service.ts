import { Injectable } from '@angular/core';
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

  send(url: string, payload: string) {
    this.stompClient.send(url, {}, JSON.stringify({'inputField': payload}));
  }

  subscribe(destination: string, callback) {
    let subscribeCallback = () => this.stompClient.subscribe(destination, (stompResponse) => {
      // stompResponse = {command, headers, body with JSON
      // reflecting the object returned by Spring framework}
      callback(JSON.parse(stompResponse.body));
    });

    if (this.stompClient.connected) {
      return subscribeCallback();
    }

    return this.stompClient.connect({}, subscribeCallback);
  }
}
