package com.github.invizible.catdogtion.event;

import com.github.invizible.catdogtion.domain.Auction;
import lombok.Getter;

public class StartedAuctionEvent {

  @Getter
  private Auction auction;

  public StartedAuctionEvent(Auction auction) {
    this.auction = auction;
  }
}
