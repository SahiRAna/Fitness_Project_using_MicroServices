package com.fitness.ActivityService.controller;

import com.fitness.ActivityService.dto.ActivityRequest;
import com.fitness.ActivityService.dto.ActivityResponse;
import com.fitness.ActivityService.repository.ActivityRepository;
import com.fitness.ActivityService.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing activities.
 * Provides endpoints for tracking, retrieving, and managing user activities.
 */
@RestController
@RequestMapping("api/activities")
@AllArgsConstructor
public class ActivityController {

    private final ActivityRepository activityRepository;
    private final ActivityService activityService;

    /**
     * Endpoint to track a new activity.
     *
     * @param activityRequest The request body containing the details of the activity to be tracked.
     * @return A ResponseEntity containing the details of the tracked activity.
     */
    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest activityRequest) {
        return ResponseEntity.ok(activityService.trackActivity(activityRequest));
    }

    /**
     * Endpoint to retrieve all activities for a specific user.
     *
     * @param userId The ID of the user, passed in the "X-USER-ID" header.
     * @return A ResponseEntity containing a list of the user's activities.
     */
    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@RequestHeader("X-USER-ID") String userId) {
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }

    /**
     * Endpoint to retrieve the details of a specific activity.
     *
     * @param activityId The ID of the activity to retrieve.
     * @return A ResponseEntity containing the details of the requested activity.
     */
    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivityDetails(@PathVariable String activityId) {
        return ResponseEntity.ok(activityService.getActivityDetails(activityId));
    }
}
