package com.ngcapp.utpdevs.ngc_backend.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class UserModel {
  @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "phone", unique = true, nullable = false)
    private String phone;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(name = "document_type")
    private String documentType; // 'DNI', 'CE', 'PASAPORTE'
    
    @Column(name = "document_number")
    private String documentNumber;
    
    @Column(name = "is_active", columnDefinition = "boolean default true")
    private Boolean isActive = true;
    
    @Column(name = "is_verified", columnDefinition = "boolean default false")
    private Boolean isVerified = false;
    
    @Column(name = "last_location_lat", precision = 10, scale = 8)
    private BigDecimal lastLocationLat;
    
    @Column(name = "last_location_lng", precision = 11, scale = 8)
    private BigDecimal lastLocationLng;
    
    @Column(name = "last_activity_at")
    private OffsetDateTime lastActivityAt;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

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

    
}
