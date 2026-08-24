package com.ngcapp.utpdevs.ngc_backend.dtos;

import java.util.List;
import java.util.UUID;

public class ChatReadRequest {
    
  private List<UUID> chatIds;

  // Getters y Setters
  public List<UUID> getChatIds() {
    return chatIds;
  }

  public void setChatIds(List<UUID> chatIds) {
    this.chatIds = chatIds;
  }
}