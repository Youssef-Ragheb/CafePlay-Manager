package com.playstation.manager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(
        indexes = {
                @Index(name = "idx_actionlog_timestamp", columnList = "timestamp"),
                @Index(name = "idx_actionlog_user", columnList = "performed_by_id"),
                @Index(name = "idx_actionlog_user_timestamp", columnList = "performed_by_id, timestamp")
        }
)
public class ActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actionType; // CREATE, UPDATE, DELETE

    private String entityType; // مثلا: Client, Expense, Debt

    private Long entityId;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "performed_by_id") // allows indexing
    private User performedBy;
}