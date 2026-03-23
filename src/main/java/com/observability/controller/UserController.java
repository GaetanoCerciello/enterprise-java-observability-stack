package com.observability.controller;

import com.observability.model.User;
import com.observability.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * REST Controller per operazioni CRUD simulate
 * 
 * Fornisce endpoint per:
 * - CREATE: Creazione di nuovi utenti
 * - READ: Recupero dati utenti
 * - UPDATE: Aggiornamento dati
 * - DELETE: Eliminazione dati
 * 
 * Integra Apache Camel per simulare operazioni database
 * e genera metriche per Prometheus
 * 
 * @author Gaetano Cerciello
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private ProducerTemplate producerTemplate;

    private final Map<Long, User> userDatabase = Collections.synchronizedMap(new HashMap<>());
    private long userIdCounter = 1;

    /**
     * GET: Recupera tutti gli utenti
     * Simula una query database
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        long startTime = System.nanoTime();
        try {
            log.info("Retrieving all users from database");
            List<User> users = new ArrayList<>(userDatabase.values());
            return ResponseEntity.ok(users);
        } finally {
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            log.info("getAllUsers completed in {} ms", duration);
        }
    }

    /**
     * GET: Recupera un utente per ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        long startTime = System.nanoTime();
        try {
            log.info("Fetching user with ID: {}", id);
            User user = userDatabase.get(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(user);
        } finally {
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            log.info("getUserById completed in {} ms", duration);
        }
    }

    /**
     * POST: Crea nuovo utente
     * Inclusa una simulazione di processing time con Camel
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        long startTime = System.nanoTime();
        try {
            log.info("Creating new user: {}", user.getUsername());
            
            // Simula hash della password con SHA-256
            String hashedPassword = SecurityUtils.hashPasswordSHA256(user.getPassword());
            user.setPassword(hashedPassword);
            user.setId(userIdCounter++);
            user.setCreatedAt(new Date());

            // Invia a Camel per simulare elaborazione
            producerTemplate.sendBody("direct:crud-operation", 
                "CREATE User: " + user.getUsername());

            userDatabase.put(user.getId(), user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } finally {
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            log.info("createUser completed in {} ms", duration);
        }
    }

    /**
     * PUT: Aggiorna utente esistente
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        long startTime = System.nanoTime();
        try {
            log.info("Updating user with ID: {}", id);
            
            User existingUser = userDatabase.get(id);
            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }

            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(SecurityUtils.hashPasswordSHA256(updatedUser.getPassword()));

            // Invia a Camel per simulare elaborazione
            producerTemplate.sendBody("direct:crud-operation", 
                "UPDATE User: " + existingUser.getUsername());

            return ResponseEntity.ok(existingUser);
        } finally {
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            log.info("updateUser completed in {} ms", duration);
        }
    }

    /**
     * DELETE: Elimina un utente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        long startTime = System.nanoTime();
        try {
            log.info("Deleting user with ID: {}", id);
            
            User user = userDatabase.remove(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            // Invia a Camel per simulare elaborazione
            producerTemplate.sendBody("direct:crud-operation", 
                "DELETE User: " + user.getUsername());

            return ResponseEntity.noContent().build();
        } finally {
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            log.info("deleteUser completed in {} ms", duration);
        }
    }

    /**
     * POST: Endpoint di health check
     * Simula un'operazione database costosa
     */
    @PostMapping("/health-check")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        long startTime = System.nanoTime();
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "UP");
            response.put("database", "CONNECTED");
            response.put("timestamp", new Date());
            response.put("totalUsers", userDatabase.size());

            return ResponseEntity.ok(response);
        } finally {
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            log.info("healthCheck completed in {} ms", duration);
        }
    }
}
