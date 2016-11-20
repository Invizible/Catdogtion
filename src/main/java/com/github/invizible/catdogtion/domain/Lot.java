package com.github.invizible.catdogtion.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alex
 */
@Entity
@Data
@EqualsAndHashCode(exclude = {"id", "auction"})
@ToString(exclude = {"images", "auction"})
public class Lot {
  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  @Length(min = 2)
  @Column(nullable = false)
  private String name;

  @NotNull
  @Length(min = 2)
  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private Boolean active = true;

  @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
  @Size(min = 1)
  private Set<Image> images = new HashSet<>();

  @NotNull
  @Min(0)
  @Column(nullable = false)
  private BigDecimal startingPrice = BigDecimal.valueOf(0);

  @Column(nullable = false)
  private ZonedDateTime creationDate = ZonedDateTime.now();

  @ManyToOne(optional = false)
  private User auctioneer;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "lot")
  private Auction auction;

  @ManyToMany
  private Set<Characteristic> characteristics = new HashSet<>();
}
