package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private String type; // BUY or SELL
    private Integer shares;
    private Double price;
    private Double total;

    // --- THIS IS THE MISSING FIELD ---
    private String userEmail;
}
