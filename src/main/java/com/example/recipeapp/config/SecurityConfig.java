package com.example.recipeapp.config;

import com.example.recipeapp.security.AuthTokenFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.recipeapp.security.AuthEntryPointJwt;
import com.example.recipeapp.security.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthTokenFilter authTokenFilter;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(AuthTokenFilter authTokenFilter,
                          AuthEntryPointJwt authEntryPointJwt,
                          CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.authTokenFilter = authTokenFilter;
        this.authEntryPointJwt = authEntryPointJwt;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .cors(cors -> {})
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authEntryPointJwt)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()

                        .requestMatchers(HttpMethod.GET, "/recipes/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ingredients/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/images/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/recipes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/recipes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/recipes/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/ingredients").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/ingredients/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/ingredients/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/images").hasRole("ADMIN")

                        .anyRequest().authenticated()
                );

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}