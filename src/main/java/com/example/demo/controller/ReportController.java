package com.example.demo.controller;

import com.example.demo.entity.Report;
import com.example.demo.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportRepository repo;

    @GetMapping
    public List<Report> getAll() {
        return repo.findAll();
    }

    @PostMapping("/generate")
    public Report generate(@RequestBody Report report) {
        report.setCreatedAt(LocalDateTime.now());
        return repo.save(report);
    }
}
