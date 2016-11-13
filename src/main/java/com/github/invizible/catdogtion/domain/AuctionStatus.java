package com.github.invizible.catdogtion.domain;

public enum AuctionStatus {
  /**
   * Created and not started
   */
  CREATED,

  /**
   * Currently is happening
   */
  IN_PROGRESS,

  /**
   * Finished or was closed by {@link com.github.invizible.catdogtion.service.AuctionScheduler#scheduleAuctionStartDateCheck(Auction)}
   */
  CLOSED
}
