package com.github.invizible.catdogtion.service;

import com.github.invizible.catdogtion.domain.Auction;
import com.github.invizible.catdogtion.repository.AuctionRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
@CommonsLog
public class AuctionScheduler {

  @Autowired
  private TaskScheduler taskScheduler;

  @Autowired
  private AuctionRepository auctionRepository;

  @Autowired
  private AuctionService auctionService;

  @PostConstruct
  public void closeExpiredAuctions() {
    log.info("Closing expired auctions");

    auctionRepository.findExpiredNotStartedAuctions()
      .forEach(auctionService::disableLotAndCloseAuction);
  }

  @PostConstruct
  public void scheduleNotStartedAuctions() {
    log.info("Scheduling not started auctions");

    auctionRepository.findFutureNotStartedAuctions()
      .forEach(this::scheduleAuctionStartDateCheck);
  }

  public void scheduleAuctionStartDateCheck(Auction savedAuction) {
    taskScheduler.schedule(
      () -> auctionService.startOrCloseAuction(savedAuction),
      Date.from(savedAuction.getStartDate().toInstant()));
  }

}
