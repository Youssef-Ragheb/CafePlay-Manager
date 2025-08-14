package com.playstation.manager.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Data
@Setter
@Getter
@Entity
@Table(indexes = {
        @Index(name = "idx_expense_date", columnList = "dateTime"),
        @Index(name = "idx_expense_user", columnList = "createdBy_id")
})
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;

    private double amount;

    private LocalDateTime dateTime;

    @ManyToOne
    private User createdBy;
}

