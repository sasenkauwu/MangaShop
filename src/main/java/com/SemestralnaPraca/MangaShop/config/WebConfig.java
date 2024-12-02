package com.SemestralnaPraca.MangaShop.config;

import jakarta.servlet.http.HttpSessionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");  // Prispôsob podľa potreby


    }

    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionEventPublisher();
    }
}