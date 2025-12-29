package com.fitness.ActivityService.dto;

import com.fitness.ActivityService.model.ActivityType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Data Transfer Object (DTO) for sending activity data to clients.
 * This class encapsulates the activity details that are exposed through the API.
 */
@Data
public class ActivityResponse {

    /**
     * The unique identifier for the activity.
     */
    private String id;

    /**
     * The ID of the user who performed the activity.
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

    /**
     * The timestamp when the activity record was created.
     */
    private LocalDateTime startedTime;

    /**
     * The timestamp when the activity record was last updated.
     */
    private LocalDateTime updatedTime;
}
