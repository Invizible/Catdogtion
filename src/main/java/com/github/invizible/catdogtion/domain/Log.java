package com.github.invizible.catdogtion.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(exclude = {"id"})
public class Log {
  @Id
  @GeneratedValue
  private Long id;

  private String message;

  @Column(nullable = false)
  @CreatedDate
  private ZonedDateTime time;

  public Log() {}

  public Log(String message) {
    this.message = message;
  }
}
