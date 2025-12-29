package com.fitness.UserService.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for sending user data to clients.
 * This class encapsulates the user details that are exposed through the API,
 * omitting sensitive information like the password.
 */
@Data
public class UserResponse {

    /**
     * The unique identifier for the user.
     */
    private String userId;

    /**
     * The user's email address.
     */
    private String email;

    /**
     * The user's first name.
     */
    private String firstName;

    /**
     * The user's last name.
     */
    private String lastName;

    /**
     * The timestamp when the user account was created.
     */
    private LocalDateTime createdAt;

    /**
     * The timestamp when the user account was last updated.
     */
    private LocalDateTime updatedAt;
}
