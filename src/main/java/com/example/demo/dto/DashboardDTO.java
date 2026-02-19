package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map; // Add this import

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private double totalPortfolioValue;
    private double dailyChange;
    private double dailyChangePercent;
    private int totalAssets;
    private double aiHealthScore;

    // Specific type for the list of asset data
    private List<Map<String, Object>> topHoldings;
}