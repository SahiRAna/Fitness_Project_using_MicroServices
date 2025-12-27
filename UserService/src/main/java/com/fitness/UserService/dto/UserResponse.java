package com.fitness.UserService.dto;

import com.fitness.UserService.model.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Data
public class UserResponse {
    private String userId;

    private String email;


    private String password;

    private String firstName;
    private String lastName;


    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;
}

