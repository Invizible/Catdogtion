import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Auction } from './auction';
import { StompService } from './stomp.service';
import { Log } from './log';
import { Bet } from './bet';

@Injectable()
export class AuctionService {
  private url: string = '/api/auctions';

  constructor(
    private http: Http,
    private stompService: StompService
  ) { }

  addParticipantForAuction(id: number): Observable<Auction> {
    return this.http.post(`${this.url}/${id}/participants`, null)
      .map(this.transformResponseToAuction());
  }

  getStartedAuctions(callback): void {
    this.stompService.subscribe('/user/queue/startedAuction', callback);
  }

  private transformResponseToAuction() {
    return resp => resp.json() as Auction;
  }

  getLogsForAuction(id: number): Observable<Log[]> {
    return Observable.timer(0, 3000)
      .flatMapTo(this.http.get(`${this.url}/${id}/logs?sort=time,desc`))
      .map(resp => resp.json()._embedded.logs as Log[]);
  }

  makeABet(auctionId: number, bet: Bet): Observable<any> {
    return this.http.post(`${this.url}/${auctionId}/bets`, bet);
  }

  getWinner(auctionId: number, callback) {
    return this.stompService.subscribe(`/topic/auction/${auctionId}/auctionWinner`, callback);
  }
}
