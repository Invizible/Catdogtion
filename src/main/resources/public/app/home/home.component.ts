import { Component, OnInit } from '@angular/core';
import { Lot } from '../lot';
import { LotsService } from '../lots.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  lots: Lot[];

  constructor(
    private lotsService: LotsService
  ) { }

  ngOnInit() {
    this.lotsService.getAllLots().subscribe(
      lots => this.lots = lots
    );
  }

}
