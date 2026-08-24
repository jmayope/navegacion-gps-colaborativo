package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.UUID;

public class IncidentRequest {
    
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
    
    @JsonProperty("report_photo_url")
    private String reportPhotoUrl;
    
    // Getters y Setters
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
    
    public String getReportPhotoUrl() {
        return reportPhotoUrl;
    }
    
    public void setReportPhotoUrl(String reportPhotoUrl) {
        this.reportPhotoUrl = reportPhotoUrl;
    }
}