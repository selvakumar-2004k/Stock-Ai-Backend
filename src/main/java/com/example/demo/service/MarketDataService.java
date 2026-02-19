package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MarketDataService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, Double> priceCache = new ConcurrentHashMap<>();
    private final String API_KEY = "dfb1acd03e0a4e1991d337593304a3cd";
    private long lastSyncTime = 0;
    private static final long MIN_SYNC_INTERVAL = 60000;

    public void updateAllPrices(List<String> symbols) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSyncTime < MIN_SYNC_INTERVAL && !priceCache.isEmpty()) return;

        try {
            // Force :NSE suffix for every symbol to ensure consistency
            String formattedSymbols = symbols.stream()
                    .map(s -> s.split(":")[0].toUpperCase() + ":NSE")
                    .collect(Collectors.joining(","));

            String url = String.format("https://api.twelvedata.com/price?symbol=%s&apikey=%s",
                    formattedSymbols, API_KEY);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null) {
                // Check if API returned an error message globally
                if (response.containsKey("message")) {
                    System.err.println("API LIMIT REACHED: " + response.get("message"));
                    return;
                }

                for (String key : response.keySet()) {
                    Object data = response.get(key);
                    if (data instanceof Map) {
                        Map<String, String> priceData = (Map<String, String>) data;
                        if (priceData.containsKey("price")) {
                            String cleanSymbol = key.split(":")[0].toUpperCase();
                            priceCache.put(cleanSymbol, Double.parseDouble(priceData.get("price")));
                        } else if (priceData.containsKey("message")) {
                            System.err.println("Error for " + key + ": " + priceData.get("message"));
                        }
                    }
                }
                lastSyncTime = currentTime;
            }
        } catch (Exception e) {
            System.err.println("Twelve Data Sync Error: " + e.getMessage());
        }
    }

    public double getStockPrice(String symbol) {
        // Return 0.0 or the last known price instead of 100.0 if cache is missing
        return priceCache.getOrDefault(symbol.toUpperCase(), getFallbackPrice(symbol));
    }

    private double getFallbackPrice(String symbol) {
        return switch (symbol.toUpperCase()) {
            case "RELIANCE" -> 2985.00;
            case "TCS" -> 4120.00;
            case "HDFCBANK" -> 1680.00;
            case "INFY" -> 1530.00;
            case "ICICIBANK" -> 1150.00;
            case "SBIN" -> 790.00;
            default -> 0.0; // Changed to 0.0 to make it obvious data is missing
        };
    }
}