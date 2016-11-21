package com.github.invizible.catdogtion.repository;

import com.github.invizible.catdogtion.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
