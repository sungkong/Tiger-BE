package com.tiger.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://localhost:3000", "https://d2j20bmrtisx8o.cloudfront.net")
                .allowCredentials(true)
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowedMethods("*")
                .maxAge(3000);
    }
}
