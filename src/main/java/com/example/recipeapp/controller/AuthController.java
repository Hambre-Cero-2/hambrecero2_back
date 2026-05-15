package com.example.recipeapp.controller;

import com.example.recipeapp.domain.User;
import com.example.recipeapp.dto.JwtResponseDto;
import com.example.recipeapp.dto.LoginRequestDto;
import com.example.recipeapp.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        JwtResponseDto jwtResponseDto = userService.authenticate(loginRequestDto);
        return ResponseEntity.ok(jwtResponseDto);
    }
}
