package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class QueryStats {

  @JsonProperty("total_queries")
  private long totalQueries;

  @JsonProperty("by_type")
  private Map<String, Long> byType;

  @JsonProperty("avg_response_time_ms")
  private double avgResponseTimeMs;

  @JsonProperty("max_response_time_ms")
  private double maxResponseTimeMs;

  @JsonProperty("min_response_time_ms")
  private double minResponseTimeMs;

  @JsonProperty("total_results")
  private long totalResults;

  @JsonProperty("avg_results_per_query")
  private double avgResultsPerQuery;

  @JsonProperty("queries_with_selection")
  private long queriesWithSelection;

  @JsonProperty("selection_rate")
  private double selectionRate;

  @JsonProperty("popular_filters")
  private Map<String, Long> popularFilters;

  // Getters y Setters
  public long getTotalQueries() {
    return totalQueries;
  }

  public void setTotalQueries(long totalQueries) {
    this.totalQueries = totalQueries;
  }

  public Map<String, Long> getByType() {
    return byType;
  }

  public void setByType(Map<String, Long> byType) {
    this.byType = byType;
  }

  public double getAvgResponseTimeMs() {
    return avgResponseTimeMs;
  }

  public void setAvgResponseTimeMs(double avgResponseTimeMs) {
    this.avgResponseTimeMs = avgResponseTimeMs;
  }

  public double getMaxResponseTimeMs() {
    return maxResponseTimeMs;
  }

  public void setMaxResponseTimeMs(double maxResponseTimeMs) {
    this.maxResponseTimeMs = maxResponseTimeMs;
  }

  public double getMinResponseTimeMs() {
    return minResponseTimeMs;
  }

  public void setMinResponseTimeMs(double minResponseTimeMs) {
    this.minResponseTimeMs = minResponseTimeMs;
  }

  public long getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(long totalResults) {
    this.totalResults = totalResults;
  }

  public double getAvgResultsPerQuery() {
    return avgResultsPerQuery;
  }

  public void setAvgResultsPerQuery(double avgResultsPerQuery) {
    this.avgResultsPerQuery = avgResultsPerQuery;
  }

  public long getQueriesWithSelection() {
    return queriesWithSelection;
  }

  public void setQueriesWithSelection(long queriesWithSelection) {
    this.queriesWithSelection = queriesWithSelection;
  }

  public double getSelectionRate() {
    return selectionRate;
  }

  public void setSelectionRate(double selectionRate) {
    this.selectionRate = selectionRate;
  }

  public Map<String, Long> getPopularFilters() {
    return popularFilters;
  }

  public void setPopularFilters(Map<String, Long> popularFilters) {
    this.popularFilters = popularFilters;
  }
}