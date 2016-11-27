package com.github.invizible.catdogtion.service;

import com.github.invizible.catdogtion.domain.Auction;
import com.github.invizible.catdogtion.event.RescheduleBetTimeoutTaskEvent;
import com.github.invizible.catdogtion.event.StartedAuctionEvent;
import com.github.invizible.catdogtion.repository.AuctionRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@CommonsLog
public class AuctionScheduler {

  @Autowired
  private TaskScheduler taskScheduler;

  @Autowired
  private AuctionRepository auctionRepository;

  @Autowired
  private AuctionService auctionService;

  @Value("${auction.betTimeoutInMinutes}")
  private int betTimeoutInMinutes;

  @Value("${auction.auctionTimeoutInMinutes}")
  private int auctionTimeoutInMinutes;

  private Map<Long, ScheduledFuture<?>> betTimeoutScheduledFutures = new ConcurrentHashMap<>();
  private Map<Long, ScheduledFuture<?>> auctionTimeoutScheduledFutures = new ConcurrentHashMap<>();

  @PostConstruct
  public void closeExpiredAuctions() {
    log.info("Closing expired auctions");

    auctionRepository.findExpiredNotStartedAuctions()
      .forEach(auctionService::closeAuctionDueToLowParticipantsCount);
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

  @EventListener
  public void scheduleAuctionTimeout(StartedAuctionEvent auctionEvent) {
    scheduleTimeoutTask(
      auctionEvent,
      auctionTimeoutInMinutes,
      auctionTimeoutScheduledFutures
    );
  }

  @EventListener
  public void scheduleBetTimeout(StartedAuctionEvent auctionEvent) {
    scheduleTimeoutTask(
      auctionEvent,
      betTimeoutInMinutes,
      betTimeoutScheduledFutures
    );
  }

  private void scheduleTimeoutTask(
    StartedAuctionEvent auctionEvent,
    int timeoutInMinutes,
    Map<Long, ScheduledFuture<?>> scheduledFutures) {

    Auction auction = auctionEvent.getAuction();

    ScheduledFuture<?> future = taskScheduler.schedule(
      () -> {
        cancelTask(auction, auctionTimeoutScheduledFutures);
        cancelTask(auction, betTimeoutScheduledFutures);
        auctionService.announceWinnerOrCloseAuctionDueToBetTimeout(auction);
      },
      createPeriodicTrigger(timeoutInMinutes));

    scheduledFutures.put(auction.getId(), future);
  }

  private PeriodicTrigger createPeriodicTrigger(int timeout) {
    PeriodicTrigger trigger = new PeriodicTrigger(timeout, TimeUnit.MINUTES);
    trigger.setInitialDelay(timeout);
    return trigger;
  }

  @EventListener
  public void cancelAndRescheduleBetTimeoutTask(RescheduleBetTimeoutTaskEvent rescheduleEvent) {
    Auction auction = rescheduleEvent.getAuction();
    cancelTask(auction, betTimeoutScheduledFutures);

    scheduleBetTimeout(new StartedAuctionEvent(auction));
  }

  private void cancelTask(Auction auction, Map<Long, ScheduledFuture<?>> scheduledFutures) {
    ScheduledFuture<?> future = scheduledFutures.remove(auction.getId());
    if (future != null) {
      future.cancel(false);
    }
  }

}
