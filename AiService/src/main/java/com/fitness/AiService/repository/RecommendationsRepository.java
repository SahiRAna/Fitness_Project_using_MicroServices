package com.fitness.AiService.repository;

import com.fitness.AiService.model.Recommendations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for handling CRUD operations for recommendations.
 * This repository extends MongoRepository to provide MongoDB-specific data access.
 */
@Repository
public interface RecommendationsRepository extends MongoRepository<Recommendations,String> {

    /**
     * Finds a list of recommendations for a specific user.
     *
     * @param userId The ID of the user to find recommendations for.
     * @return A list of recommendations for the user.
     */
    List<Recommendations> findByUserId(String userId);

    /**
     * Finds recommendations for a specific activity.
     *
     * @param activityId The ID of the activity to find recommendations for.
     * @return An Optional containing the recommendations for the activity, or empty if not found.
     */
    Optional<Recommendations> findByActivityId(String activityId);
}
