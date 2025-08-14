package com.playstation.manager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
public class CafeOrder {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime createdAt;

    @ManyToOne(optional = true)
    private Client client;

    @ManyToOne
    private User createdBy;

    private double totalAmount;

    private boolean finalized = false;

    private boolean isGuest;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CafeOrderItem> items;
}

