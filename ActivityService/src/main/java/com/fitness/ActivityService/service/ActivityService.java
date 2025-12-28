package com.fitness.ActivityService.service;

import com.fitness.ActivityService.dto.ActivityRequest;
import com.fitness.ActivityService.dto.ActivityResponse;
import com.fitness.ActivityService.model.Activity;
import com.fitness.ActivityService.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private  final ActivityRepository activityRepository;
    public  ActivityResponse trackActivity(ActivityRequest activityRequest) {
        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .activityType(activityRequest.getActivityType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .startTime(activityRequest.getStartTime()).build();
        Activity savedActivity = activityRepository.save(activity);
        return  mapToActivity(savedActivity);
    }
//    function to map the activity into activity response
    private ActivityResponse mapToActivity(Activity activity){
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
//function to get the List of activities a user will perform
    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
//        first we change the list into stream then we map<means all data will be changed into activity response
//        through the function created then it will be collected back to list and return
        return activities.stream().map(this::mapToActivity).collect(Collectors.toList());
    }

    public ActivityResponse getActivityDetails(String activityId) {
      return   activityRepository.findById(activityId).map(this::mapToActivity).orElseThrow(
              ()-> new RuntimeException("Activity not found by this  id:"+ activityId)
      );
    }
}
