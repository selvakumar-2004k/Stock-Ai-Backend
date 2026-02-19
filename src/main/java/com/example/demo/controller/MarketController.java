package com.example.demo.controller;

import com.example.demo.service.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/market")
@CrossOrigin(origins = "http://localhost:3000") // Allow React to access this
public class MarketController {

    @Autowired
    private MarketDataService marketDataService;

    @GetMapping("/prices")
    public Map<String, Double> getLivePrices(@RequestParam List<String> symbols) {
        System.out.println("Received request for: " + symbols);

        // 1. FORCE UPDATE: Fetch fresh data from Yahoo for these specific symbols
        // This ensures the cache is hot before we read from it.
        marketDataService.updateAllPrices(symbols);

        // 2. READ & RETURN: Now that cache is updated, return the values
        Map<String, Double> response = new HashMap<>();
        for (String symbol : symbols) {
            response.put(symbol, marketDataService.getStockPrice(symbol));
        }

        return response;
    }
}