package com.playstation.manager.repository;

import com.playstation.manager.entity.CafeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeOrderRepository extends JpaRepository<CafeOrder, Long> {
    List<CafeOrder> findAllByFinalizedFalse();
}
