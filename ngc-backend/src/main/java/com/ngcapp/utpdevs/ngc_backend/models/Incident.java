package com.ngcapp.utpdevs.ngc_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Incident {
  
  private UUID id;
  
  @JsonProperty("route_id")
  private UUID routeId;
  
  @JsonProperty("segment_id")
  private UUID segmentId;
  
  @JsonProperty("user_id")
  private UUID userId;
  
  @JsonProperty("incident_type")
  private String incidentType;
  
  @JsonProperty("severity")
  private String severity;
  
  private String description;
  
  @JsonProperty("location_lat")
  private BigDecimal locationLat;
  
  @JsonProperty("location_lng")
  private BigDecimal locationLng;
  
  @JsonProperty("location_address")
  private String locationAddress;
  
  @JsonProperty("is_panic")
  private Boolean isPanic = false;
  
  @JsonProperty("is_resolved")
  private Boolean isResolved = false;
  
  @JsonProperty("resolved_at")
  private OffsetDateTime resolvedAt;
  
  @JsonProperty("report_photo_url")
  private String reportPhotoUrl;
  
  @JsonProperty("created_at")
  private OffsetDateTime createdAt;
  
  @JsonProperty("updated_at")
  private OffsetDateTime updatedAt;
  
  // Getters y Setters
  public UUID getId() {
      return id;
  }
  
  public void setId(UUID id) {
      this.id = id;
  }
  
  public UUID getRouteId() {
      return routeId;
  }
  
  public void setRouteId(UUID routeId) {
      this.routeId = routeId;
  }
  
  public UUID getSegmentId() {
      return segmentId;
  }
  
  public void setSegmentId(UUID segmentId) {
      this.segmentId = segmentId;
  }
  
  public UUID getUserId() {
      return userId;
  }
  
  public void setUserId(UUID userId) {
      this.userId = userId;
  }
  
  public String getIncidentType() {
      return incidentType;
  }
  
  public void setIncidentType(String incidentType) {
      this.incidentType = incidentType;
  }
  
  public String getSeverity() {
      return severity;
  }
  
  public void setSeverity(String severity) {
      this.severity = severity;
  }
  
  public String getDescription() {
      return description;
  }
  
  public void setDescription(String description) {
      this.description = description;
  }
  
  public BigDecimal getLocationLat() {
      return locationLat;
  }
  
  public void setLocationLat(BigDecimal locationLat) {
      this.locationLat = locationLat;
  }
  
  public BigDecimal getLocationLng() {
      return locationLng;
  }
  
  public void setLocationLng(BigDecimal locationLng) {
      this.locationLng = locationLng;
  }
  
  public String getLocationAddress() {
      return locationAddress;
  }
  
  public void setLocationAddress(String locationAddress) {
      this.locationAddress = locationAddress;
  }
  
  public Boolean getIsPanic() {
      return isPanic;
  }
  
  public void setIsPanic(Boolean isPanic) {
      this.isPanic = isPanic;
  }
  
  public Boolean getIsResolved() {
      return isResolved;
  }
  
  public void setIsResolved(Boolean isResolved) {
      this.isResolved = isResolved;
  }
  
  public OffsetDateTime getResolvedAt() {
      return resolvedAt;
  }
  
  public void setResolvedAt(OffsetDateTime resolvedAt) {
      this.resolvedAt = resolvedAt;
  }
  
  public String getReportPhotoUrl() {
      return reportPhotoUrl;
  }
  
  public void setReportPhotoUrl(String reportPhotoUrl) {
      this.reportPhotoUrl = reportPhotoUrl;
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