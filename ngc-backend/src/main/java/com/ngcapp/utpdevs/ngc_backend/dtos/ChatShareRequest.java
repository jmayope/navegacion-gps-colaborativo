package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ChatShareRequest {
    
    private UUID chatId;
    
    @JsonProperty("share_link")
    private String shareLink;
    
    @JsonProperty("share_expires_at")
    private OffsetDateTime shareExpiresAt;
    
    // Getters y Setters
    public UUID getChatId() {
        return chatId;
    }
    
    public void setChatId(UUID chatId) {
        this.chatId = chatId;
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