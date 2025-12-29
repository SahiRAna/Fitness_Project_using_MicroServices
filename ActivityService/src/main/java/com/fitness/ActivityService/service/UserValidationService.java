package com.fitness.ActivityService.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@AllArgsConstructor
@Service
public class UserValidationService {
    private final WebClient userServiceWebClient;

    public Boolean userValidation(String userId){
        try {
            return userServiceWebClient.get()
                    .uri("api/users/{userId}/validation" ,userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        }catch (WebClientResponseException e){
            if (e.getStatusCode()== HttpStatus.NOT_FOUND){
             throw new RuntimeException("User Not Found");
            }
            else if (e.getStatusCode()==HttpStatus.BAD_REQUEST){
                throw new RuntimeException("Invalid Request");
            }
        }
        return null;
    }
}
