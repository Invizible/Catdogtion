package com.github.invizible.catdogtion.event;

import com.github.invizible.catdogtion.domain.Auction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
abstract class AbstractAuctionEvent
{
  private Auction auction;
}
