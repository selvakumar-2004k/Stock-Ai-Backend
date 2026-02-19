package com.example.demo.controller;

import com.example.demo.entity.Transaction;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Ensure CORS matches your React port
public class TransactionController {

    private final TransactionRepository repo;

    // MERGED GET METHOD: Handles both "all" and "filtered by email"
    @GetMapping
    public List<Transaction> getTransactions(@RequestParam(required = false) String email) {
        if (email != null && !email.isEmpty()) {
            // Returns only data for this specific user
            return repo.findByUserEmail(email);
        }
        // For security, if no email is passed, return an empty list
        // (Prevents new users from seeing old/test data)
        return new ArrayList<>();
    }

    @PostMapping
    public Transaction create(@RequestBody Transaction t) {
        return repo.save(t);
    }

    @GetMapping("/{id}")
    public Transaction getById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }
}