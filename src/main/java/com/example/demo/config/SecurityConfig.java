package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.user.CustomUserDetailsService;

@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var service = new CustomUserDetailsService(userRepository, roleRepository, passwordEncoder());
        service.registerUser("user", "password");

        return service;
    }

    @Bean
    public SecurityFilterChain formLoginFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(login -> login.loginProcessingUrl("/login").defaultSuccessUrl("/")
                .failureUrl("/login?error").permitAll())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

        return httpSecurity.build();
    }
}
