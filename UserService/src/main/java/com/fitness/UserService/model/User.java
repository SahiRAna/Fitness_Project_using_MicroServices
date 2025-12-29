package com.fitness.UserService.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a user entity in the database.
 * This class is mapped to the "users" table.
 */
@Entity
@Table(name = "users")
@Data
public class User {

    /**
     * The unique identifier for the user.
     * It is generated automatically as a UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    /**
     * The user's email address.
     * It must be unique and cannot be null.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * The user's password.
     * It cannot be null.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The user's first name.
     */
    private String firstName;

    /**
     * The user's last name.
     */
    private String lastName;

    /**
     * The role of the user (e.g., USER, ADMIN).
     * Defaults to USER.
     */
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    /**
     * The timestamp when the user account was created.
     * This is automatically set on creation.
     */
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * The timestamp when the user account was last updated.
     * This is automatically set on update.
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
