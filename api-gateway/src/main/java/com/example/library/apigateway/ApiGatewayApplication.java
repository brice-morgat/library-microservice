package com.example.library.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application principale de l'API Gateway.
 *
 * <p>Démarre Spring Cloud Gateway et expose les routes définies dans la configuration.</p>
 *
 * @since 1.0
 */
@SpringBootApplication
public class ApiGatewayApplication {
    /**
     * Point d'entrée de l'application.
     *
     * @param args arguments de ligne de commande.
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
