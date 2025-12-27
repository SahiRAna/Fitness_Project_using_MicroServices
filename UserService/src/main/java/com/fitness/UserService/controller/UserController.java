package com.fitness.UserService.controller;

import com.fitness.UserService.dto.RegisterRequest;
import com.fitness.UserService.dto.UserResponse;
import com.fitness.UserService.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.beans.JavaBean;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    public UserService userService;
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse>  getUserName(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }
    @GetMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request ){
        return ResponseEntity.ok(userService.register(request));
    }
}
