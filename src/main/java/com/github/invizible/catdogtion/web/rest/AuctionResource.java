package com.github.invizible.catdogtion.web.rest;

import com.github.invizible.catdogtion.domain.Auction;
import com.github.invizible.catdogtion.domain.User;
import com.github.invizible.catdogtion.repository.AuctionRepository;
import com.github.invizible.catdogtion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
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


  private static class BetDTO {
    BigDecimal bet;
  }

  //TODO: test security
  @PreAuthorize("@auctionRepository.findOne(#auctionId).participants.![username].contains(principal.username)")
  @PostMapping("/{id}/bets")
  public ResponseEntity<?> makeABet(@RequestParam("id") Long auctionId, @RequestBody BetDTO betDTO) {
    BigDecimal newHighestPrice = betDTO.bet;

    Auction currentAuction = auctionRepository.findOne(auctionId);
    if (isNewHighestPriceBiggerThenCurrent(newHighestPrice, currentAuction)) {
      //TODO: cancel and reschedule task
      currentAuction.setHighestPrice(newHighestPrice);
      auctionRepository.save(currentAuction);

      return ResponseEntity.ok().build();
    }

    return ResponseEntity.badRequest().body("Bet size is less than current highest price!");
  }

  private boolean isNewHighestPriceBiggerThenCurrent(BigDecimal newHighestPrice, Auction currentAuction)
  {
    return newHighestPrice.compareTo(currentAuction.getHighestPrice()) > 0;
  }
}
