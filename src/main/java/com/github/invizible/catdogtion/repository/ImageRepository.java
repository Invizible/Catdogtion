package com.github.invizible.catdogtion.repository;

import com.github.invizible.catdogtion.domain.Image;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Alex
 */
public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {
}
