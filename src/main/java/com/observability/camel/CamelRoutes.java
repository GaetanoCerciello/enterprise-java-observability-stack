package com.observability.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Apache Camel Routes Configuration
 * 
 * Definisce rotte Camel per simulare flussi di dati realistici
 * che generano traffico monitorabile da Prometheus.
 * 
 * Rotte:
 * 1. DataSimulationRoute: Genera dati di simulazione ogni 5 secondi
 * 2. ProcessingRoute: Elabora e trasforma i dati
 */
@Component
public class CamelRoutes extends RouteBuilder {

    @Override
    public void configure() {
        
        // Rotta 1: Simulazione dati ogni 5 secondi
        from("timer:data-simulator?period=5000&fixedRate=true")
            .routeId("data-simulation-route")
            .setBody(constant("Simulated data event from Apache Camel"))
            .log("Data Simulation Event: ${body}")
            .to("direct:process-data");

        // Rotta 2: Elaborazione dati
        from("direct:process-data")
            .routeId("data-processing-route")
            .log("Processing: ${body}")
            .transform().simple("Processed: ${body}")
            .log("Processing completed: ${body}");

        // Rotta 3: Simulazione operazioni CRUD generate da REST
        from("direct:crud-operation")
            .routeId("crud-operation-route")
            .log("CRUD Operation: ${body}")
            .setHeader("ProcessingTime", constant(System.currentTimeMillis()))
            .to("direct:response-handler");

        // Rotta 4: Handler delle risposte
        from("direct:response-handler")
            .routeId("response-handler-route")
            .log("Response Handler: ${body}")
            .transform().simple("CRUD Operation Result: ${body}");
    }
}
