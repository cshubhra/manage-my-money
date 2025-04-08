package com.ror;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RorApplication {
    public static void main(String[] args) {
        SpringApplication.run(RorApplication.class, args);
    }
}