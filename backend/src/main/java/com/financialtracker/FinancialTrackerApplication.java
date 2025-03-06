package com.financialtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for the Financial Tracker Spring Boot application.
 * This application is a conversion of the Ruby on Rails application to a Spring Boot backend.
 * 
 * @author AI Developer
 */
@SpringBootApplication
@EnableJpaAuditing
public class FinancialTrackerApplication {

    /**
     * Main method that starts the Spring Boot application
     * 
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(FinancialTrackerApplication.class, args);
    }
}