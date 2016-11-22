package com.github.invizible.catdogtion.service;

import com.github.invizible.catdogtion.domain.Auction;
import com.github.invizible.catdogtion.domain.Lot;
import com.github.invizible.catdogtion.repository.AuctionRepository;
import com.github.invizible.catdogtion.repository.LotRepository;
import com.github.invizible.catdogtion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@Transactional
public class LotService {

  @Autowired
  private AuctionRepository auctionRepository;

  @Autowired
  private LotRepository lotRepository;

  @Autowired
  private UserRepository userRepository;

  @Value("${auction.startDateOffsetInMinutes}")
  private long auctionStartDateOffsetInMinutes;

  @Autowired
  private AuctionScheduler auctionScheduler;

  public void createAuction(Lot lot) {
    Auction auction = new Auction();
    auction.setLot(lot);
    auction.setStartDate(ZonedDateTime.now().plusMinutes(auctionStartDateOffsetInMinutes));
    auction.setHighestPrice(lot.getStartingPrice());
    Auction savedAuction = auctionRepository.save(auction);

    auctionScheduler.scheduleAuctionStartDateCheck(savedAuction);
  }

  public Lot saveLot(Lot lot) {
    lot.setAuctioneer(userRepository.findCurrentUser());
    return lotRepository.save(lot);
  }
}
