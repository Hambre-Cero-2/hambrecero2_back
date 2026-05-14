package com.example.recipeapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class UserInDto {

    @NotBlank(message = "Username cannot be blank")
    public String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 4, message = "Password must be at least 4 characters")
    public String password;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email cannot be blank")
    public String email;
}
