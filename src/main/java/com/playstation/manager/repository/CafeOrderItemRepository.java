package com.playstation.manager.repository;

import com.playstation.manager.entity.CafeOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeOrderItemRepository extends JpaRepository<CafeOrderItem, Long> {
}
