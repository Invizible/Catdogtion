import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Auction } from './auction';
import { StompService } from './stomp.service';
import { Log } from './log';
import { Bet } from './bet';
import { User } from './user';

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

  getStartedAuctions(): Observable<Auction> {
    return this.stompService.connect('/user/queue/startedAuction');
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

  getWinner(auctionId: number): Observable<Auction> {
    return this.stompService.connect(`/topic/auction/${auctionId}/auctionWinner`);
  }
}
