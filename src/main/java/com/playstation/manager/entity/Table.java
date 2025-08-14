package com.playstation.manager.entity;

import lombok.Data;

import java.util.List;
@Data
public class Table {
    private Long id;
    private Customer customer;
    private Client client;
    private List<Order> orders;
    private boolean isAvailable;
    private float totalPrice;

}
