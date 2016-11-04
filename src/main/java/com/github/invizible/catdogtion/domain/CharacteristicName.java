package com.github.invizible.catdogtion.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class CharacteristicName {
  @Id
  @GeneratedValue
  private Long id;

  @Length(min = 2)
  @Column(nullable = false)
  private String name;

  @OneToMany
  @JoinColumn
  private Set<CharacteristicValue> values = new HashSet<>();
}
