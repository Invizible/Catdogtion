package com.github.invizible.catdogtion.service;

import com.github.invizible.catdogtion.domain.Auction;
import com.github.invizible.catdogtion.event.AuctionWinnerEvent;
import com.github.invizible.catdogtion.event.StartedAuctionEvent;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class AuctionListener {

  private static final String STARTED_AUCTION_DESTINATION = "/queue/startedAuction";
  private static final String AUCTION_WINNER_DESTINATION = "/topic/auction/%s/auctionWinner";

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  @EventListener
  public void handleStartedAuctionEvent(StartedAuctionEvent startedAuctionEvent) {
    Auction startedAuction = startedAuctionEvent.getAuction();

    log.info(String.format("Pushing started auction to the front: %d", startedAuction.getId()));

    sendToParticipants(STARTED_AUCTION_DESTINATION, startedAuction);
  }

  private void sendToParticipants(String destination, Auction startedAuction)
  {
    startedAuction.getParticipants().forEach(participant ->
      messagingTemplate.convertAndSendToUser(participant.getUsername(), destination, startedAuction));
  }

  @EventListener
  public void handleAuctionWinnerEvent(AuctionWinnerEvent winnerEvent) {
    Auction auction = winnerEvent.getAuction();

    log.info(String.format("Pushing auction withWinner to the front: %d", auction.getId()));

    messagingTemplate.convertAndSend(String.format(AUCTION_WINNER_DESTINATION, auction.getId()), auction);
  }
}
