package org.example.order_inventory_system.config;

import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth
                        // public pages
                        .requestMatchers("/login", "/admin-login", "/customer-login", "/register").permitAll()
                        // customer pages
                        .requestMatchers("/customer/**").hasAuthority("ROLE_CUSTOMER")
                        // admin pages
                        .requestMatchers("/customers/**", "/products/**", "/stores/**",
                                "/orders/**", "/inventory/**",
                                "/shipments/**", "/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            // redirect based on role after login
                            String role = authentication.getAuthorities()
                                    .iterator().next().getAuthority();
                            if (role.equals("ROLE_ADMIN")) {
                                response.sendRedirect("/");
                            } else {
                                response.sendRedirect("/customer/dashboard");
                            }
                        })
                        .failureUrl("/admin-login?error=true")                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }
}