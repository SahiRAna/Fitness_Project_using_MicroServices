package com.fitness.ActivityService.repository;

import com.fitness.ActivityService.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityRepository extends MongoRepository<Activity,String> {

    List<Activity> findByUserId(String userId);
}
