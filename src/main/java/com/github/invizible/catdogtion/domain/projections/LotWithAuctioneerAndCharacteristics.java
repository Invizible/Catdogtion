package com.github.invizible.catdogtion.domain.projections;

import com.github.invizible.catdogtion.domain.Characteristic;
import com.github.invizible.catdogtion.domain.Lot;
import com.github.invizible.catdogtion.domain.User;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Projection(name = "withAuctioneerAndCharacteristics", types = Lot.class)
public interface LotWithAuctioneerAndCharacteristics {
  Long getId();

  String getName();

  String getDescription();

  BigDecimal getStartingPrice();

  ZonedDateTime getCreationDate();

  User getAuctioneer();

  Set<Characteristic> getCharacteristics();
}
