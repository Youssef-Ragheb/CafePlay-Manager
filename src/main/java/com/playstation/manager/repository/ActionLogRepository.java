package com.playstation.manager.repository;

import com.playstation.manager.entity.ActionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface ActionLogRepository extends CrudRepository<ActionLog, Long> {
    Page<ActionLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
