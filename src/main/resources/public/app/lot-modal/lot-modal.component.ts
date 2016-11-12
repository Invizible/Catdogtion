import { Component, OnInit, ViewChild, NgZone, Input, Output, EventEmitter } from '@angular/core';
import { ModalDirective } from 'ng2-bootstrap';
import { Lot } from '../lot';
import { CookieService } from 'angular2-cookie/services/cookies.service';
import { ImageService } from '../image.service';
import { LotService } from '../lot.service';
import { Router } from '@angular/router';
import { Image } from '../image';

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

  @Output()
  addLot: EventEmitter<Lot> = new EventEmitter<Lot>();

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
    this.responses = []; //clear previous images

    this.lot.images.forEach(
      image => this.responses.push({image: image})
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
        if (this.isImageUploaded(resp)) {
          return;
        }

        total += resp.progress.total;
        uploaded += resp.progress.loaded;
        if (resp.response) {
          let image = JSON.parse(resp.response);
          resp.image = new Image(this.imageService.convertToBase64Image(image), image.id);
          this.lot.images.push(resp.image);
        }
      });
      let percent = Math.floor(uploaded / total * 100);

      this.zone.run(() => {
        this.responses[index] = data;
        this.progress = percent;
      });
    }
  }

  private isImageUploaded(resp): boolean {
    return resp.image;
  }

  save(): void {
    this.lot.id ? this.updateLot() : this.saveLot();
  }

  private saveLot(): void {
    this.lot.images = this.responses.map(
      response => ({id: JSON.parse(response.response).id})
    );
    this.lotService.saveLot(this.lot)
      .subscribe(lot => {
        this.addLot.emit(lot);
        this.lot = new Lot();
        this.responses = [];
        this.lotModal.hide()
      });
  }

  private updateLot() {
    let lot: Lot = Object.assign({}, this.lot); //make a clone, because we will spoil it
    lot.images = this.responses.map(response => ({id: response.image.id}));
    this.lotService.updateLot(lot)
      .subscribe(resp => this.lotModal.hide());
  }

  removeImage(response: any): void {
    this.responses = this.responses.filter(resp => resp.image.id != response.image.id);
    this.lot.images = this.lot.images.filter(img => img.id != response.image.id);
  }
}
