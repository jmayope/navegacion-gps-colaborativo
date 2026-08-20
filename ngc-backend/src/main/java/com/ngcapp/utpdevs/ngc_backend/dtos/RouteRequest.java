package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.UUID;

public class RouteRequest {
    
  @JsonProperty("user_id")
  private UUID userId;
  
  @JsonProperty("origin_name")
  private String originName;
  
  @JsonProperty("origin_lat")
  private BigDecimal originLat;
  
  @JsonProperty("origin_lng")
  private BigDecimal originLng;
  
  @JsonProperty("origin_address")
  private String originAddress;
  
  @JsonProperty("destination_name")
  private String destinationName;
  
  @JsonProperty("destination_lat")
  private BigDecimal destinationLat;
  
  @JsonProperty("destination_lng")
  private BigDecimal destinationLng;
  
  @JsonProperty("destination_address")
  private String destinationAddress;
  
  @JsonProperty("estimated_distance")
  private BigDecimal estimatedDistance;
  
  @JsonProperty("estimated_duration")
  private Integer estimatedDuration;
  
  @JsonProperty("is_favorite")
  private Boolean isFavorite = false;
  
  // Getters y Setters
  public UUID getUserId() {
      return userId;
  }
  
  public void setUserId(UUID userId) {
      this.userId = userId;
  }
  
  public String getOriginName() {
      return originName;
  }
  
  public void setOriginName(String originName) {
      this.originName = originName;
  }
  
  public BigDecimal getOriginLat() {
      return originLat;
  }
  
  public void setOriginLat(BigDecimal originLat) {
      this.originLat = originLat;
  }
  
  public BigDecimal getOriginLng() {
      return originLng;
  }
  
  public void setOriginLng(BigDecimal originLng) {
      this.originLng = originLng;
  }
  
  public String getOriginAddress() {
      return originAddress;
  }
  
  public void setOriginAddress(String originAddress) {
      this.originAddress = originAddress;
  }
  
  public String getDestinationName() {
      return destinationName;
  }
  
  public void setDestinationName(String destinationName) {
      this.destinationName = destinationName;
  }
  
  public BigDecimal getDestinationLat() {
      return destinationLat;
  }
  
  public void setDestinationLat(BigDecimal destinationLat) {
      this.destinationLat = destinationLat;
  }
  
  public BigDecimal getDestinationLng() {
      return destinationLng;
  }
  
  public void setDestinationLng(BigDecimal destinationLng) {
      this.destinationLng = destinationLng;
  }
  
  public String getDestinationAddress() {
      return destinationAddress;
  }
  
  public void setDestinationAddress(String destinationAddress) {
      this.destinationAddress = destinationAddress;
  }
  
  public BigDecimal getEstimatedDistance() {
      return estimatedDistance;
  }
  
  public void setEstimatedDistance(BigDecimal estimatedDistance) {
      this.estimatedDistance = estimatedDistance;
  }
  
  public Integer getEstimatedDuration() {
      return estimatedDuration;
  }
  
  public void setEstimatedDuration(Integer estimatedDuration) {
      this.estimatedDuration = estimatedDuration;
  }
  
  public Boolean getIsFavorite() {
      return isFavorite;
  }
  
  public void setIsFavorite(Boolean isFavorite) {
      this.isFavorite = isFavorite;
  }
}