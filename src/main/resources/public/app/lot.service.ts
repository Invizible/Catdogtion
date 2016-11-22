import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';
import { Lot } from './lot';
import { Image } from './image';
import { User } from './user';
import { ImageService } from "./image.service";
import { Auction } from './auction';

@Injectable()
export class LotService {
  private lotsUrl: string = '/api/lots';

  constructor(
    private http: Http,
    private imageService: ImageService
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
        .map(image => {
          let convertedImage = new Image(this.imageService.convertToBase64Image(image));
          convertedImage.id = image.id;
          return convertedImage;
        }));
  }

  getLot(id: number): Observable<Lot> {
    return this.http.get(`${this.lotsUrl}/${id}?projection=withAuctioneerAndCharacteristics`)
      .map(this.transformResponseToLot());
  }

  getLotAuctioneer(id: number): Observable<User> {
    return this.http.get(`${this.lotsUrl}/${id}/auctioneer`)
      .map(resp => resp.json() as User);
  }

  saveLot(lot: Lot): Observable<Lot> {
    return this.http.post(`${this.lotsUrl}`, lot)
      .map(this.transformResponseToLot());
  }

  private transformResponseToLot() {
    return resp => resp.json() as Lot;
  }

  updateLot(lot: Lot): Observable<any> {
    return this.http.put(`${this.lotsUrl}`, lot);
  }

  deleteLot(id: number): Observable<any> {
    return this.http.delete(`${this.lotsUrl}/${id}`);
  }

  getAuctionByLotId(id: number): Observable<Auction> {
    return this.http.get(`${this.lotsUrl}/${id}/auction?projection=withParticipantsAndWinner`)
      .map(resp => resp.json() as Auction);
  }
}
