package com.playstation.manager.repository;

import com.playstation.manager.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    Page<Expense> findByDateTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
