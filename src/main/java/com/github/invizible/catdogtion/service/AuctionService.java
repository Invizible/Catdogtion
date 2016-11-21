package com.github.invizible.catdogtion.service;

import com.github.invizible.catdogtion.domain.Auction;
import com.github.invizible.catdogtion.domain.AuctionStatus;
import com.github.invizible.catdogtion.domain.Log;
import com.github.invizible.catdogtion.domain.Lot;
import com.github.invizible.catdogtion.domain.User;
import com.github.invizible.catdogtion.event.StartedAuctionEvent;
import com.github.invizible.catdogtion.repository.AuctionRepository;
import com.github.invizible.catdogtion.repository.LogRepository;
import com.github.invizible.catdogtion.repository.LotRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Set;

@Service
@Transactional
@CommonsLog
public class AuctionService {
  private static final int MIN_PARTICIPANTS_BOUNDARY = 1;
  private static final String THE_AUCTION_WAS_CLOSED_LOG = "The auction was closed due to low participants count";
  private static final String THE_AUCTION_HAS_STARTED_LOG = "The auction has just started. Starting price is: %s";
  private static final String MONEY_FORMAT = "#0.##";

  @Autowired
  private AuctionRepository auctionRepository;

  @Autowired
  private LotRepository lotRepository;

  @Autowired
  private LogRepository logRepository;

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  private EmailService emailService;

  public void startOrCloseAuction(Auction savedAuction) {
    log.info(String.format("Scheduler for auction with id: %d has been started", savedAuction.getId()));

    Auction auction = auctionRepository.findOne(savedAuction.getId());
    Set<User> participants = auction.getParticipants();

    if (participants.size() > MIN_PARTICIPANTS_BOUNDARY) {
      startAuction(auction);
    } else {
      disableLotAndCloseAuction(auction);
    }
  }

  public void disableLotAndCloseAuction(Auction auction) {
    disableLot(auction.getLot());
    closeAuction(auction);
  }

  private void startAuction(Auction auction) {
    log.info(String.format("Starting the auction: %d", auction.getId()));

    String startingPrice = new DecimalFormat(MONEY_FORMAT).format(auction.getLot().getStartingPrice());
    auction.getLogs().add(new Log(String.format(THE_AUCTION_HAS_STARTED_LOG, startingPrice)));

    auction.setStatus(AuctionStatus.IN_PROGRESS);
    Auction savedAuction = auctionRepository.save(auction);

    emailService.sendAuctionStartingNotificationToAllParticipants(savedAuction);

    applicationEventPublisher.publishEvent(new StartedAuctionEvent(savedAuction)); //fire an event, so we can send started auction back to the front (in future can be replaced with RxJava)
  }

  private void disableLot(Lot lot) {
    log.info(String.format("Disabling lot: %d", lot.getId()));

    lot.setActive(false);
    lotRepository.save(lot);
  }

  private void closeAuction(Auction auction) {
    log.info(String.format("Closing auction: %d", auction.getId()));

    Log log = logRepository.save(new Log(THE_AUCTION_WAS_CLOSED_LOG));
    auction.getLogs().add(log);
    auction.setStatus(AuctionStatus.CLOSED);
    auction.setEndDate(log.getTime());
    auctionRepository.save(auction);
  }
}
