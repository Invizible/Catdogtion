package com.github.invizible.catdogtion.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {
  @Id
  @GeneratedValue
  private Long id;

  @Size(min = 2, max = 30)
  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false, length = 60)
  private String password;

  @NotNull
  @Column(nullable = false)
  private Boolean enabled;

  @ManyToMany
  @JoinTable(name = "users_authorities",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns =  @JoinColumn(name = "authority_id")
  )
  private Set<Role> roles = new HashSet<>();
}
