package com.ngcapp.utpdevs.ngc_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Segment {
    
    private UUID id;
    
    @JsonProperty("route_id")
    private UUID routeId;
    
    @JsonProperty("sequence_number")
    private Integer sequenceNumber;
    
    @JsonProperty("start_lat")
    private BigDecimal startLat;
    
    @JsonProperty("start_lng")
    private BigDecimal startLng;
    
    @JsonProperty("end_lat")
    private BigDecimal endLat;
    
    @JsonProperty("end_lng")
    private BigDecimal endLng;
    
    @JsonProperty("distance")
    private BigDecimal distance; // en kilómetros
    
    @JsonProperty("duration")
    private Integer duration; // en minutos
    
    @JsonProperty("street_name")
    private String streetName;
    
    @JsonProperty("road_type")
    private String roadType; // 'highway', 'primary', 'secondary', 'residential'
    
    @JsonProperty("speed_limit")
    private Integer speedLimit; // en km/h
    
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
    
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }
    
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    public BigDecimal getStartLat() {
        return startLat;
    }
    
    public void setStartLat(BigDecimal startLat) {
        this.startLat = startLat;
    }
    
    public BigDecimal getStartLng() {
        return startLng;
    }
    
    public void setStartLng(BigDecimal startLng) {
        this.startLng = startLng;
    }
    
    public BigDecimal getEndLat() {
        return endLat;
    }
    
    public void setEndLat(BigDecimal endLat) {
        this.endLat = endLat;
    }
    
    public BigDecimal getEndLng() {
        return endLng;
    }
    
    public void setEndLng(BigDecimal endLng) {
        this.endLng = endLng;
    }
    
    public BigDecimal getDistance() {
        return distance;
    }
    
    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public String getStreetName() {
        return streetName;
    }
    
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
    
    public String getRoadType() {
        return roadType;
    }
    
    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }
    
    public Integer getSpeedLimit() {
        return speedLimit;
    }
    
    public void setSpeedLimit(Integer speedLimit) {
        this.speedLimit = speedLimit;
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