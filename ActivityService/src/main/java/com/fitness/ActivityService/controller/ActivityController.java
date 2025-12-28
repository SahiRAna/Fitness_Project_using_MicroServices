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

@RestController
@RequestMapping("api/activities")
@AllArgsConstructor

public class ActivityController {
    private final ActivityRepository activityRepository;
    private ActivityService activityService ;
//    @GetMapping("/a")
//    public String test(){
//        return "hello";
//    }
    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest activityRequest){
        return ResponseEntity.ok(activityService.trackActivity(activityRequest));
    }
    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@RequestHeader("X-USER-ID") String userId){
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }
    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivityDetails(@PathVariable String activityId){
       return ResponseEntity.ok(activityService.getActivityDetails(activityId));
    }
}
