import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import { Location } from '@angular/common';
import { LotService } from '../lot.service';
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
    private router: Router,
    private location: Location,
    private lotService: LotService
  ) { }

  ngOnInit() {
    this.route.params.forEach((params: Params) => {
      let id = +params['id'];
      this.lotService.getLot(id).subscribe(
        lot => {
          this.lot = lot;
          this.lotService.getLotImages(id).subscribe(images => this.lot.images = images)
        }
      );
    });
  }

  goBack(): void {
    this.location.back();
  }

  removeLot(lot: Lot): void {
    this.lotService.delete(lot.id).subscribe(
      resp => this.router.navigate(['/my-lots'])
    );
  }

}
