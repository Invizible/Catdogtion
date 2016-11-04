package com.github.invizible.catdogtion.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class CharacteristicValue {
  @Id
  @GeneratedValue
  private Long id;

  @Length(min = 2)
  @Column(nullable = false)
  private String value;
}
