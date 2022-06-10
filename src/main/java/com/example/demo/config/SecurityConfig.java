package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
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

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository, RoleRepository roleRepository,
            @Value("${spring.security.user.name}") String defaultUsername,
            @Value("${spring.security.user.password}") String defaultPassword,
            @Value("${spring.security.user.roles}") String... roles) {
        var userDetailsService = new CustomUserDetailsService(userRepository, roleRepository, passwordEncoder());
        userDetailsService.registerUser(defaultUsername, defaultPassword, roles);

        return userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain formLoginFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(login -> login.loginProcessingUrl("/login").defaultSuccessUrl("/")
                .failureUrl("/login?error").permitAll())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

        return httpSecurity.build();
    }
}
