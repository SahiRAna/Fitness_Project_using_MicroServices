package com.fitness.UserService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
//For Registering the user the data that is required to register it properly will be added here
    @NotBlank(message = "Email must not be empty")
    @Email(message = "given mail is not valid")
    private String email;
    @NotBlank(message = "Password must not be empty")
    @Size(min = 6, message = "password at least greater than 6")
    private String password;
    private  String firstname;
    private String lastName;
}
