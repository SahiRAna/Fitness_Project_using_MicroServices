package com.fitness.ActivityService.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents an activity entity in the database.
 * This class is mapped to the "activities" collection in MongoDB.
 */
@Document(collection = "activities")
@Builder
@Data
public class Activity {

    /**
     * The unique identifier for the activity.
     */
    @Id
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
     * This field is mapped to the "metrics" field in the MongoDB document.
     */
    @Field("metrics")
    private Map<String, Object> additionalMetrics;

    /**
     * The timestamp when the activity record was created.
     * This field is automatically populated with the creation date.
     */
    @CreatedDate
    private LocalDateTime startedTime;

    /**
     * The timestamp when the activity record was last updated.
     * This field is automatically updated on modification.
     */
    @LastModifiedDate
    private LocalDateTime updatedTime;
}
