package com.fitness.AiService.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.AiService.model.Activity;
import com.fitness.AiService.model.Recommendations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {
    public final GeminiService geminiService;
    public Recommendations generateRecommendations(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("Response from Ai:{}",aiResponse);
        return processAiResponse(activity,aiResponse);


    }
    private Recommendations processAiResponse(Activity activity ,String aiResponse){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(aiResponse);
            JsonNode nodePath = jsonNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");
            String jsonContent = nodePath
                    .asText()
                    .replaceAll("```json\\n","")
                    .replaceAll("\\n```","")
                    .trim();
//            log.info("Parsed Data from the Ai Response:{}",jsonContent);
            JsonNode analysisJson = objectMapper
                    .readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysis(fullAnalysis,analysisNode,"overall","Overall:");
            addAnalysis(fullAnalysis,analysisNode,"pace","Pace:");
            addAnalysis(fullAnalysis,analysisNode,"heartRate","Heart Rate:");
            addAnalysis(fullAnalysis,analysisNode,"caloriesBurned","Calories Burned:");

            List<String> improvement = extractImprovements(
                    analysisJson.path("improvements"));
            List<String> suggestion = extractSuggestions(
                    analysisJson.path("suggestions"));
            List<String> safety = extractSafety(
                    analysisJson.path("safety"));
            return Recommendations.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getActivityType())
                    .recommendations(fullAnalysis.toString().trim())
                    .improvements(improvement)
                    .suggestions(suggestion)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .build();
        }catch (Exception e){

            e.printStackTrace();
            return createDefaultRecommendations(activity);
        }
    }

    private Recommendations createDefaultRecommendations(Activity activity) {
        return Recommendations.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getActivityType())
                .recommendations("Unable to generate Detailed Recommendations")
                .improvements(Collections.singletonList("Continue the regular Workout"))
                .suggestions(Collections.singletonList("Contact the Pediatrician"))
                .safety(Arrays.asList("safety",
                        "hydration"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafety(JsonNode safetyNode) {
        List<String> safety= new ArrayList<>();
        if (safetyNode.isArray()){
            safetyNode.forEach(safe ->{
                safety.add(safe.asText());
            });
        }
        return safety.isEmpty()?
                Collections.singletonList("No Specific Safety required"): safety;

    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if (suggestionsNode.isArray()){
            suggestionsNode.forEach(suggestion ->{
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(String.format("%s:%s",workout,description));
            });
        }
        return suggestions.isEmpty()?
                Collections.singletonList("No Specific Suggestions required"): suggestions;

    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if (improvementsNode.isArray()){
            improvementsNode.forEach(improvement ->{
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvements.add(String.format("%s:%s",area,detail));
            });
        }
        return improvements.isEmpty()?
            Collections.singletonList("No Specific Improvements required"): improvements;

    }

    private void addAnalysis(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
        {
          "analysis": {
            "overall": "Overall analysis here",
            "pace": "Pace analysis here",
            "heartRate": "Heart rate analysis here",
            "caloriesBurned": "Calories analysis here"
          },
          "improvements": [
            {
              "area": "Area name",
              "recommendation": "Detailed recommendation"
            }
          ],
          "suggestions": [
            {
              "workout": "Workout name",
              "description": "Detailed workout description"
            }
          ],
          "safety": [
            "Safety point 1",
            "Safety point 2"
          ]
        }

        Analyze this activity:
        Activity Type: %s
        Duration: %d minutes
        Calories Burned: %d
        Additional Metrics: %s
        
        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
        Ensure the response follows the EXACT JSON format shown above.
        """,
                activity.getActivityType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }
}
