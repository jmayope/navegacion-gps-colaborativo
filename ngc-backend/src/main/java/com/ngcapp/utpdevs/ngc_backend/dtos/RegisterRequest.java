package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class RegisterRequest {

  private String username;

  private String email;

  private String phone;

  @JsonProperty("full_name")
  private String fullName;

  private String password;

  @JsonProperty("document_type")
  private String documentType;

  @JsonProperty("document_number")
  private String documentNumber;

  @JsonProperty("last_location_lat")
  private BigDecimal lastLocationLat;

  @JsonProperty("last_location_lng")
  private BigDecimal lastLocationLng;

  // Getters y Setters
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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
}