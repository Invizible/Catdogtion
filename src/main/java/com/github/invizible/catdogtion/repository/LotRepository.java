package com.github.invizible.catdogtion.repository;

import com.github.invizible.catdogtion.domain.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LotRepository extends JpaRepository<Lot, Long> {

  @Query("select l from Lot l where l.auctioneer.username = ?#{principal.username}")
  List<Lot> findAllForCurrentUser();
}
