package com.playstation.manager.dto;

import com.playstation.manager.entity.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String name;
    private Role role; // ENUM: ADMIN, EMPLOYEE
}