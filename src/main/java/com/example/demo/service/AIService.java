package com.example.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIService {

    private final ChatClient chatClient;

    public record Insight(
            int id,
            String category,
            String title,
            String severity,
            String description,
            String recommendation,
            String impact
    ) {}

    public record Prediction(
            String symbol,
            String name,
            double currentPrice,
            double predictedPrice,
            int confidence,
            String timeframe,
            String sentiment
    ) {}

    public record AIResponse(
            List<Insight> insights,
            List<Prediction> predictions,
            int score
    ) {}

    public AIService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    // 🔥 CACHE ADDED HERE
    @Cacheable(value = "portfolioAnalysis", key = "#stocks")
    public AIResponse getPortfolioAnalysis(String stocks) {

        try {

            var converter = new BeanOutputConverter<>(AIResponse.class);

            return chatClient.prompt()
                    .user(u -> u.text("""
                    The user is asking for an analysis of these specific assets: {stocks}.
                    
                    Instructions:
                    1. Provide exactly 3 strategic insights.
                    2. Generate realistic price predictions.
                    3. Provide a Portfolio Health Score (1-10).
                    
                    Return ONLY valid JSON:
                    {format}
                    """)
                            .param("stocks", stocks)
                            .param("format", converter.getFormat()))
                    .call()
                    .entity(AIResponse.class);

        } catch (Exception e) {

            // Prevent app crash when Gemini rate limit hits
            throw new RuntimeException("AI service temporarily unavailable. Please try again later.");
        }
    }
}