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
        @Index(name = "idx_debt_client", columnList = "client_id"),
        @Index(name = "idx_debt_created_at", columnList = "createdAt")
})
public class ClientDebt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    private double amount;

    private String note; // optional invoice id or reason

    private LocalDateTime createdAt;
}
