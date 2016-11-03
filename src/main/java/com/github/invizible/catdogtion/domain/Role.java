package com.github.invizible.catdogtion.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "authorities")
@Data
public class Role {
  @Id
  @NotNull
  @Size(max = 50)
  @Column(name = "authority", length = 50)
  private String name;
}
