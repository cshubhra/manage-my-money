package com.epam.managemymoney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.epam.managemymoney.repository")
@EntityScan(basePackages = "com.epam.managemymoney.model")
@EnableTransactionManagement
public class ManageMyMoneyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageMyMoneyApplication.class, args);
    }
}