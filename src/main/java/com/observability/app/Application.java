package com.observability.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot Application Entry Point
 * 
 * Applicazione Spring Boot 3 con Java 17 per demonstrare:
 * - Integrazione con Apache Camel per simulazione flussi dati
 * - Prometheus/Micrometer per observability
 * - Spring Boot Actuator per monitoring
 * - REST controller con operazioni CRUD simulate
 * - Utility per security (SHA-256 hashing)
 * 
 * @author Valentina Nappi
 * @version 1.0.0
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.observability.app",
    "com.observability.camel",
    "com.observability.controller",
    "com.observability.util"
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("=========================================");
        System.out.println("Enterprise Java Observability Stack");
        System.out.println("Running on Java 17 with Spring Boot 3");
        System.out.println("Prometheus metrics available at: /actuator/prometheus");
        System.out.println("Health check at: /actuator/health");
        System.out.println("=========================================");
    }
}
