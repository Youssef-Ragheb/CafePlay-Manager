package com.playstation.manager.dto;

import lombok.Data;

@Data
public class ExpenseDTO {
    private String reason;
    private double amount;
}