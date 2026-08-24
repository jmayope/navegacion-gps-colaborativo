package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class ChatConversation {
    
    @JsonProperty("user_id")
    private UUID userId;
    
    @JsonProperty("user_name")
    private String userName;
    
    @JsonProperty("route_id")
    private UUID routeId;
    
    @JsonProperty("route_name")
    private String routeName;
    
    @JsonProperty("total_messages")
    private int totalMessages;
    
    @JsonProperty("unread_count")
    private int unreadCount;
    
    @JsonProperty("last_message")
    private ChatMessage lastMessage;
    
    @JsonProperty("messages")
    private List<ChatMessage> messages;
    
    // Getters y Setters
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
    
    public UUID getRouteId() {
        return routeId;
    }
    
    public void setRouteId(UUID routeId) {
        this.routeId = routeId;
    }
    
    public String getRouteName() {
        return routeName;
    }
    
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
    
    public int getTotalMessages() {
        return totalMessages;
    }
    
    public void setTotalMessages(int totalMessages) {
        this.totalMessages = totalMessages;
    }
    
    public int getUnreadCount() {
        return unreadCount;
    }
    
    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
    
    public ChatMessage getLastMessage() {
        return lastMessage;
    }
    
    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }
    
    public List<ChatMessage> getMessages() {
        return messages;
    }
    
    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}

