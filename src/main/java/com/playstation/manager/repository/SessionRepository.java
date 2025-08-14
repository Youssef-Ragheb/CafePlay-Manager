package com.playstation.manager.repository;

import com.playstation.manager.entity.Session;
import com.playstation.manager.entity.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByDeviceIdAndStatus(Long deviceId, SessionStatus status);
    List<Session> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    Page<Session> findByStartTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
