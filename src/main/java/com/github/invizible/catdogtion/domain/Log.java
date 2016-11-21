package com.github.invizible.catdogtion.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Entity
@Data
@EqualsAndHashCode(exclude = {"id"})
public class Log {
  @Id
  @GeneratedValue
  private Long id;

  private String message;

  @CreatedDate
  private ZonedDateTime time;
}
