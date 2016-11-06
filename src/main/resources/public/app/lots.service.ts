import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';
import { Lot } from './lot';
import { Image } from './image';

@Injectable()
export class LotsService {

  constructor(
    private http: Http
  ) { }

  getAllLots(): Observable<Lot[]> {
    return this.http.get('/api/lots')
      .map(resp => resp.json()._embedded.lots as Lot[]);
  }

  getLotImages(lotId: number): Observable<Image[]> {
    return this.http.get(`/api/lots/${lotId}/images`)
      .map(resp => resp.json()._embedded.images
        .map(image => new Image(`data:${image.contentType};base64,${image.image}`)));
  }
}
