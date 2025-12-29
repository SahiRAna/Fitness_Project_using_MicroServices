package com.fitness.UserService.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for receiving user data from clients.
 * This class encapsulates the user details provided when creating or updating a user.
 */
@Data
public class UserRequest {

    /**
     * The user's email address.
     */
    private String email;

    /**
     * The user's password.
     */
    private String password;

    /**
     * The user's first name.
     */
    private String firstName;

    /**
     * The user's last name.
     */
    private String lastName;
}
