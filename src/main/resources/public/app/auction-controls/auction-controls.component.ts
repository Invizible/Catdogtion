import { Component, OnInit, Input } from '@angular/core';
import { Log } from '../log';
import { Auction } from '../auction';

@Component({
  selector: 'auction-controls',
  templateUrl: 'auction-controls.component.html'
})
export class AuctionControlsComponent implements OnInit {
  logs: Log[] = [];

  @Input()
  auction: Auction;

  constructor() { }

  ngOnInit() {
  }

}
