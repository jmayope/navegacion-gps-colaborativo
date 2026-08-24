package com.ngcapp.utpdevs.ngc_backend.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ChatMessage {
    private UUID id;
    private UUID userId;
    private String userName;
    private String messageType;
    private String messageContent;
    private BigDecimal locationLat;
    private BigDecimal locationLng;
    private Boolean isRead;
    private OffsetDateTime createdAt;
    private OffsetDateTime readAt;
    
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
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
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
    
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public OffsetDateTime getReadAt() {
        return readAt;
    }
    
    public void setReadAt(OffsetDateTime readAt) {
        this.readAt = readAt;
    }
}