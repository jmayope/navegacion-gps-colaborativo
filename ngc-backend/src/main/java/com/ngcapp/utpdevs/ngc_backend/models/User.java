package com.ngcapp.utpdevs.ngc_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class User {
    
    private UUID id;
    
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
    
    @JsonProperty("phone")
    private String phone;
    
    @JsonProperty("full_name")
    private String fullName;
    
    @JsonProperty("document_type")
    private String documentType; // 'DNI', 'CE', 'PASAPORTE'
    
    @JsonProperty("document_number")
    private String documentNumber;
    
    @JsonProperty("is_active")
    private Boolean isActive = true;
    
    @JsonProperty("is_verified")
    private Boolean isVerified = false;
    
    @JsonProperty("last_location_lat")
    private BigDecimal lastLocationLat;
    
    @JsonProperty("last_location_lng")
    private BigDecimal lastLocationLng;
    
    @JsonProperty("last_activity_at")
    private OffsetDateTime lastActivityAt;
    
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    
    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;

    @JsonProperty("is_admin")
    private Boolean isAdmin = false;
    
    // ============================================================
    // CONSTRUCTORES
    // ============================================================
    
    public User() {}
    
    public User(UUID id, String email, String phone, String fullName) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.fullName = fullName;
        this.isActive = true;
        this.isVerified = false;
    }
    
    // ============================================================
    // GETTERS Y SETTERS
    // ============================================================
    
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

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getDocumentType() {
        return documentType;
    }
    
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    
    public String getDocumentNumber() {
        return documentNumber;
    }
    
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Boolean getIsVerified() {
        return isVerified;
    }
    
    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
    
    public BigDecimal getLastLocationLat() {
        return lastLocationLat;
    }
    
    public void setLastLocationLat(BigDecimal lastLocationLat) {
        this.lastLocationLat = lastLocationLat;
    }
    
    public BigDecimal getLastLocationLng() {
        return lastLocationLng;
    }
    
    public void setLastLocationLng(BigDecimal lastLocationLng) {
        this.lastLocationLng = lastLocationLng;
    }
    
    public OffsetDateTime getLastActivityAt() {
        return lastActivityAt;
    }
    
    public void setLastActivityAt(OffsetDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }
    
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }
    
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    // ============================================================
    // MÉTODOS ÚTILES
    // ============================================================
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", fullName='" + fullName + '\'' +
                ", isActive=" + isActive +
                ", isVerified=" + isVerified +
                '}';
    }
}