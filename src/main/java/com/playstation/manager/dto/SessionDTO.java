package com.playstation.manager.dto;

import com.playstation.manager.entity.GameMode;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class SessionDTO {
    private Long deviceId;
    private GameMode gameMode;
    private LocalDateTime startTime;
}
