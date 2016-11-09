import {Component, OnInit, ViewChild, NgZone} from '@angular/core';
import { ModalDirective } from 'ng2-bootstrap';
import { Lot } from '../lot';
import { CookieService } from "angular2-cookie/services/cookies.service";
import { ImageService } from "../image.service";

@Component({
  selector: 'lot-modal',
  templateUrl: './lot-modal.component.html',
  styleUrls: ['./lot-modal.component.css'],
  exportAs: 'lotModal'
})
export class LotModalComponent implements OnInit {

  @ViewChild('lotModal')
  lotModal: ModalDirective;

  private zone: NgZone;
  private options: Object;
  private progress: number = 0;
  private responses: any[] = [];

  lot: Lot = new Lot();

  constructor(
    private cookieService: CookieService,
    private imageService: ImageService
  ) { }

  ngOnInit() {
    var xsrfToken = this.cookieService.get('XSRF-TOKEN');

    this.zone = new NgZone({ enableLongStackTrace: false });
    this.options = {
      url: '/api/uploadImage',
      customHeaders: {
        'X-XSRF-TOKEN': xsrfToken
      }
    };
  }

  showModal(): void {
    this.lotModal.show();
  }

  handleMultipleUpload(data: any): void {
    let index = this.responses.findIndex(x => x.id === data.id);
    if (index === -1) {
      this.responses.push(data);
    } else {
      let total = 0;
      let uploaded = 0;
      this.responses.forEach(resp => {
        total += resp.progress.total;
        uploaded += resp.progress.loaded;
        resp.image = this.imageService.convertToBase64Image(JSON.parse(resp.response));
      });
      let percent = Math.floor(uploaded / total * 100);

      this.zone.run(() => {
        this.responses[index] = data;
        this.progress = percent;
      });
    }
  }

  save(): void {

  }
}
