package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncidentResolveRequest {

  private String resolutionNotes;

  @JsonProperty("report_photo_url")
  private String reportPhotoUrl;

  // Getters y Setters
  public String getResolutionNotes() {
    return resolutionNotes;
  }

  public void setResolutionNotes(String resolutionNotes) {
    this.resolutionNotes = resolutionNotes;
  }

  public String getReportPhotoUrl() {
    return reportPhotoUrl;
  }

  public void setReportPhotoUrl(String reportPhotoUrl) {
    this.reportPhotoUrl = reportPhotoUrl;
  }
}