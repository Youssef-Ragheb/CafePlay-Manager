package com.playstation.manager.repository;

import com.playstation.manager.entity.PlaystationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaystationTypeRepository extends JpaRepository<PlaystationType, Long> {
}
