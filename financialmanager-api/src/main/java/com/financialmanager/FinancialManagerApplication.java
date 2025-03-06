package com.financialmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the Financial Manager API
 * Handles initialization of the Spring Boot application with JPA auditing and scheduling enabled
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class FinancialManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialManagerApplication.class, args);
    }
}