package com.ngcapp.utpdevs.ngc_backend.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class MovementPoint {

  private BigDecimal latitude;

  private BigDecimal longitude;

  private BigDecimal altitude;
  private BigDecimal speed;
  private BigDecimal heading;
  private BigDecimal accuracy;
  private Boolean isMoving;

  private Integer batteryLevel;

  private OffsetDateTime recordedAt;

  // Getters y Setters
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
}