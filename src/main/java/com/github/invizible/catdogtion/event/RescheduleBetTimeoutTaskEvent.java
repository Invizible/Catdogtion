package com.github.invizible.catdogtion.event;

import com.github.invizible.catdogtion.domain.Auction;

public class RescheduleBetTimeoutTaskEvent extends AbstractAuctionEvent {
  public RescheduleBetTimeoutTaskEvent(Auction auction) {
    super(auction);
  }
}
