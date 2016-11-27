import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { Log } from '../log';
import { Auction } from '../auction';
import { AuctionService } from '../auction.service';
import { Observable } from 'rxjs';
import { Bet } from '../bet';
import * as _random from 'lodash/random';

@Component({
  selector: 'auction-controls',
  templateUrl: 'auction-controls.component.html'
})
export class AuctionControlsComponent implements OnInit, OnDestroy {
  logs: Observable<Log[]>;
  bet: Bet = new Bet();
  randomWinnerGifPath: string;
  betTimeout: number = 60; //TODO: remove hardcode
  showTimer: boolean = true;
  private winnerSubscription;

  @Input()
  auction: Auction;

  constructor(
    private auctionService: AuctionService
  ) { }

  ngOnInit() {
    this.logs = this.auctionService.getLogsForAuction(this.auction.id);
    this.randomWinnerGifPath = this.getRandomWinnerGifPath();
    this.winnerSubscription = this.auctionService.getWinner(this.auction.id, auction => this.auction = auction);
  }

  ngOnDestroy() {
    this.winnerSubscription.unsubscribe();
  }

  auctionHasStarted(): boolean {
    return this.auction.status == 'IN_PROGRESS';
  }

  isAuctionCreated(): boolean {
    return this.auction.status == 'CREATED';
  }

  makeBet(): void {
    this.showTimer = false;
    this.auctionService.makeABet(this.auction.id, this.bet).subscribe(
      () => {
        this.auction.highestPrice = this.bet.bet;
        this.showTimer = true;
      }
    );
  }

  private getRandomWinnerGifPath(): string {
    return `../../assets/winner-${_random(1, 6)}.gif`;
  }
}
