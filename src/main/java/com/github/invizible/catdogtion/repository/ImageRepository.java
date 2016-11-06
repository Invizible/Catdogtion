package com.github.invizible.catdogtion.repository;

import com.github.invizible.catdogtion.domain.Image;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Alex
 */
public interface ImageRepository extends CrudRepository<Image, Long> {
}
