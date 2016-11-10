import { Component, OnInit, ViewChild, NgZone, Input } from '@angular/core';
import { ModalDirective } from 'ng2-bootstrap';
import { Lot } from '../lot';
import { CookieService } from 'angular2-cookie/services/cookies.service';
import { ImageService } from '../image.service';
import { LotService } from '../lot.service';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

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

  mouseEnter: boolean = false;
  previewDescription: boolean = false;

  @Input()
  lot: Lot = new Lot();

  constructor(private cookieService: CookieService,
              private imageService: ImageService,
              private lotService: LotService,
              private router: Router) {
  }

  ngOnInit() {
    var xsrfToken = this.cookieService.get('XSRF-TOKEN');

    this.zone = new NgZone({enableLongStackTrace: false});
    this.options = {
      url: '/api/uploadImage',
      customHeaders: {
        'X-XSRF-TOKEN': xsrfToken
      }
    };
  }

  showModal(): void {
    //TODO: we need ids
    this.lot.images.forEach(
      image => this.responses.push({image: image.image})
    );
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
        if (resp.response) {
          resp.image = this.imageService.convertToBase64Image(JSON.parse(resp.response));
        }
      });
      let percent = Math.floor(uploaded / total * 100);

      this.zone.run(() => {
        this.responses[index] = data;
        this.progress = percent;
      });
    }
  }

  save(): void {
    this.lot.id ? this.updateLot() : this.saveLot();
  }

  private saveLot(): void {
    this.lot.images = this.responses.map(
      response => ({id: JSON.parse(response.response).id})
    );
    this.lotService.saveLot(this.lot)
      .subscribe(resp => this.router.navigate(['/home']));
  }

  private updateLot() {
    this.lotService.updateLot(this.lot)
      .subscribe(resp => resp);
  }

  removeImage(response: any): void {
    let image = JSON.parse(response.response);
    this.imageService.deleteImage(image.id).subscribe(
      () => this.responses = this.responses.filter(resp => resp.id != response.id)
    );
  }
}
