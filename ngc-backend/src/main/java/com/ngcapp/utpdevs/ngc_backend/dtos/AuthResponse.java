package com.ngcapp.utpdevs.ngc_backend.dtos;

import java.util.UUID;

public class AuthResponse {
    
    private UUID id;
    private String email;
    private String fullName;
    private String token; // Para JWT (opcional)
    private String message;
    
    // Constructor
    public AuthResponse(UUID id, String email, String fullName, String message) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.message = message;
    }
    
    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}