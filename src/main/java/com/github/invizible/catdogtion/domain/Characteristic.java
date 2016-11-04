package com.github.invizible.catdogtion.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Characteristic {
  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  @OneToOne(optional = false)
  private CharacteristicName name;

  @NotNull
  @OneToOne(optional = false)
  private CharacteristicValue value;

  @ManyToMany(mappedBy = "characteristics")
  private Set<Lot> lots = new HashSet<>();
}
