import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';
import { Auction } from './auction';

@Injectable()
export class AuctionService {
  private url: string = '/api/auctions';

  constructor(
    private http: Http
  ) { }

  addParticipantForAuction(id: number): Observable<Auction> {
    return this.http.post(`${this.url}/${id}/participants`, null)
      .map(this.transformResponseToAuction());
  }

  getStartedAuctions(): Observable<Auction> {
    return this.http.get(`${this.url}/search/findStartedAuctions`)
      .map(this.transformResponseToAuction());
  }

  private transformResponseToAuction() {
    return resp => resp.json() as Auction;
  }
}
