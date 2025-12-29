package com.fitness.AiService.service;

import com.fitness.AiService.model.Recommendations;
import com.fitness.AiService.repository.RecommendationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for handling business logic related to recommendations.
 * This service provides methods for retrieving recommendations from the repository.
 */
@Service
@RequiredArgsConstructor
public class RecommendationsService {

    /**
     * The repository that handles recommendation data.
     */
    public final RecommendationsRepository recommendationsRepository;

    /**
     * Retrieves a list of recommendations for a specific user.
     *
     * @param userId The ID of the user to get recommendations for.
     * @return A list of recommendations for the user.
     */
    public  List<Recommendations> getUserRecommendations(String userId) {
        return recommendationsRepository.findByUserId(userId);
    }

    /**
     * Retrieves recommendations for a specific activity.
     *
     * @param activityId The ID of the activity to get recommendations for.
     * @return The recommendations for the activity.
     * @throws RuntimeException if no activity is found with the given ID.
     */
    public Recommendations getActivityRecommendations(String activityId){
        return recommendationsRepository.findByActivityId(activityId)
                .orElseThrow(()->new RuntimeException("NO Activity Found in this activity:"+activityId));
    }
}
