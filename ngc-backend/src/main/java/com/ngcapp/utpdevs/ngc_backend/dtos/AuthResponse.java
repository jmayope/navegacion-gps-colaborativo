package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.UUID;

public class AuthResponse {
    
    private UUID id;
    private String email;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("is_verified")
    private Boolean isVerified;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("is_admin")
    private Boolean isAdmin;
    private String token;
    @JsonProperty("token_type")
    private String tokenType = "Bearer";
    @JsonProperty("expires_at")
    private OffsetDateTime expiresAt;
    private String message;
    private boolean success;
    
    // Constructor para éxito
    public AuthResponse(UUID id, String email, String fullName, 
                        Boolean isVerified, Boolean isActive, Boolean isAdmin, String token, 
                        OffsetDateTime expiresAt, String message) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.isVerified = isVerified;
        this.isActive = isActive;
        this.isAdmin = isAdmin;
        this.token = token;
        this.expiresAt = expiresAt;
        this.message = message;
        this.success = true;
    }
    
    // Constructor para error
    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public Boolean getIsVerified() {
        return isVerified;
    }
    
    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }
    
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}