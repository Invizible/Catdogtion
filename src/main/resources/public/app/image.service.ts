import { Injectable } from '@angular/core';

@Injectable()
export class ImageService {

  constructor() { }

  convertToBase64Image(image: any): string {
    return `data:${image.contentType};base64,${image.image}`;
  }
}
