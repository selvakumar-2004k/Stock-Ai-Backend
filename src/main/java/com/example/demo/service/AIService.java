package com.example.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AIService {

    private final ChatClient chatClient;

    // These must exactly match the keys your React code is looking for
    public record Insight(int id, String category, String title, String severity, String description, String recommendation, String impact) {}
    public record Prediction(String symbol, String name, double currentPrice, double predictedPrice, int confidence, String timeframe, String sentiment) {}
    public record AIResponse(List<Insight> insights, List<Prediction> predictions, int score) {}

    public AIService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public AIResponse getPortfolioAnalysis(String stocks) {
        // Force the AI to output JSON matching our Record
        var converter = new BeanOutputConverter<>(AIResponse.class);

        return chatClient.prompt()
                .user(u -> u.text("""
    The user is asking for an analysis of these specific assets: {stocks}.
    
    Instructions:
    1. If the input is not a stock ticker, try to identify the company.
    2. Provide 3 specific strategic insights for this selection.
    3. Generate price predictions based on current market sentiment.
    4. Provide a 'Portfolio Health Score' (1-10) for this specific combination.
    
    Return the data ONLY in JSON format:
    {format}
    """)
                        .param("stocks", stocks)
                        .param("format", converter.getFormat()))
                .call()
                .entity(AIResponse.class);
    }
}