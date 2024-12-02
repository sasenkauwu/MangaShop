package com.SemestralnaPraca.MangaShop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/user/register", "/api/user/login", "/api/user/users").permitAll() // Verejné endpointy
                       // .requestMatchers("/api/admin/**").hasRole("ROLE_ADMIN")
                        //.requestMatchers("/api/cart/**").authenticated()
                        .requestMatchers("/profile").authenticated()
                        .requestMatchers("/cart").authenticated()
                        .anyRequest().permitAll() // Zvyšok bez auth
                )
                .formLogin(form -> form.disable())
                .logout(logout -> logout
                        .logoutUrl("/api/user/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true) // Zruší session
                        .deleteCookies("JSESSIONID") // Odstráni session cookie
                );

        return http.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Na hashovanie hesiel
    }
}
