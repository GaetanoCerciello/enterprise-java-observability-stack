package com.observability.model;

import lombok.*;

import java.util.Date;

/**
 * Entità User per operazioni CRUD simulate
 * 
 * Rappresenta un utente nel sistema base di dati simulato
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;
    private Date createdAt;
    private Date updatedAt;
    private boolean active;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", active=" + active +
                ", createdAt=" + createdAt +
                '}';
    }
}
