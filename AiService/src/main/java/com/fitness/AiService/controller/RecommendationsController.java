package com.fitness.AiService.controller;

import com.fitness.AiService.model.Recommendations;
import com.fitness.AiService.service.RecommendationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for handling recommendation-related requests.
 * This controller provides endpoints for retrieving recommendations for users and activities.
 *
 * <h2>Endpoints</h2>
 * <table border="1">
 *     <tr>
 *         <th>Method</th>
 *         <th>Path</th>
 *         <th>Description</th>
 *     </tr>
 *     <tr>
 *         <td>GET</td>
 *         <td>/api/recommendations/user/{userId}</td>
 *         <td>Retrieves a list of recommendations for a specific user.</td>
 *     </tr>
 *     <tr>
 *         <td>GET</td>
 *         <td>/api/recommendations/activity/{activityId}</td>
 *         <td>Retrieves recommendations for a specific activity.</td>
 *     </tr>
 * </table>
 */
@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationsController {

    /**
     * The service that provides recommendation logic.
     */
    public  final RecommendationsService recommendationsService;

    /**
     * Retrieves a list of recommendations for a specific user.
     *
     * @param userId The ID of the user to get recommendations for.
     * @return A ResponseEntity containing a list of recommendations.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendations>> getUserRecommendations(@PathVariable String userId){
        return ResponseEntity.ok(recommendationsService.getUserRecommendations(userId));
    }

    /**
     * Retrieves recommendations for a specific activity.
     *
     * @param activityId The ID of the activity to get recommendations for.
     * @return A ResponseEntity containing the recommendations for the activity.
     */
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendations> getActivityRecommendations(@PathVariable String activityId){
        return ResponseEntity.ok(recommendationsService.getActivityRecommendations(activityId));
    }

}
