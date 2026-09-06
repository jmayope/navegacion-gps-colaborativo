package com.ngcapp.utpdevs.ngc_backend.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class RouteFunctionResponse {
  private UUID route_id;
  private UUID user_id;
  private String user_name;
  private String origin_name;
  private String destination_name;
  private BigDecimal origin_lat;
  private BigDecimal origin_lng;
  private BigDecimal destination_lat;
  private BigDecimal destination_lng;
  private BigDecimal estimated_distance;
  private Integer estimated_duration;
  private Boolean is_favorite;
  private String status;
  private OffsetDateTime started_at;
  private OffsetDateTime completed_at;
  private OffsetDateTime created_at;
  private Integer incident_count;
  
  public UUID getRoute_id() {
    return route_id;
  }
  public void setRoute_id(UUID route_id) {
    this.route_id = route_id;
  }
  public UUID getUser_id() {
    return user_id;
  }
  public void setUser_id(UUID user_id) {
    this.user_id = user_id;
  }
  public String getUser_name() {
    return user_name;
  }
  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }
  public String getOrigin_name() {
    return origin_name;
  }
  public void setOrigin_name(String origin_name) {
    this.origin_name = origin_name;
  }
  public String getDestination_name() {
    return destination_name;
  }
  public void setDestination_name(String destination_name) {
    this.destination_name = destination_name;
  }
  public BigDecimal getOrigin_lat() {
    return origin_lat;
  }
  public void setOrigin_lat(BigDecimal origin_lat) {
    this.origin_lat = origin_lat;
  }
  public BigDecimal getOrigin_lng() {
    return origin_lng;
  }
  public void setOrigin_lng(BigDecimal origin_lng) {
    this.origin_lng = origin_lng;
  }
  public BigDecimal getDestination_lat() {
    return destination_lat;
  }
  public void setDestination_lat(BigDecimal destination_lat) {
    this.destination_lat = destination_lat;
  }
  public BigDecimal getDestination_lng() {
    return destination_lng;
  }
  public void setDestination_lng(BigDecimal destination_lng) {
    this.destination_lng = destination_lng;
  }
  public BigDecimal getEstimated_distance() {
    return estimated_distance;
  }
  public void setEstimated_distance(BigDecimal estimated_distance) {
    this.estimated_distance = estimated_distance;
  }
  public Integer getEstimated_duration() {
    return estimated_duration;
  }
  public void setEstimated_duration(Integer estimated_duration) {
    this.estimated_duration = estimated_duration;
  }
  public Boolean getIs_favorite() {
    return is_favorite;
  }
  public void setIs_favorite(Boolean is_favorite) {
    this.is_favorite = is_favorite;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public OffsetDateTime getStarted_at() {
    return started_at;
  }
  public void setStarted_at(OffsetDateTime started_at) {
    this.started_at = started_at;
  }
  public OffsetDateTime getCompleted_at() {
    return completed_at;
  }
  public void setCompleted_at(OffsetDateTime completed_at) {
    this.completed_at = completed_at;
  }
  public OffsetDateTime getCreated_at() {
    return created_at;
  }
  public void setCreated_at(OffsetDateTime created_at) {
    this.created_at = created_at;
  }
  public Integer getIncident_count() {
    return incident_count;
  }
  public void setIncident_count(Integer incident_count) {
    this.incident_count = incident_count;
  }
  
}