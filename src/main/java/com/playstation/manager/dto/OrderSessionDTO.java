package com.playstation.manager.dto;

import lombok.Data;

@Data
public class OrderSessionDTO {
    private long orderId;
    private String productName;
    private int quantity;
    private double price;
}
