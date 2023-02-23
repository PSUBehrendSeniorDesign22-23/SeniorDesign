package com.behrend.contestmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())//security measure for CSRF attacks and excludes username/password from HTTP requests
                .authorizeRequests(auth -> {
                        auth.antMatchers("/").permitAll();
                        auth.antMatchers("/DevelopmentTools").hasRole("USER");
                }
                        )
                .formLogin()
                .loginPage("/Landing")
                .failureUrl("/LoginFailed")
                .and()
                .logout().permitAll()
                .and()
                .build();
    }
    
}