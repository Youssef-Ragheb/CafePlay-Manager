package com.playstation.manager.dto;

import lombok.Data;

@Data
public class ClientDebtDTO {
    private Long clientId;
    private double amount;
    private String note;
}