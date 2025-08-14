package com.playstation.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.Table;

import lombok.ToString;

@Data
@Entity
@Table(name = "product_order", indexes = {
        @Index(name = "idx_order_session", columnList = "session_id"),
        @Index(name = "idx_order_product", columnList = "product_id")
})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    private Session session;

    @ManyToOne
    @ToString.Exclude
    private Product product;

    private int quantity;

    private double totalPrice;
}
