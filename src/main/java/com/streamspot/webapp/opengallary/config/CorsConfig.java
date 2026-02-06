package com.streamspot.webapp.opengallary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        System.out.println("In config");        // Allow only your frontend origin (update with production URL later)
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));

        // Allow all necessary HTTP methods including OPTIONS
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow necessary headers
        config.setAllowedHeaders(Arrays.asList("*"));  // allow all headers

        config.setExposedHeaders(Arrays.asList("Authorization"));

        // Allow credentials (for JWT)
        config.setAllowCredentials(true);

        // Apply config
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
