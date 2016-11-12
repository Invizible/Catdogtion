import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import { Location } from '@angular/common';
import { LotService } from '../lot.service';
import { Lot } from '../lot';
import {User} from "../user";
import {AccountService} from "../account.service";
import { Auction } from '../auction';
import { AuctionService } from '../auction.service';

@Component({
  selector: 'lot-details',
  templateUrl: './lot-details.component.html',
  styleUrls: ['./lot-details.component.css']
})
export class LotDetailsComponent implements OnInit {
  lot: Lot = new Lot();
  authenticatedUser: User = new User();
  auction: Auction = new Auction;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private lotService: LotService,
    private accountService: AccountService,
    private auctionService: AuctionService
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

      this.lotService.getAuctionByLotId(id).subscribe(auction => this.auction = auction);
    });

    this.accountService.getAuthenticatedUser().subscribe(
      user => this.authenticatedUser = user
    );
  }

  goBack(): void {
    this.location.back();
  }

  removeLot(): void {
    this.lotService.deleteLot(this.lot.id).subscribe(
      resp => this.router.navigate(['/my-lots'])
    );
  }

  participate(): void {
    this.auctionService.addParticipantForAuction(this.auction.id).subscribe(
      auction => this.auction = auction
    );
  }

}
