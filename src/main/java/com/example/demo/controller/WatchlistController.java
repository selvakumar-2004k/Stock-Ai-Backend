package com.example.demo.controller;

import com.example.demo.entity.Watchlist;
import com.example.demo.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistRepository repo;

    @GetMapping
    public List<Watchlist> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public Watchlist add(@RequestBody Watchlist w) {
        return repo.save(w);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
