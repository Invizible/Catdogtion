import { Component, OnInit, Input } from '@angular/core';
import { Log } from '../log';
import { Auction } from '../auction';
import { AuctionService } from '../auction.service';
import { Observable } from 'rxjs';
import { Bet } from '../bet';
import * as _random from 'lodash/random';
import { User } from '../user';

@Component({
  selector: 'auction-controls',
  templateUrl: 'auction-controls.component.html'
})
export class AuctionControlsComponent implements OnInit {
  logs: Observable<Log[]>;
  bet: Bet = new Bet();
  winner: User;
  randomWinnerGifPath: string;

  @Input()
  auction: Auction;

  constructor(
    private auctionService: AuctionService
  ) { }

  ngOnInit() {
    this.logs = this.auctionService.getLogsForAuction(this.auction.id);
    this.winner = this.auction.winner;
    this.randomWinnerGifPath = this.getRandomWinnerGifPath();
    this.auctionService.getWinner(this.auction.id).subscribe(auction => this.winner = auction.winner);
  }

  auctionHasStarted(): boolean {
    return this.auction.status == 'IN_PROGRESS';
  }

  isAuctionCreated(): boolean {
    return this.auction.status == 'CREATED';
  }

  makeBet(): void {
    this.auctionService.makeABet(this.auction.id, this.bet).subscribe(
      () => this.auction.highestPrice = this.bet.bet
    );
  }

  private getRandomWinnerGifPath(): string {
    return `../../assets/winner-${_random(1, 6)}.gif`;
  }
}
