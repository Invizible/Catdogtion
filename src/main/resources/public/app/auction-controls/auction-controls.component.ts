import { Component, OnInit, Input } from '@angular/core';
import { Log } from '../log';
import { Auction } from '../auction';
import { AuctionService } from '../auction.service';
import { Observable } from 'rxjs';
import { Bet } from '../bet';

@Component({
  selector: 'auction-controls',
  templateUrl: 'auction-controls.component.html'
})
export class AuctionControlsComponent implements OnInit {
  logs: Observable<Log[]>;
  bet: Bet = new Bet();

  @Input()
  auction: Auction;

  constructor(
    private auctionService: AuctionService
  ) { }

  ngOnInit() {
    this.logs = this.auctionService.getLogsForAuction(this.auction.id);
  }

  auctionHasStarted(): boolean {
    return this.auction.status == 'IN_PROGRESS';
  }

  isAuctionCreated(): boolean {
    return this.auction.status == 'CREATED';
  }

  makeABet(): void {
    this.auctionService.makeABet(this.auction.id, this.bet).subscribe(
      () => this.auction.highestPrice = this.bet.bet
    );
  }
}
