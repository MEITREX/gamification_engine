package de.unistuttgart.iste.meitrex.rulesengine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class adds an allowed origin to the CORS configuration, for development purposes.
 * Do not use this in production.
 */
@Configuration
@Profile("dev")
public class DevCorsConfig implements WebMvcConfigurer {

    @Value("${allowed-origin}")
    private String allowedOrigin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowedHeaders("Content-Type", "*")
                .allowCredentials(true);
    }
}