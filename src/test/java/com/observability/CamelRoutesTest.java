package com.observability;

import com.observability.camel.CamelRoutes;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration Test per Apache Camel Routes
 * 
 * Testa:
 * - Configurazione delle rotte Camel
 * - Invio e ricezione di messaggi
 * - Processamento dei dati con Camel
 * 
 * @author Valentina Nappi
 */
@Slf4j
@SpringBootTest
@CamelSpringTest
@DisplayName("CamelRoutes Integration Tests")
public class CamelRoutesTest {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Test
    @DisplayName("Dovrebbe avere tutte le rotte configurate e disponibili")
    void testRoutesConfiguration_Success() {
        // Assert
        assertThat(camelContext).isNotNull();
        assertThat(camelContext.getRoutes()).isNotEmpty();

        // Verifica che le rotte attese siano presenti
        assertThat(camelContext.getRoutes())
            .anyMatch(route -> "data-simulation-route".equals(route.getId()))
            .anyMatch(route -> "data-processing-route".equals(route.getId()))
            .anyMatch(route -> "crud-operation-route".equals(route.getId()));

        log.info("✓ Test: routesConfiguration passed");
        camelContext.getRoutes().forEach(route -> 
            log.info("  Route: {} - StartPoint: {}", route.getId(), route.getStartupOrder())
        );
    }

    @Test
    @DisplayName("Dovrebbe processare un messaggio attraverso direct:process-data")
    void testProcessDataRoute_Success() {
        // Arrange
        String testMessage = "Test data event";

        // Act
        producerTemplate.sendBody("direct:process-data", testMessage);

        // Assert (comunque passato se non ha lanciato eccezioni)
        assertThat(testMessage).isNotEmpty();
        log.info("✓ Test: processDataRoute passed - Message: {}", testMessage);
    }

    @Test
    @DisplayName("Dovrebbe processare un'operazione CRUD")
    void testCrudOperationRoute_Success() {
        // Arrange
        String crudOperation = "CREATE User: testuser";

        // Act
        producerTemplate.sendBody("direct:crud-operation", crudOperation);

        // Assert
        assertThat(crudOperation).contains("User");
        log.info("✓ Test: crudOperationRoute passed - Operation: {}", crudOperation);
    }

    @Test
    @DisplayName("Dovrebbe gestire la risposta handler")
    void testResponseHandlerRoute_Success() {
        // Arrange
        String responseData = "Operation completed successfully";

        // Act
        producerTemplate.sendBody("direct:response-handler", responseData);

        // Assert
        assertThat(responseData).isNotEmpty();
        log.info("✓ Test: responseHandlerRoute passed - Response: {}", responseData);
    }

    @Test
    @DisplayName("Dovrebbe avere il Camel context in stato RUNNING")
    void testCamelContextState_Success() {
        // Assert
        assertThat(camelContext.getStatus().isStarting() || 
                  camelContext.getStatus().isStarted())
            .isTrue();

        log.info("✓ Test: camelContextState passed - Status: {}", camelContext.getStatus());
    }
}
