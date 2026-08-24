package com.ngcapp.utpdevs.ngc_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Chat {

  private UUID id;

  @JsonProperty("route_id")
  private UUID routeId;

  @JsonProperty("user_id")
  private UUID userId;

  @JsonProperty("recipient_user_id")
  private UUID recipientUserId;

  @JsonProperty("message_type")
  private String messageType;

  @JsonProperty("message_content")
  private String messageContent;

  @JsonProperty("location_lat")
  private BigDecimal locationLat;

  @JsonProperty("location_lng")
  private BigDecimal locationLng;

  @JsonProperty("is_read")
  private Boolean isRead = false;

  @JsonProperty("read_at")
  private OffsetDateTime readAt;

  @JsonProperty("is_shared")
  private Boolean isShared = false;

  @JsonProperty("share_link")
  private String shareLink;

  @JsonProperty("share_expires_at")
  private OffsetDateTime shareExpiresAt;

  @JsonProperty("created_at")
  private OffsetDateTime createdAt;

  @JsonProperty("updated_at")
  private OffsetDateTime updatedAt;

  // Getters y Setters
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getRouteId() {
    return routeId;
  }

  public void setRouteId(UUID routeId) {
    this.routeId = routeId;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public UUID getRecipientUserId() {
    return recipientUserId;
  }

  public void setRecipientUserId(UUID recipientUserId) {
    this.recipientUserId = recipientUserId;
  }

  public String getMessageType() {
    return messageType;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public String getMessageContent() {
    return messageContent;
  }

  public void setMessageContent(String messageContent) {
    this.messageContent = messageContent;
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

  public Boolean getIsRead() {
    return isRead;
  }

  public void setIsRead(Boolean isRead) {
    this.isRead = isRead;
  }

  public OffsetDateTime getReadAt() {
    return readAt;
  }

  public void setReadAt(OffsetDateTime readAt) {
    this.readAt = readAt;
  }

  public Boolean getIsShared() {
    return isShared;
  }

  public void setIsShared(Boolean isShared) {
    this.isShared = isShared;
  }

  public String getShareLink() {
    return shareLink;
  }

  public void setShareLink(String shareLink) {
    this.shareLink = shareLink;
  }

  public OffsetDateTime getShareExpiresAt() {
    return shareExpiresAt;
  }

  public void setShareExpiresAt(OffsetDateTime shareExpiresAt) {
    this.shareExpiresAt = shareExpiresAt;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}