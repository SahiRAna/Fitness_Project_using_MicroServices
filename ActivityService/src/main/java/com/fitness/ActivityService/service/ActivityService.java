package com.fitness.ActivityService.service;

import com.fitness.ActivityService.dto.ActivityRequest;
import com.fitness.ActivityService.dto.ActivityResponse;
import com.fitness.ActivityService.model.Activity;
import com.fitness.ActivityService.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling business logic related to activities.
 * This class is responsible for tracking new activities, retrieving user activities,
 * and fetching details of a specific activity. It also integrates with RabbitMQ
 * to publish activity data for further processing.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    // Injects the exchange name from application properties for RabbitMQ.
    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    // Injects the routing key from application properties for RabbitMQ.
    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    /**
     * Tracks a new activity after validating the user.
     * If the user is valid, it saves the activity and publishes it to RabbitMQ.
     *
     * @param activityRequest The request object containing activity details.
     * @return An ActivityResponse object with details of the tracked activity.
     * @throws RuntimeException if the user is not valid.
     */
    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        // Validate the user before tracking the activity.
        Boolean isUserValid = userValidationService.userValidation(activityRequest.getUserId());
        if (!isUserValid) {
            throw new RuntimeException("User is Not Valid " + activityRequest.getUserId());
        }

        // Build the Activity entity from the request.
        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .activityType(activityRequest.getActivityType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .startTime(activityRequest.getStartTime()).build();

        // Save the activity to the repository.
        Activity savedActivity = activityRepository.save(activity);

        // Publish the saved activity to RabbitMQ for AI processing.
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
        } catch (Exception e) {
            log.error("Problem Occurred during Work : ", e);
        }

        // Map the saved activity to a response object and return it.
        return mapToActivity(savedActivity);
    }

    /**
     * Converts an Activity entity to an ActivityResponse DTO.
     *
     * @param activity The Activity entity to be converted.
     * @return An ActivityResponse DTO.
     */
    private ActivityResponse mapToActivity(Activity activity) {
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setId(activity.getId());
        activityResponse.setUserId(activity.getUserId());
        activityResponse.setActivityType(activity.getActivityType());
        activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setCaloriesBurned(activity.getCaloriesBurned());
        activityResponse.setStartedTime(activity.getStartedTime());
        activityResponse.setStartTime(activity.getStartTime());
        activityResponse.setUpdatedTime(activity.getUpdatedTime());
        return activityResponse;
    }

    /**
     * Retrieves a list of all activities for a given user.
     *
     * @param userId The ID of the user whose activities are to be retrieved.
     * @return A list of ActivityResponse objects.
     */
    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        // Map the list of Activity entities to a list of ActivityResponse DTOs.
        return activities.stream().map(this::mapToActivity).collect(Collectors.toList());
    }

    /**
     * Retrieves the details of a specific activity by its ID.
     *
     * @param activityId The ID of the activity to be retrieved.
     * @return An ActivityResponse object with the details of the activity.
     * @throws RuntimeException if the activity is not found.
     */
    public ActivityResponse getActivityDetails(String activityId) {
        // Find the activity by ID and map it to a response, or throw an exception if not found.
        return activityRepository.findById(activityId).map(this::mapToActivity).orElseThrow(
                () -> new RuntimeException("Activity not found by this  id:" + activityId)
        );
    }
}
