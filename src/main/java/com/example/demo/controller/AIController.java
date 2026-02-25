package com.example.demo.controller;

import com.example.demo.service.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolios")
@CrossOrigin
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/ai-analysis")
    public ResponseEntity<?> getFullAnalysis(
            @RequestParam(defaultValue = "RELIANCE,TCS") String stocks) {

        try {

            AIService.AIResponse response =
                    aiService.getPortfolioAnalysis(stocks);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity
                    .status(503)
                    .body("AI service temporarily unavailable. Please try again later.");
        }
    }
}