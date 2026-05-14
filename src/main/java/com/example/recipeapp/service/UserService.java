package com.example.recipeapp.service;

import com.example.recipeapp.domain.User;
import com.example.recipeapp.dto.UserInDto;
import com.example.recipeapp.dto.UserOutDto;
import com.example.recipeapp.exception.UserNotFoundException;
import com.example.recipeapp.repository.UserRepository;
import com.example.recipeapp.dto.JwtResponseDto;
import com.example.recipeapp.dto.LoginRequestDto;
import com.example.recipeapp.security.JwtUtils;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserService(UserRepository userRepository,
                       ModelMapper modelMapper,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public UserOutDto create(UserInDto userInDto) {
        if (userRepository.existsByUsername(userInDto.username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(userInDto.email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(userInDto.username);
        user.setPassword(passwordEncoder.encode(userInDto.password));
        user.setEmail(userInDto.email);
        user.setRole("USER");

        User savedUser = userRepository.save(user);

        return userOutDto(savedUser);
    }

    public List<UserOutDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::userOutDto)
                .toList();
    }

    public UserOutDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return userOutDto(user);
    }

    private UserOutDto userOutDto(User user) {
        return modelMapper.map(user, UserOutDto.class);
    }

    public JwtResponseDto authenticate(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.username);

        if (user == null) {
            throw new UserNotFoundException("Invalid username or password");
        }

        if (!passwordEncoder.matches(loginRequestDto.password, user.getPassword())) {
            throw new UserNotFoundException("Invalid username or password");
        }

        String token = jwtUtils.generateJwtToken(user);

        return new JwtResponseDto(token, user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}