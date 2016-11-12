package com.github.invizible.catdogtion.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Future;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Auction {
  @Id
  @GeneratedValue
  private Long id;

  @JsonIgnore
  @OneToOne(cascade = CascadeType.ALL, optional = false)
  private Lot lot;

  @ManyToMany
  private Set<User> participants = new HashSet<>();

  @Column(nullable = false)
  @Future
  private ZonedDateTime startDate = ZonedDateTime.now();

  @Column
  private ZonedDateTime endDate;

  @Column
  private BigDecimal wonPrice;

  @ManyToOne
  private User winner;
}
