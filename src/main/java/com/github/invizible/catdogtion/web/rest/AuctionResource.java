package com.github.invizible.catdogtion.web.rest;

import com.github.invizible.catdogtion.domain.Auction;
import com.github.invizible.catdogtion.domain.User;
import com.github.invizible.catdogtion.repository.AuctionRepository;
import com.github.invizible.catdogtion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RepositoryRestController
@ResponseBody
@RequestMapping("/auctions")
public class AuctionResource {

  @Autowired
  private AuctionRepository auctionRepository;

  @Autowired
  private UserRepository userRepository;

  @PostMapping("/{id}/participants")
  public ResponseEntity<?> addParticipant(@PathVariable("id") Auction auction, UriComponentsBuilder ucb) {
    User currentUser = userRepository.findCurrentUser();

    auction.getParticipants().add(currentUser);

    Auction savedAuction = auctionRepository.save(auction);

    URI uri = ucb
      .path("/auctions/")
      .path(auction.getId().toString())
      .path("/participants/")
      .path(currentUser.getId().toString())
      .build()
      .toUri();

    return ResponseEntity.created(uri).body(savedAuction);
  }

  @SendTo("/topic/startedAuction")
  public Auction getStartedAuction() {
    //TODO: implement
    return null;
  }
}
