package com.github.invizible.catdogtion.web.rest;

import com.github.invizible.catdogtion.domain.Lot;
import com.github.invizible.catdogtion.repository.LotRepository;
import com.github.invizible.catdogtion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
@ResponseBody
@RequestMapping("/lots")
public class LotResource {

  @Autowired
  private LotRepository lotRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RepositoryEntityLinks entityLinks;

  @PostMapping
  public ResponseEntity<?> saveLot(@RequestBody @Valid Lot lot) {
    if (lot.getId() != null) {
      return ResponseEntity.badRequest().body("New lot can't already have an id");
    }

    lot.setAuctioneer(userRepository.findCurrentUser());
    Lot savedLot = lotRepository.save(lot);

    Resource<Lot> lotResource = new Resource<>(savedLot);
    lotResource.add(linkTo(methodOn(LotResource.class).saveLot(savedLot)).withSelfRel());

    return ResponseEntity
      .created(entityLinks.linkForSingleResource(Lot.class, savedLot.getId()).toUri())
      .body(lotResource);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateLot(@RequestBody @Valid Lot lot) {
    if (lot.getId() == null) {
      return saveLot(lot);
    }

    lot.setAuctioneer(userRepository.findCurrentUser());
    Lot savedLot = lotRepository.save(lot);

    Resource<Lot> lotResource = new Resource<>(savedLot);
    lotResource.add(linkTo(methodOn(LotResource.class).updateLot(savedLot)).withSelfRel());
    return ResponseEntity.ok(lotResource);
  }
}
