package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class MoodStats {
    
  private double average;

  @JsonProperty("total_records")
  private long totalRecords;

  @JsonProperty("by_type")
  private Map<String, Long> byType;

  @JsonProperty("by_value")
  private Map<Integer, Long> byValue;

  @JsonProperty("most_common_mood")
  private String mostCommonMood;

  @JsonProperty("average_by_type")
  private Map<String, Double> averageByType;

  // Getters y Setters
  public double getAverage() {
    return average;
  }

  public void setAverage(double average) {
    this.average = average;
  }

  public long getTotalRecords() {
    return totalRecords;
  }

  public void setTotalRecords(long totalRecords) {
    this.totalRecords = totalRecords;
  }

  public Map<String, Long> getByType() {
    return byType;
  }

  public void setByType(Map<String, Long> byType) {
    this.byType = byType;
  }

  public Map<Integer, Long> getByValue() {
    return byValue;
  }

  public void setByValue(Map<Integer, Long> byValue) {
    this.byValue = byValue;
  }

  public String getMostCommonMood() {
    return mostCommonMood;
  }

  public void setMostCommonMood(String mostCommonMood) {
    this.mostCommonMood = mostCommonMood;
  }

  public Map<String, Double> getAverageByType() {
    return averageByType;
  }

  public void setAverageByType(Map<String, Double> averageByType) {
    this.averageByType = averageByType;
  }
}