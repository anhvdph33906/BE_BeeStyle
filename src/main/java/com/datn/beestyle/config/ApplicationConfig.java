package com.datn.beestyle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public AuditorAware<?> auditorAware() {
        return new AuditAwareImpl<>();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
