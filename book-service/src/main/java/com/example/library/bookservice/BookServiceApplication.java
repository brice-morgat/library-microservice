package com.example.library.bookservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application principale du Book Service.
 *
 * @since 1.0
 */
@SpringBootApplication
public class BookServiceApplication {
    /**
     * Point d'entrée de l'application.
     *
     * @param args arguments de ligne de commande.
     */
    public static void main(String[] args) {
        SpringApplication.run(BookServiceApplication.class, args);
    }
}
