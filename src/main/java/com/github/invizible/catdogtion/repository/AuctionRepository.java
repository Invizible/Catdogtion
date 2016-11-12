package com.github.invizible.catdogtion.repository;

import com.github.invizible.catdogtion.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

  @RestResource(exported = false)
  @Override
  Auction save(Auction entity);

  @RestResource(exported = false)
  @Override
  void delete(Long aLong);

  @RestResource(exported = false)
  @Override
  void delete(Auction entity);
}
