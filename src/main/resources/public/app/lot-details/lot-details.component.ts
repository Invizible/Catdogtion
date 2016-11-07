import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Location } from '@angular/common';
import { LotsService } from '../lots.service';
import { Lot } from '../lot';

@Component({
  selector: 'lot-details',
  templateUrl: './lot-details.component.html',
  styleUrls: ['./lot-details.component.css']
})
export class LotDetailsComponent implements OnInit {
  lot: Lot = new Lot();

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private lotsService: LotsService
  ) { }

  ngOnInit() {
    this.route.params.forEach((params: Params) => {
      let id = +params['id'];
      this.lotsService.getLot(id).subscribe(
        lot => {
          this.lot = lot;
          this.lotsService.getLotImages(id).subscribe(images => this.lot.images = images)
        }
      );
    });
  }

  goBack(): void {
    this.location.back();
  }

}
