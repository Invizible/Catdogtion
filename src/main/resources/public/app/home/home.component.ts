import { Component, OnInit } from '@angular/core';
import { Lot } from '../lot';
import { LotService } from '../lot.service';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  lots: Lot[];
  forCurrentUser: boolean = false;

  constructor(
    private lotsService: LotService,
    private route: ActivatedRoute
  ) {
    let data = route.snapshot.data[0];
    this.forCurrentUser = data ? data.forCurrentUser : false;
  }

  ngOnInit() {
    let lots: Observable<Lot[]> = this.forCurrentUser ? this.lotsService.getAllLotsForCurrentUser() : this.lotsService.getAllLots();
    lots.subscribe(
      lots => this.lots = lots
    );
  }

}
