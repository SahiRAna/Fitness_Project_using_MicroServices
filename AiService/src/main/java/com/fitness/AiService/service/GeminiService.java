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
        // 1. Construct Request Body
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        // 2. Sanitize and Build URL
        String cleanBaseUrl = geminiApiUrl.trim();
        // Remove trailing '?' if present
        if (cleanBaseUrl.endsWith("?")) {
            cleanBaseUrl = cleanBaseUrl.substring(0, cleanBaseUrl.length() - 1);
        }

        // Remove trailing '/' if present (Google hates double slashes)
        if (cleanBaseUrl.endsWith("/")) {
            cleanBaseUrl = cleanBaseUrl.substring(0, cleanBaseUrl.length() - 1);
        }

        // Manually construct the final string
        String finalUrlString = cleanBaseUrl + "?key=" + geminiApiKey.trim();

        // 3. Create URI
        java.net.URI finalUri = java.net.URI.create(finalUrlString);

        // 🚨 DEBUG LOG: This will show us EXACTLY what is being sent
        log.info(">>>>>> SENDING REQUEST TO: {}", finalUri);

        return webClient.post()
                .uri(finalUri)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("============== GEMINI API ERROR ==============");
                            log.error("Status: {}", clientResponse.statusCode());
                            log.error("URL: {}", finalUri); // Log URL on error
                            log.error("Reason: {}", errorBody);
                            log.error("==============================================");
                            return Mono.error(new WebClientResponseException(
                                    clientResponse.statusCode().value(),
                                    "Gemini Error",
                                    clientResponse.headers().asHttpHeaders(),
                                    errorBody.getBytes(),
                                    null
                            ));
                        })
                )
                .bodyToMono(String.class)
                .block();
    }
}