import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalDirective } from 'ng2-bootstrap';
import { Lot } from '../lot';

@Component({
  selector: 'lot-modal',
  templateUrl: './lot-modal.component.html',
  styleUrls: ['./lot-modal.component.css'],
  exportAs: 'lotModal'
})
export class LotModalComponent implements OnInit {

  @ViewChild('lotModal')
  lotModal: ModalDirective;

  lot: Lot = new Lot();

  constructor() { }

  ngOnInit() {
  }

  showModal(): void {
    this.lotModal.show();
  }

  save(): void {

  }
}
