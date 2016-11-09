package com.github.invizible.catdogtion.web.rest;

import com.github.invizible.catdogtion.domain.Lot;
import com.github.invizible.catdogtion.repository.LotRepository;
import com.github.invizible.catdogtion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@RepositoryRestController
@ResponseBody
public class LotResource {

  @Autowired
  private LotRepository lotRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RepositoryEntityLinks entityLinks;

  @PostMapping("/lots")
  public ResponseEntity<?> saveLot(@RequestBody @Valid Lot lot) {
    if (lot.getId() != null) {
      return ResponseEntity.badRequest().body("New lot can't already have an id");
    }

    lot.setAuctioneer(userRepository.findCurrentUser());
    Lot savedLot = lotRepository.save(lot);
    return ResponseEntity
      .created(entityLinks.linkForSingleResource(Lot.class, savedLot.getId()).toUri())
      .body(savedLot);
  }
}
