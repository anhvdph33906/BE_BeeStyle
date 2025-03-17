package com.datn.beestyle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableScheduling
public class BeBeestyleSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(BeBeestyleSpringBootApplication.class, args);
    }

}
