package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ChatRequest {
    
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
    
    @JsonProperty("is_shared")
    private Boolean isShared = false;
    
    @JsonProperty("share_link")
    private String shareLink;
    
    @JsonProperty("share_expires_at")
    private OffsetDateTime shareExpiresAt;
    
    // Getters y Setters
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
}