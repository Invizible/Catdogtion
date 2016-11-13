package com.github.invizible.catdogtion.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(exclude = {"id"})
public class Image {
  private static final String DEFAULT_CONTENT_TYPE = "image/jpeg";

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  @Lob
  @Column(nullable = false)
  private byte[] image;

  @NotEmpty
  @Column(nullable = false)
  private String contentType = DEFAULT_CONTENT_TYPE;

  public Image() {
  }

  public Image(byte[] image) {
    this.image = image;
  }

  public Image(byte[] image, String contentType) {
    this.image = image;
    this.contentType = contentType;
  }
}
