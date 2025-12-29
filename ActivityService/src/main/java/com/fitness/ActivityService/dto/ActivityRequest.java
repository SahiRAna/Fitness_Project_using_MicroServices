package com.fitness.ActivityService.dto;

import com.fitness.ActivityService.model.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Data Transfer Object (DTO) for receiving activity data from clients.
 * This class encapsulates the activity details provided when tracking a new activity.
 */
@Data
public class ActivityRequest {

    /**
     * The ID of the user performing the activity.
     */
    private String userId;

    /**
     * The type of the activity (e.g., RUNNING, CYCLING).
     */
    private ActivityType activityType;

    /**
     * The duration of the activity in minutes.
     */
    private Integer duration;

    /**
     * The number of calories burned during the activity.
     */
    private Integer caloriesBurned;

    /**
     * The start time of the activity.
     */
    private LocalDateTime startTime;

    /**
     * A map of additional metrics related to the activity (e.g., distance, pace).
     */
    private Map<String, Object> additionalMetrics;
}
