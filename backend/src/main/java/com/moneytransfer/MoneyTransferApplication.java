package com.moneytransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Money Transfer application.
 * This application is a Spring Boot implementation of a personal finance management system.
 * 
 * @author AI Developer
 */
@SpringBootApplication
public class MoneyTransferApplication {

    /**
     * Main method that starts the Spring Boot application
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(MoneyTransferApplication.class, args);
    }
}