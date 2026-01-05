package com.fitness.AiService.service;

import com.fitness.AiService.model.Activity;
import com.fitness.AiService.model.Recommendations;
import com.fitness.AiService.repository.RecommendationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {
    private  final  ActivityAiService activityAiService;
    private final RecommendationsRepository recommendationsRepository;

    @RabbitListener(queues = "activity.queue")
    public void ActivityListener(Activity activity) {
        log.info(" Received activity for processing :{} ", activity.getId());
        log.info(" Generated Recommendations :{} ",
                activityAiService.generateRecommendations(activity));
        Recommendations recommendations = activityAiService.generateRecommendations(activity);
        recommendationsRepository.save(recommendations);

        }
}
