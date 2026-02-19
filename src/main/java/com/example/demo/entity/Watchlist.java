package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    @ManyToOne
    private User user;
}
