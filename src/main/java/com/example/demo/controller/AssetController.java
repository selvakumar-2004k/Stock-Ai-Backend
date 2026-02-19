package com.example.demo.controller;

import com.example.demo.entity.Asset;
import com.example.demo.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetRepository repo;

    @GetMapping
    public List<Asset> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public Asset add(@RequestBody Asset asset) {
        return repo.save(asset);
    }

    @GetMapping("/{id}")
    public Asset getById(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public Asset update(@PathVariable Long id, @RequestBody Asset a) {
        a.setId(id);
        return repo.save(a);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}

