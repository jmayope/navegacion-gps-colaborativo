package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class RouteQueryRequest {

  @JsonProperty("user_id")
  private UUID userId;

  @JsonProperty("query_type")
  private String queryType;

  @JsonProperty("origin_lat")
  private BigDecimal originLat;

  @JsonProperty("origin_lng")
  private BigDecimal originLng;

  @JsonProperty("destination_lat")
  private BigDecimal destinationLat;

  @JsonProperty("destination_lng")
  private BigDecimal destinationLng;

  @JsonProperty("query_filters")
  private Map<String, Object> queryFilters;

  @JsonProperty("result_count")
  private Integer resultCount;

  @JsonProperty("response_time_ms")
  private Integer responseTimeMs;

  @JsonProperty("selected_route_id")
  private UUID selectedRouteId;

  @JsonProperty("ip_address")
  private String ipAddress;

  @JsonProperty("user_agent")
  private String userAgent;

  // Getters y Setters
  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public String getQueryType() {
    return queryType;
  }

  public void setQueryType(String queryType) {
    this.queryType = queryType;
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

  public Map<String, Object> getQueryFilters() {
    return queryFilters;
  }

  public void setQueryFilters(Map<String, Object> queryFilters) {
    this.queryFilters = queryFilters;
  }

  public Integer getResultCount() {
    return resultCount;
  }

  public void setResultCount(Integer resultCount) {
    this.resultCount = resultCount;
  }

  public Integer getResponseTimeMs() {
    return responseTimeMs;
  }

  public void setResponseTimeMs(Integer responseTimeMs) {
    this.responseTimeMs = responseTimeMs;
  }

  public UUID getSelectedRouteId() {
    return selectedRouteId;
  }

  public void setSelectedRouteId(UUID selectedRouteId) {
    this.selectedRouteId = selectedRouteId;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }
}