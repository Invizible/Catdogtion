package com.github.invizible.catdogtion.event;

import com.github.invizible.catdogtion.domain.Auction;

public class StartedAuctionEvent extends AbstractAuctionEvent {
  public StartedAuctionEvent(Auction auction) {
    super(auction);
  }
}
