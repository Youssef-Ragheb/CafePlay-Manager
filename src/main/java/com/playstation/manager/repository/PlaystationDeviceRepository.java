package com.playstation.manager.repository;

import com.playstation.manager.entity.PlaystationDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaystationDeviceRepository extends JpaRepository<PlaystationDevice, Long> {
}
