package com.fitness.UserService.controller;

import com.fitness.UserService.dto.RegisterRequest;
import com.fitness.UserService.dto.UserResponse;
import com.fitness.UserService.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing users.
 * Provides endpoints for user registration, profile retrieval, and validation.
 */
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Endpoint to retrieve a user's profile by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return A ResponseEntity containing the user's profile information.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserName(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    /**
     * Endpoint for registering a new user.
     *
     * @param request The request body containing the user's registration details.
     * @return A ResponseEntity containing the newly created user's profile information.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    /**
     * Endpoint to validate the existence of a user by their ID.
     * This is typically used by other services to confirm a user's validity.
     *
     * @param userId The ID of the user to validate.
     * @return A ResponseEntity containing a boolean value indicating whether the user exists.
     */
    @GetMapping("/{userId}/validation")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.existBYUserId(userId));
    }
}
