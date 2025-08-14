package com.playstation.manager.repository;

import com.playstation.manager.entity.ClientDebt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClientDebtRepository extends JpaRepository<ClientDebt, Long> {
    List<ClientDebt> findByClientId(Long clientId);
    Page<ClientDebt> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    List<ClientDebt> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
