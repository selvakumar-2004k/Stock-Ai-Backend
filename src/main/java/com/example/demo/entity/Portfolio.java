package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data; // <--- Make sure this is imported

@Entity
@Data // <--- This generates getSymbol(), getShares(), etc.
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private String name;
    private int shares;
    private double averagePrice;
    private String sector;


    private String userEmail;
}