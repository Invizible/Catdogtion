package com.github.invizible.catdogtion.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

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
@EqualsAndHashCode(exclude = {"id"})
@ToString(exclude = {"password"})
public class User {
  @Id
  @GeneratedValue
  private Long id;

  @NotEmpty
  @Size(min = 2, max = 30)
  @Column(unique = true, nullable = false)
  private String username;

  @NotEmpty
  @Size(min = 1, max = 50)
  @Column(nullable = false)
  private String firstName;

  @NotEmpty
  @Size(min = 1, max = 50)
  @Column(nullable = false)
  private String lastName;

  @Email
  @Column(nullable = false)
  private String email;

  @JsonIgnore
  @NotEmpty
  @Column(nullable = false, length = 60)
  private String password;

  @NotNull
  @Column(nullable = false)
  private Boolean enabled = true;

  @ManyToMany
  @JoinTable(name = "users_authorities",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns =  @JoinColumn(name = "authority_id")
  )
  private Set<Role> roles = new HashSet<>();
}
