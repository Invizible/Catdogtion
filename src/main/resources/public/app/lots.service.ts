import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';
import { Lot } from './lot';
import { Image } from './image';
import { User } from './user';

@Injectable()
export class LotsService {
  private lotsUrl: string = '/api/lots';

  constructor(
    private http: Http
  ) { }

  getAllLots(): Observable<Lot[]> {
    return this.http.get(this.lotsUrl)
      .map(resp => resp.json()._embedded.lots as Lot[]);
  }

  getAllLotsForCurrentUser(): Observable<Lot[]> {
    return this.http.get(`${this.lotsUrl}/search/findAllForCurrentUser`)
      .map(resp => resp.json()._embedded.lots as Lot[]);
  }

  getLotImages(lotId: number): Observable<Image[]> {
    return this.http.get(`${this.lotsUrl}/${lotId}/images`)
      .map(resp => resp.json()._embedded.images
        .map(image => new Image(`data:${image.contentType};base64,${image.image}`)));
  }

  getLot(id: number): Observable<Lot> {
    return this.http.get(`${this.lotsUrl}/${id}?projection=withAuctioneerAndCharacteristics`)
      .map(resp => resp.json() as Lot);
  }

  getLotAuctioneer(id: number): Observable<User> {
    return this.http.get(`${this.lotsUrl}/${id}/auctioneer`)
      .map(resp => resp.json() as User);
  }
}
