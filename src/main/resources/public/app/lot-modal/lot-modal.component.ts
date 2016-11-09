import {Component, OnInit, ViewChild, NgZone} from '@angular/core';
import { ModalDirective } from 'ng2-bootstrap';
import { Lot } from '../lot';
import { CookieService } from "angular2-cookie/services/cookies.service";

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
  private response: any[] = [];

  lot: Lot = new Lot();

  constructor(
    private cookieService: CookieService
  ) { }

  ngOnInit() {
    var xsrfToken = this.cookieService.get('XSRF-TOKEN');

    this.zone = new NgZone({ enableLongStackTrace: false });
    this.options = {
      url: '/api/images',
      customHeaders: {
        'X-XSRF-TOKEN': xsrfToken
      }
    };
  }

  showModal(): void {
    this.lotModal.show();
  }

  handleMultipleUpload(data: any): void {
    let index = this.response.findIndex(x => x.id === data.id);
    if (index === -1) {
      this.response[1].push(data);
    } else {
      let total = 0, uploaded = 0;
      this.response.forEach(resp => {
        total += resp.progress.total;
        uploaded += resp.progress.loaded;
      });
      let percent = uploaded / (total / 100) / 100;

      this.zone.run(() => {
        this.response[index] = data;
        this.progress = percent;
      });
    }
  }

  save(): void {

  }
}
