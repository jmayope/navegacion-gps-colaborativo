package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.UUID;

public class MoodStateRequest {
    
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

  // Getters y Setters
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
}