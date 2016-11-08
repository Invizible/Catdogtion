import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalDirective } from 'ng2-bootstrap';
import { Lot } from '../lot';
import { FileUploader } from "ng2-file-upload";

@Component({
  selector: 'lot-modal',
  templateUrl: './lot-modal.component.html',
  styleUrls: ['./lot-modal.component.css'],
  exportAs: 'lotModal'
})
export class LotModalComponent implements OnInit {

  @ViewChild('lotModal')
  lotModal: ModalDirective;

  uploader: FileUploader = new FileUploader({url: 'https://evening-anchorage-3159.herokuapp.com/api/'});
  hasDropZoneOver:boolean = false;

  lot: Lot = new Lot();

  constructor() { }

  ngOnInit() {
  }

  showModal(): void {
    this.lotModal.show();
  }

  fileOverBase(e:any):void {
    this.hasDropZoneOver = e;
  }

  save(): void {

  }
}
