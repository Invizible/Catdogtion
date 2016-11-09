import { Component, OnInit, Input } from '@angular/core';
import { Lot } from '../lot';
import { LotService } from '../lot.service';

@Component({
  selector: 'lot',
  templateUrl: './lot.component.html'
})
export class LotComponent implements OnInit {
  @Input()
  lot: Lot;

  constructor(
    private lotsService: LotService
  ) { }

  ngOnInit() {
    this.lotsService.getLotImages(this.lot.id).subscribe(
      images => this.lot.images = images
    );
  }

}
