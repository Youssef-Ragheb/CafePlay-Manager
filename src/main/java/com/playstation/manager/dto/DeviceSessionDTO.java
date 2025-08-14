package com.playstation.manager.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.playstation.manager.entity.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DeviceSessionDTO {

    private Long id;

    private PlaystationDevice device;


    private User createdBy;
    @Enumerated(EnumType.STRING)
    private GameMode gameMode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private int durationMinutes;
    private double timePrice;
    private double totalOrdersPrice;
    private double totalPrice;

    private List<OrderSessionDTO> orders;
    @Enumerated(EnumType.STRING)
    private SessionStatus status; // ACTIVE, FINISHED
}


