package com.currency.converter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for setting up web security and user authentication.
 *
 * <p>This class is annotated with {@link Configuration} to define beans for configuring
 * HTTP security, user details, and password encoding mechanisms for the application.</p>
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the security filter chain to manage HTTP security settings.
     *
     * <p>This method configures HTTP security settings such as setting up basic HTTP authentication.</p>
     *
     * @param http An instance of {@link HttpSecurity} used to configure HTTP security.
     * @return A {@link SecurityFilterChain} that contains the HTTP security configuration.
     * @throws Exception If an error occurs while configuring HTTP security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Configures an in-memory user details service with sample users.
     *
     * <p>This method creates two users with basic authentication: "user" and "admin". The user credentials
     * are stored in memory for simplicity, and passwords are encoded using {@link BCryptPasswordEncoder}.</p>
     *
     * @return An instance of {@link UserDetailsService} containing the configured in-memory users.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password(passwordEncoder().encode("password"))
                .build());
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .build());
        return manager;
    }

    /**
     * Provides a password encoder to encode user passwords.
     *
     * <p>This method returns an instance of {@link BCryptPasswordEncoder}, which is a strong password encoder
     * that uses the BCrypt algorithm to securely hash passwords.</p>
     *
     * @return A {@link PasswordEncoder} instance for password encoding.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
