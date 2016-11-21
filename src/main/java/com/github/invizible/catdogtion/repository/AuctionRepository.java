package com.github.invizible.catdogtion.repository;

import com.github.invizible.catdogtion.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

  @Query("select a from Auction a where a.status = 'CREATED' and a.startDate < CURRENT_TIMESTAMP")
  List<Auction> findExpiredNotStartedAuctions();

  @Query("select a from Auction a where a.status = 'CREATED' and a.startDate >= CURRENT_TIMESTAMP")
  List<Auction> findFutureNotStartedAuctions();

  @SuppressWarnings("unchecked")
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
