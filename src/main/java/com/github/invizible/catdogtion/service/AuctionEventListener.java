package com.github.invizible.catdogtion.service;

import com.github.invizible.catdogtion.domain.Auction;
import com.github.invizible.catdogtion.event.StartedAuctionEvent;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class AuctionEventListener {

  private static final String STARTED_AUCTION_DESTINATION = "/topic/startedAuction";

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  @EventListener
  private void handleStartedAuctionEvent(StartedAuctionEvent startedAuctionEvent) {
    Auction startedAuction = startedAuctionEvent.getAuction();

    log.info(String.format("Pushing started auction to the front: %s", startedAuction));
    messagingTemplate.convertAndSend(STARTED_AUCTION_DESTINATION, startedAuction);
  }
}
