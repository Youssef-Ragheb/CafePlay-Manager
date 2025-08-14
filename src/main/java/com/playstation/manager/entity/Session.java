package com.playstation.manager.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Setter
@Getter
@Entity
@Table(indexes = {
        @Index(name = "idx_session_start_time", columnList = "startTime"),
        @Index(name = "idx_session_status", columnList = "status"),
        @Index(name = "idx_session_created_by", columnList = "createdBy_id"),
        @Index(name = "idx_session_device", columnList = "device_id")
})
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PlaystationDevice device;

    @ManyToOne
    private User createdBy;
    @Enumerated(EnumType.STRING)
    private GameMode gameMode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private int durationMinutes;
    private double timePrice;
    private double totalOrdersPrice;
    private double totalPrice;
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private SessionStatus status; // ACTIVE, FINISHED
}

