package com.observability;

import com.observability.controller.UserController;
import com.observability.model.User;
import com.observability.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit Test per UserController
 * 
 * Usa JUnit 5 e Mockito per testare:
 * - Creazione di utenti (CREATE)
 * - Recupero di utenti (READ)
 * - Aggiornamento di utenti (UPDATE)
 * - Eliminazione di utenti (DELETE)
 * 
 * @author Valentina Nappi
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Unit Tests")
public class UserControllerTest {

    @Mock
    private ProducerTemplate producerTemplate;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Preparazione dati di test
        testUser = User.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .password("securepassword123")
            .role("USER")
            .active(true)
            .build();

        // Mock del producer template
        doNothing().when(producerTemplate).sendBody(anyString(), anyString());
    }

    @Test
    @DisplayName("Dovrebbe creare un nuovo utente con successo")
    void testCreateUser_Success() {
        // Arrange
        User newUser = User.builder()
            .username("newuser")
            .email("newuser@example.com")
            .password("password123")
            .role("USER")
            .active(true)
            .build();

        // Act
        ResponseEntity<User> response = userController.createUser(newUser);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("newuser");
        assertThat(response.getBody().getPassword()).isNotBlank();
        assertThat(response.getBody().getId()).isNotNull();
        
        // Verifica che Camel sia stato chiamato
        verify(producerTemplate, times(1)).sendBody(anyString(), anyString());
        log.info("✓ Test: createUser passed");
    }

    @Test
    @DisplayName("Dovrebbe recuperare tutti gli utenti")
    void testGetAllUsers_Success() {
        // Arrange
        userController.createUser(testUser);

        // Act
        ResponseEntity<java.util.List<User>> response = userController.getAllUsers();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        log.info("✓ Test: getAllUsers passed - Retrieved {} users", response.getBody().size());
    }

    @Test
    @DisplayName("Dovrebbe recuperare un utente per ID")
    void testGetUserById_Success() {
        // Arrange
        ResponseEntity<User> createResponse = userController.createUser(testUser);
        Long userId = createResponse.getBody().getId();

        // Act
        ResponseEntity<User> response = userController.getUserById(userId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(userId);
        assertThat(response.getBody().getUsername()).isEqualTo(testUser.getUsername());
        log.info("✓ Test: getUserById passed");
    }

    @Test
    @DisplayName("Dovrebbe restituire 404 per utente non trovato")
    void testGetUserById_NotFound() {
        // Act
        ResponseEntity<User> response = userController.getUserById(999L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        log.info("✓ Test: getUserById (NotFound) passed");
    }

    @Test
    @DisplayName("Dovrebbe aggiornare un utente esistente")
    void testUpdateUser_Success() {
        // Arrange
        ResponseEntity<User> createResponse = userController.createUser(testUser);
        Long userId = createResponse.getBody().getId();

        User updatedUser = User.builder()
            .username("updateduser")
            .email("updated@example.com")
            .password("newpassword123")
            .build();

        // Act
        ResponseEntity<User> response = userController.updateUser(userId, updatedUser);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("updateduser");
        assertThat(response.getBody().getEmail()).isEqualTo("updated@example.com");
        
        // Verifica che Camel sia stato chiamato per l'operazione UPDATE
        verify(producerTemplate, atLeast(2)).sendBody(anyString(), anyString());
        log.info("✓ Test: updateUser passed");
    }

    @Test
    @DisplayName("Dovrebbe restituire 404 quando si aggiorna utente non trovato")
    void testUpdateUser_NotFound() {
        // Arrange
        User updatedUser = User.builder()
            .username("updateduser")
            .email("updated@example.com")
            .password("newpassword123")
            .build();

        // Act
        ResponseEntity<User> response = userController.updateUser(999L, updatedUser);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        log.info("✓ Test: updateUser (NotFound) passed");
    }

    @Test
    @DisplayName("Dovrebbe eliminare un utente")
    void testDeleteUser_Success() {
        // Arrange
        ResponseEntity<User> createResponse = userController.createUser(testUser);
        Long userId = createResponse.getBody().getId();

        // Act
        ResponseEntity<Void> response = userController.deleteUser(userId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        
        // Verifica che l'utente sia stato eliminato
        ResponseEntity<User> getResponse = userController.getUserById(userId);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        
        log.info("✓ Test: deleteUser passed");
    }

    @Test
    @DisplayName("Dovrebbe restituire 404 quando si elimina utente non trovato")
    void testDeleteUser_NotFound() {
        // Act
        ResponseEntity<Void> response = userController.deleteUser(999L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        log.info("✓ Test: deleteUser (NotFound) passed");
    }

    @Test
    @DisplayName("Dovrebbe verificare lo health check")
    void testHealthCheck_Success() {
        // Act
        ResponseEntity<java.util.Map<String, Object>> response = userController.healthCheck();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo("UP");
        assertThat(response.getBody().get("database")).isEqualTo("CONNECTED");
        log.info("✓ Test: healthCheck passed");
    }
}
