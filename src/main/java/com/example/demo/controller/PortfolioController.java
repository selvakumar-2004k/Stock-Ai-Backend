package com.example.demo.controller;

import com.example.demo.dto.DashboardDTO;
import com.example.demo.entity.Portfolio;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor // This handles the constructor injection for repo and marketDataService
public class PortfolioController {

    private final PortfolioRepository repo;
    private final MarketDataService marketDataService;

    // FIX 1: Filter the list by email so users only see their own stocks
    @GetMapping
    public List<Portfolio> getPortfolio(@RequestParam String email) {
        // If no email is provided, return empty list instead of ALL stocks
        if (email == null || email.isEmpty()) {
            return new ArrayList<>();
        }
        return repo.findByUserEmail(email);
    }

    @PostMapping
    public Portfolio create(@RequestBody Portfolio portfolio) {
        return repo.save(portfolio);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAsset(@PathVariable Long id) {
        return repo.findById(id)
                .map(asset -> {
                    repo.delete(asset);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // FIX 2: Filter the Dashboard Summary by email too!
    @GetMapping("/summary")
    public DashboardDTO getDashboardData(@RequestParam String email) {
        // 1. Fetch assets ONLY for this user
        List<Portfolio> assets = repo.findByUserEmail(email);

        double totalValue = 0;
        List<Map<String, Object>> holdings = new ArrayList<>();

        for (Portfolio asset : assets) {
            double currentPrice = marketDataService.getStockPrice(asset.getSymbol());
            double assetHoldingsValue = currentPrice * asset.getShares();
            totalValue += assetHoldingsValue;

            Map<String, Object> assetMap = new HashMap<>();
            assetMap.put("symbol", asset.getSymbol());
            assetMap.put("shares", asset.getShares());
            assetMap.put("value", assetHoldingsValue);
            assetMap.put("price", currentPrice);

            holdings.add(assetMap);
        }

        DashboardDTO dto = new DashboardDTO();
        dto.setTotalPortfolioValue(totalValue);
        dto.setTotalAssets(assets.size());
        dto.setDailyChange(0.0); // You can calculate this later
        dto.setDailyChangePercent(0.0);
        dto.setAiHealthScore(8.5);
        dto.setTopHoldings(holdings);

        return dto;
    }
}