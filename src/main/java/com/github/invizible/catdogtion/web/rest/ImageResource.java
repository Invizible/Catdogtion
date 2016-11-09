package com.github.invizible.catdogtion.web.rest;

import com.github.invizible.catdogtion.domain.Image;
import com.github.invizible.catdogtion.repository.ImageRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CommonsLog
public class ImageResource {

  @Autowired
  private ImageRepository imageRepository;

  @Autowired
  private RepositoryEntityLinks entityLinks;

  @PostMapping("/uploadImage")
  public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
    if (!file.isEmpty()) {
      try {
        Image image = new Image(file.getBytes(), file.getContentType());
        Image savedImage = imageRepository.save(image);
        return ResponseEntity.created(entityLinks.linkForSingleResource(Image.class, savedImage.getId()).toUri())
          .body(savedImage); //TODO: probably we should not return image content back which is used for preview
      } catch (Exception e) {
        log.error(e);
        return ResponseEntity.badRequest().body("Very bad file");
      }
    } else {
      return ResponseEntity.badRequest().body("File is empty!");
    }
  }
}
