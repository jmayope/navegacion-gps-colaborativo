package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class SessionStats {

  @JsonProperty("total_sessions")
  private long totalSessions;

  @JsonProperty("active_sessions")
  private long activeSessions;

  @JsonProperty("inactive_sessions")
  private long inactiveSessions;

  @JsonProperty("expired_sessions")
  private long expiredSessions;

  @JsonProperty("by_device_os")
  private Map<String, Long> byDeviceOs;

  @JsonProperty("by_app_version")
  private Map<String, Long> byAppVersion;

  @JsonProperty("avg_session_duration_minutes")
  private double avgSessionDurationMinutes;

  @JsonProperty("unique_devices")
  private long uniqueDevices;

  @JsonProperty("unique_users")
  private long uniqueUsers;

  // Getters y Setters
  public long getTotalSessions() {
    return totalSessions;
  }

  public void setTotalSessions(long totalSessions) {
    this.totalSessions = totalSessions;
  }

  public long getActiveSessions() {
    return activeSessions;
  }

  public void setActiveSessions(long activeSessions) {
    this.activeSessions = activeSessions;
  }

  public long getInactiveSessions() {
    return inactiveSessions;
  }

  public void setInactiveSessions(long inactiveSessions) {
    this.inactiveSessions = inactiveSessions;
  }

  public long getExpiredSessions() {
    return expiredSessions;
  }

  public void setExpiredSessions(long expiredSessions) {
    this.expiredSessions = expiredSessions;
  }

  public Map<String, Long> getByDeviceOs() {
    return byDeviceOs;
  }

  public void setByDeviceOs(Map<String, Long> byDeviceOs) {
    this.byDeviceOs = byDeviceOs;
  }

  public Map<String, Long> getByAppVersion() {
    return byAppVersion;
  }

  public void setByAppVersion(Map<String, Long> byAppVersion) {
    this.byAppVersion = byAppVersion;
  }

  public double getAvgSessionDurationMinutes() {
    return avgSessionDurationMinutes;
  }

  public void setAvgSessionDurationMinutes(double avgSessionDurationMinutes) {
    this.avgSessionDurationMinutes = avgSessionDurationMinutes;
  }

  public long getUniqueDevices() {
    return uniqueDevices;
  }

  public void setUniqueDevices(long uniqueDevices) {
    this.uniqueDevices = uniqueDevices;
  }

  public long getUniqueUsers() {
    return uniqueUsers;
  }

  public void setUniqueUsers(long uniqueUsers) {
    this.uniqueUsers = uniqueUsers;
  }
}