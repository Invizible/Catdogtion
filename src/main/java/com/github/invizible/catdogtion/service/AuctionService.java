package com.github.invizible.catdogtion.service;

import com.github.invizible.catdogtion.domain.Auction;
import com.github.invizible.catdogtion.domain.AuctionStatus;
import com.github.invizible.catdogtion.domain.Log;
import com.github.invizible.catdogtion.domain.Lot;
import com.github.invizible.catdogtion.domain.User;
import com.github.invizible.catdogtion.event.AuctionWinnerEvent;
import com.github.invizible.catdogtion.event.RescheduleBetTimeoutTaskEvent;
import com.github.invizible.catdogtion.event.StartedAuctionEvent;
import com.github.invizible.catdogtion.repository.AuctionRepository;
import com.github.invizible.catdogtion.repository.LogRepository;
import com.github.invizible.catdogtion.repository.LotRepository;
import com.github.invizible.catdogtion.repository.UserRepository;
import com.github.invizible.catdogtion.util.MoneyUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

@Service
@Transactional
@CommonsLog
public class AuctionService {
  private static final int MIN_PARTICIPANTS_BOUNDARY = 1;
  private static final String THE_AUCTION_WAS_CLOSED_LOG = "The auction was closed due to low participants count";
  private static final String THE_AUCTION_HAS_STARTED_LOG = "The auction has just started. Starting price is: %s";
  private static final String BET_TIMEOUT_CLOSE_AUCTION_LOG_MESSAGE = "An auction was closed because no one made a bet";

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

  @Autowired
  private UserRepository userRepository;

  public void startOrCloseAuction(Auction savedAuction) {
    log.info(String.format("Scheduler for auction with id: %d has been started", savedAuction.getId()));

    Auction auction = auctionRepository.findOne(savedAuction.getId());
    Set<User> participants = auction.getParticipants();

    if (participants.size() > MIN_PARTICIPANTS_BOUNDARY) {
      startAuction(auction);
    } else {
      closeAuctionDueToLowParticipantsCount(auction);
    }
  }

  private void disableLotAndCloseAuction(Auction auction, Log closeAuctionLog) {
    disableLot(auction.getLot());
    closeAuction(auction, closeAuctionLog);
  }

  public void closeAuctionDueToLowParticipantsCount(Auction auction) {
    disableLotAndCloseAuction(auction, new Log(THE_AUCTION_WAS_CLOSED_LOG));
  }

  public void announceWinnerOrCloseAuctionDueToBetTimeout(Auction auction) {
    if (isLastBetBiggerThanStartingPrice(auction)) {
//      auction.setWinner(); TODO: set winner
      applicationEventPublisher.publishEvent(new AuctionWinnerEvent(auction));
    } else {
      disableLotAndCloseAuction(auction, new Log(BET_TIMEOUT_CLOSE_AUCTION_LOG_MESSAGE));
    }
  }

  private boolean isLastBetBiggerThanStartingPrice(Auction auction)
  {
    return auction.getHighestPrice().compareTo(auction.getLot().getStartingPrice()) > 0;
  }

  private void startAuction(Auction auction) {
    log.info(String.format("Starting the auction: %d", auction.getId()));

    String startingPrice = MoneyUtils.format(auction.getLot().getStartingPrice());
    auction.getLogs().add(new Log(String.format(THE_AUCTION_HAS_STARTED_LOG, startingPrice)));

    auction.setStatus(AuctionStatus.IN_PROGRESS);
    Auction savedAuction = auctionRepository.save(auction);

    emailService.sendAuctionStartingNotificationToAllParticipants(savedAuction);

    //fire an event, so we can send started auction back to the front (in future can be replaced with RxJava)
    applicationEventPublisher.publishEvent(new StartedAuctionEvent(savedAuction));
  }

  private void disableLot(Lot lot) {
    log.info(String.format("Disabling lot: %d", lot.getId()));

    lot.setActive(false);
    lotRepository.save(lot);
  }

  private void closeAuction(Auction auction, Log closeAuctionLog) {
    log.info(String.format("Closing auction: %d", auction.getId()));

    Log log = logRepository.save(closeAuctionLog);
    auction.getLogs().add(log);
    auction.setStatus(AuctionStatus.CLOSED);
    auction.setEndDate(log.getTime());
    auctionRepository.save(auction);
  }

  public boolean addBet(Long auctionId, BigDecimal newHighestPrice, String currentUserName) {
    Auction currentAuction = auctionRepository.findOne(auctionId);

    if (isNewHighestPriceBiggerThenCurrent(newHighestPrice, currentAuction)) {
      applicationEventPublisher.publishEvent(new RescheduleBetTimeoutTaskEvent(currentAuction));
      saveBet(newHighestPrice, currentUserName, currentAuction);
      return true;
    }

    return false;
  }

  private void saveBet(BigDecimal newHighestPrice, String currentUserName, Auction currentAuction)
  {
    User currentUser = userRepository.findByUsername(currentUserName).get();
    Log log = new Log(String.format("User %s %s made a bet: %s",
      currentUser.getFirstName(),
      currentUser.getLastName(),
      MoneyUtils.format(newHighestPrice)));
    currentAuction.getLogs().add(log);
    currentAuction.setHighestPrice(newHighestPrice);
    auctionRepository.save(currentAuction);
  }

  private boolean isNewHighestPriceBiggerThenCurrent(BigDecimal newHighestPrice, Auction currentAuction)
  {
    return newHighestPrice.compareTo(currentAuction.getHighestPrice()) > 0;
  }
}
