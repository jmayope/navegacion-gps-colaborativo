package com.ngcapp.utpdevs.ngc_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class MoodState {
  
  private UUID id;
  
  @JsonProperty("user_id")
  private UUID userId;
  
  @JsonProperty("route_id")
  private UUID routeId;
  
  @JsonProperty("mood_type")
  private String moodType;
  
  @JsonProperty("mood_value")
  private Integer moodValue;
  
  private String comment;
  
  @JsonProperty("location_lat")
  private BigDecimal locationLat;
  
  @JsonProperty("location_lng")
  private BigDecimal locationLng;
  
  @JsonProperty("created_at")
  private OffsetDateTime createdAt;
  
  // Getters y Setters
  public UUID getId() {
      return id;
  }
  
  public void setId(UUID id) {
      this.id = id;
  }
  
  public UUID getUserId() {
      return userId;
  }
  
  public void setUserId(UUID userId) {
      this.userId = userId;
  }
  
  public UUID getRouteId() {
      return routeId;
  }
  
  public void setRouteId(UUID routeId) {
      this.routeId = routeId;
  }
  
  public String getMoodType() {
      return moodType;
  }
  
  public void setMoodType(String moodType) {
      this.moodType = moodType;
  }
  
  public Integer getMoodValue() {
      return moodValue;
  }
  
  public void setMoodValue(Integer moodValue) {
      this.moodValue = moodValue;
  }
  
  public String getComment() {
      return comment;
  }
  
  public void setComment(String comment) {
      this.comment = comment;
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
  
  public OffsetDateTime getCreatedAt() {
      return createdAt;
  }
  
  public void setCreatedAt(OffsetDateTime createdAt) {
      this.createdAt = createdAt;
  }
}