package com.github.invizible.catdogtion.repository;

import com.github.invizible.catdogtion.domain.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotRepository extends JpaRepository<Lot, Long> {
}
