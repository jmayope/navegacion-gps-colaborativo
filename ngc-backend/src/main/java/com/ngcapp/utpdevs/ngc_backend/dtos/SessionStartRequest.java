package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class SessionStartRequest {
    
  @JsonProperty("user_id")
  private UUID userId;

  @JsonProperty("device_id")
  private String deviceId;

  @JsonProperty("device_name")
  private String deviceName;

  @JsonProperty("device_os")
  private String deviceOs;

  @JsonProperty("app_version")
  private String appVersion;

  // Getters y Setters
  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public String getDeviceOs() {
    return deviceOs;
  }

  public void setDeviceOs(String deviceOs) {
    this.deviceOs = deviceOs;
  }

  public String getAppVersion() {
    return appVersion;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }
}