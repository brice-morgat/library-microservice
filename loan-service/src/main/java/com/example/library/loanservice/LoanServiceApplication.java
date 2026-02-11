package com.example.library.loanservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Application principale du Loan Service.
 *
 * @since 1.0
 */
@SpringBootApplication
@EnableFeignClients
public class LoanServiceApplication {
    /**
     * Point d'entrée de l'application.
     *
     * @param args arguments de ligne de commande.
     */
    public static void main(String[] args) {
        SpringApplication.run(LoanServiceApplication.class, args);
    }
}
