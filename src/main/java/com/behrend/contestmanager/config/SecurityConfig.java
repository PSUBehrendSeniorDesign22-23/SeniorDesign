package com.behrend.contestmanager.config;

import com.behrend.contestmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}





   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())//security measure for CSRF attacks and excludes username/password from HTTP requests
                .authorizeRequests().antMatchers("/**").permitAll().anyRequest().authenticated()
                        //permissions need more tweaking
                        //auth.antMatchers("/DevelopmentTools").hasRole("USER");
                .and().formLogin()
                .loginPage("/Landing")
                .failureUrl("/LoginFailed")
                .and()
                .logout().permitAll()
                .and()
                .build();
    }
    
}
