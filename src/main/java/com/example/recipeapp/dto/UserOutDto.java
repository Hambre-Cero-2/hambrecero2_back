package com.example.recipeapp.dto;

import lombok.Data;

@Data
public class UserOutDto {

    private Long id;
    private String username;
    private String email;
    private String role;
}
