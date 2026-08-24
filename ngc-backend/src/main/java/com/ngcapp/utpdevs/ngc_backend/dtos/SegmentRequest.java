package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.UUID;

public class SegmentRequest {
    
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
  private BigDecimal distance;

  @JsonProperty("duration")
  private Integer duration;

  @JsonProperty("street_name")
  private String streetName;

  @JsonProperty("road_type")
  private String roadType;

  @JsonProperty("speed_limit")
  private Integer speedLimit;

  // Getters y Setters
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
}