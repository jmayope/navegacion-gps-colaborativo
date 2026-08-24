package com.ngcapp.utpdevs.ngc_backend.dtos;

import java.util.UUID;

public class SessionPingRequest {
    
  private UUID sessionId;

  // Getters y Setters
  public UUID getSessionId() {
    return sessionId;
  }

  public void setSessionId(UUID sessionId) {
    this.sessionId = sessionId;
  }
}