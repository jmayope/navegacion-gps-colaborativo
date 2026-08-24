package com.ngcapp.utpdevs.ngc_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class MovementHistory {

  private UUID id;

  @JsonProperty("user_id")
  private UUID userId;

  @JsonProperty("route_id")
  private UUID routeId;

  @JsonProperty("latitude")
  private BigDecimal latitude;

  @JsonProperty("longitude")
  private BigDecimal longitude;

  @JsonProperty("altitude")
  private BigDecimal altitude;

  @JsonProperty("speed")
  private BigDecimal speed; // en km/h

  @JsonProperty("heading")
  private BigDecimal heading; // grados

  @JsonProperty("accuracy")
  private BigDecimal accuracy; // en metros

  @JsonProperty("is_moving")
  private Boolean isMoving;

  @JsonProperty("battery_level")
  private Integer batteryLevel;

  @JsonProperty("recorded_at")
  private OffsetDateTime recordedAt;

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

  public BigDecimal getLatitude() {
    return latitude;
  }

  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  public BigDecimal getLongitude() {
    return longitude;
  }

  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  public BigDecimal getAltitude() {
    return altitude;
  }

  public void setAltitude(BigDecimal altitude) {
    this.altitude = altitude;
  }

  public BigDecimal getSpeed() {
    return speed;
  }

  public void setSpeed(BigDecimal speed) {
    this.speed = speed;
  }

  public BigDecimal getHeading() {
    return heading;
  }

  public void setHeading(BigDecimal heading) {
    this.heading = heading;
  }

  public BigDecimal getAccuracy() {
    return accuracy;
  }

  public void setAccuracy(BigDecimal accuracy) {
    this.accuracy = accuracy;
  }

  public Boolean getIsMoving() {
    return isMoving;
  }

  public void setIsMoving(Boolean isMoving) {
    this.isMoving = isMoving;
  }

  public Integer getBatteryLevel() {
    return batteryLevel;
  }

  public void setBatteryLevel(Integer batteryLevel) {
    this.batteryLevel = batteryLevel;
  }

  public OffsetDateTime getRecordedAt() {
    return recordedAt;
  }

  public void setRecordedAt(OffsetDateTime recordedAt) {
    this.recordedAt = recordedAt;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }
}