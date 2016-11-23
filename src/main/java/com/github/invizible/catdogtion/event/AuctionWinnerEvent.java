package com.github.invizible.catdogtion.event;

import com.github.invizible.catdogtion.domain.Auction;

public class AuctionWinnerEvent extends AbstractAuctionEvent {
  public AuctionWinnerEvent(Auction auction) {
    super(auction);
  }
}
