package com.fitness.UserService.service;

import com.fitness.UserService.dto.RegisterRequest;
import com.fitness.UserService.dto.UserResponse;
import com.fitness.UserService.model.User;
import com.fitness.UserService.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service class for handling business logic related to users.
 * This class is responsible for user registration, profile retrieval, and validation.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    /**
     * Registers a new user.
     *
     * @param request The request object containing user registration details.
     * @return A UserResponse object with details of the newly created user.
     * @throws RuntimeException if a user with the given email already exists.
     */
    public UserResponse register(@Valid RegisterRequest request) {
        // Check if a user with the provided email already exists.
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create a new User entity from the registration request.
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // Note: Passwords should be hashed in a real application.
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        // Save the new user to the repository.
        User savedUser = repository.save(user);

        // Map the saved user entity to a response object and return it.
        return mapToUserResponse(savedUser);
    }

    /**
     * Retrieves a user's profile by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return A UserResponse object with the user's profile information.
     * @throws RuntimeException if no user is found with the given ID.
     */
    public UserResponse getUserProfile(String userId) {
        // Find the user by ID or throw an exception if not found.
        User user = repository.findById(userId).orElseThrow(
                () -> new RuntimeException("User Not Found")
        );
        // Map the user entity to a response object and return it.
        return mapToUserResponse(user);
    }

    /**
     * Checks if a user exists by their ID.
     *
     * @param userId The ID of the user to check.
     * @return A boolean value indicating whether the user exists.
     */
    public Boolean existBYUserId(String userId) {
        log.info("Calling User Validation API for user ID {}", userId);
        return repository.existsById(userId);
    }

    /**
     * Converts a User entity to a UserResponse DTO.
     *
     * @param user The User entity to be converted.
     * @return A UserResponse DTO.
     */
    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }
}
