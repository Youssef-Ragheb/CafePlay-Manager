package com.playstation.manager.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Entity
@Table(indexes = {
        @Index(name = "idx_client_phone", columnList = "phone"),
        @Index(name = "idx_client_name", columnList = "name")
})
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
}

