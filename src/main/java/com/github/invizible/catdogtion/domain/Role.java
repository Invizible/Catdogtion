package com.github.invizible.catdogtion.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "authorities")
@Data
@EqualsAndHashCode(exclude = {"id"})
public class Role {
  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  @Size(max = 50)
  @Column(name = "authority", length = 50)
  private String name;
}
