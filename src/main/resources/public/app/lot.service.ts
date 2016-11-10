import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';
import { Lot } from './lot';
import { Image } from './image';
import { User } from './user';
import { ImageService } from "./image.service";

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
      .map(resp => resp.json() as Lot);
  }

  getLotAuctioneer(id: number): Observable<User> {
    return this.http.get(`${this.lotsUrl}/${id}/auctioneer`)
      .map(resp => resp.json() as User);
  }

  saveLot(lot: Lot): Observable<any> {
    return this.http.post(`${this.lotsUrl}`, lot);
  }

  updateLot(lot: Lot): Observable<any> {
    return this.http.put(`${this.lotsUrl}/${lot.id}`, lot);
  }

  deleteLot(id: number): Observable<any> {
    return this.http.delete(`${this.lotsUrl}/${id}`);
  }
}
