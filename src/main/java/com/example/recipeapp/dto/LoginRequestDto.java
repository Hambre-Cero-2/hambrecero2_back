package com.example.recipeapp.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDto {

    @NotBlank(message = "Username cannot be blank")
    public String username;

    @NotBlank(message = "Password cannot be blank")
    public String password;
}
