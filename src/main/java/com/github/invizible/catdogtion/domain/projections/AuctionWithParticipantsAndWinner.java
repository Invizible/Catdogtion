package com.github.invizible.catdogtion.domain.projections;

import com.github.invizible.catdogtion.domain.Auction;
import com.github.invizible.catdogtion.domain.AuctionStatus;
import com.github.invizible.catdogtion.domain.User;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Projection(name = "withParticipantsAndWinner", types = Auction.class)
public interface AuctionWithParticipantsAndWinner {
  Long getId();

  Set<User> getParticipants();

  ZonedDateTime getStartDate();

  ZonedDateTime getEndDate();

  BigDecimal getWonPrice();

  User getWinner();

  AuctionStatus getStatus();
}
