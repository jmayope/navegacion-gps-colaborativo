package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class MovementStats {

  @JsonProperty("total_points")
  private long totalPoints;

  @JsonProperty("total_distance_km")
  private BigDecimal totalDistanceKm;

  @JsonProperty("avg_speed_kmh")
  private BigDecimal avgSpeedKmh;

  @JsonProperty("max_speed_kmh")
  private BigDecimal maxSpeedKmh;

  @JsonProperty("total_duration_minutes")
  private long totalDurationMinutes;

  @JsonProperty("moving_time_minutes")
  private long movingTimeMinutes;

  @JsonProperty("stopped_time_minutes")
  private long stoppedTimeMinutes;

  @JsonProperty("avg_battery_level")
  private BigDecimal avgBatteryLevel;

  @JsonProperty("min_battery_level")
  private Integer minBatteryLevel;

  @JsonProperty("max_battery_level")
  private Integer maxBatteryLevel;

  // Getters y Setters
  public long getTotalPoints() {
    return totalPoints;
  }

  public void setTotalPoints(long totalPoints) {
    this.totalPoints = totalPoints;
  }

  public BigDecimal getTotalDistanceKm() {
    return totalDistanceKm;
  }

  public void setTotalDistanceKm(BigDecimal totalDistanceKm) {
    this.totalDistanceKm = totalDistanceKm;
  }

  public BigDecimal getAvgSpeedKmh() {
    return avgSpeedKmh;
  }

  public void setAvgSpeedKmh(BigDecimal avgSpeedKmh) {
    this.avgSpeedKmh = avgSpeedKmh;
  }

  public BigDecimal getMaxSpeedKmh() {
    return maxSpeedKmh;
  }

  public void setMaxSpeedKmh(BigDecimal maxSpeedKmh) {
    this.maxSpeedKmh = maxSpeedKmh;
  }

  public long getTotalDurationMinutes() {
    return totalDurationMinutes;
  }

  public void setTotalDurationMinutes(long totalDurationMinutes) {
    this.totalDurationMinutes = totalDurationMinutes;
  }

  public long getMovingTimeMinutes() {
    return movingTimeMinutes;
  }

  public void setMovingTimeMinutes(long movingTimeMinutes) {
    this.movingTimeMinutes = movingTimeMinutes;
  }

  public long getStoppedTimeMinutes() {
    return stoppedTimeMinutes;
  }

  public void setStoppedTimeMinutes(long stoppedTimeMinutes) {
    this.stoppedTimeMinutes = stoppedTimeMinutes;
  }

  public BigDecimal getAvgBatteryLevel() {
    return avgBatteryLevel;
  }

  public void setAvgBatteryLevel(BigDecimal avgBatteryLevel) {
    this.avgBatteryLevel = avgBatteryLevel;
  }

  public Integer getMinBatteryLevel() {
    return minBatteryLevel;
  }

  public void setMinBatteryLevel(Integer minBatteryLevel) {
    this.minBatteryLevel = minBatteryLevel;
  }

  public Integer getMaxBatteryLevel() {
    return maxBatteryLevel;
  }

  public void setMaxBatteryLevel(Integer maxBatteryLevel) {
    this.maxBatteryLevel = maxBatteryLevel;
  }
}