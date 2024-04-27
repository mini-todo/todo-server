package com.example.todoproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:8081",
                        "http://localhost:9090",
                        "http://3.36.153.201",
                        "http://3.36.153.201:8081",
                        "http://3.36.153.201:80",
                        "http://3.36.153.201:3000",
                        "http://3.36.153.201:9090",
                        "https://jobava.online",
                        "https://jobava.online:3000",
                        "http://jobava.online"
                )
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS" , "PATCH")
                .exposedHeaders("Authorization", "RefreshToken");
    }
}
