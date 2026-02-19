package com.example.demo.controller;

import com.example.demo.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolios")
public class AIController {

    @Autowired
    private AIService aiService;

    @GetMapping("/ai-analysis")
    public AIService.AIResponse getFullAnalysis(@RequestParam(defaultValue = "RELIANCE, TCS") String stocks) {
        return aiService.getPortfolioAnalysis(stocks);

    }
}