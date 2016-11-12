import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';

@Injectable()
export class ImageService {
  private url: string = '/api/images';

  constructor(
    private http: Http
  ) { }

  convertToBase64Image(image: any): string {
    return `data:${image.contentType};base64,${image.image}`;
  }
}
