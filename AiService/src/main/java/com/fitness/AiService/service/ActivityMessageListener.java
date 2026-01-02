package com.fitness.AiService.service;

import com.fitness.AiService.model.Activity;
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


    @RabbitListener(queues = "activity.queue")
    public void ActivityListener(Activity activity) {
        log.info(" Received activity for processing :{} ", activity.getId());
        log.info(" Generated Recommendations :{} ",
                activityAiService.generateRecommendations(activity));
    }
}
