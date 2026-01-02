package com.fitness.AiService.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@Slf4j
public class GeminiService {
    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public GeminiService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    // This runs when the app starts to verify your key is loaded
    @PostConstruct
    public void init() {
        if (geminiApiKey == null || geminiApiKey.isEmpty()) {
            log.error("❌ GEMINI API KEY IS MISSING OR NULL!");
        } else {
            log.info("✅ Gemini Service initialized. Key starts with: {}", geminiApiKey.substring(0, Math.min(5, geminiApiKey.length())) + "...");
        }
    }


// ... inside the class ...

    public String getAnswer(String prompt) {
        // 1. Construct the request body
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        // 2. Build the URL String manually
        String cleanBaseUrl = geminiApiUrl.trim();
        if (cleanBaseUrl.endsWith("?")) {
            cleanBaseUrl = cleanBaseUrl.substring(0, cleanBaseUrl.length() - 1);
        }

        // 2. CONSTRUCT: Add the key with a SINGLE '?'
        String finalUrlString = cleanBaseUrl + "?key=" + geminiApiKey;

        // 3. URI OBJECT: Use URI.create to prevent double-encoding
        java.net.URI finalUri = java.net.URI.create(finalUrlString);

        return webClient.post()
                .uri(finalUri)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                // ... (rest of your error handling) ...
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}