package com.example.recipeapp.controller;

import com.example.recipeapp.dto.UserInDto;
import com.example.recipeapp.dto.UserOutDto;
import com.example.recipeapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserOutDto> create(@Valid @RequestBody UserInDto userInDto) {
        UserOutDto createdUser = userService.create(userInDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}