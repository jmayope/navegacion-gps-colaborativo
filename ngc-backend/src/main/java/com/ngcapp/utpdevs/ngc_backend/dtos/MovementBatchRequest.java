package com.ngcapp.utpdevs.ngc_backend.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MovementBatchRequest {

  @JsonProperty("user_id")
  private UUID userId;

  @JsonProperty("route_id")
  private UUID routeId;

  private List<MovementPoint> points;

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

  public List<MovementPoint> getPoints() {
    return points;
  }

  public void setPoints(List<MovementPoint> points) {
    this.points = points;
  }
}

  