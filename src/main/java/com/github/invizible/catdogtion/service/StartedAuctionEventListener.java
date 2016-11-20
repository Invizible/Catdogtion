package com.github.invizible.catdogtion.service;

import com.github.invizible.catdogtion.event.StartedAuctionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class StartedAuctionEventListener {

  private static final String STARTED_AUCTION_DESTINATION = "/topic/startedAuction";

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  @EventListener
  private void handleStartedAuctionEvent(StartedAuctionEvent startedAuctionEvent) {
    messagingTemplate.convertAndSend(STARTED_AUCTION_DESTINATION, startedAuctionEvent.getAuction());
  }
}
