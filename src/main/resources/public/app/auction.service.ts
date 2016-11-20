import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';
import { Auction } from './auction';
import { StompService } from './stomp.service';

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
    return this.stompService.connect('/topic/startedAuction');
  }

  private transformResponseToAuction() {
    return resp => resp.json() as Auction;
  }
}
