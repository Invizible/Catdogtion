package com.github.invizible.catdogtion.web.rest;

import com.github.invizible.catdogtion.domain.Image;
import com.github.invizible.catdogtion.repository.ImageRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api")
@CommonsLog
public class ImageResource {

  @Autowired
  private ImageRepository imageRepository;

  @PostMapping("/uploadImage")
  public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
    if (!file.isEmpty()) {
      try {
        Image image = new Image(file.getBytes(), file.getContentType());
        Image savedImage = imageRepository.save(image);
        return ResponseEntity.created(new URI("/api/images/" + savedImage.getId()))
          .body(savedImage);
      } catch (Exception e) {
        log.error(e);
        return ResponseEntity.badRequest().body("Very bad file");
      }
    } else {
      return ResponseEntity.badRequest().body("File is empty!");
    }
  }
}
