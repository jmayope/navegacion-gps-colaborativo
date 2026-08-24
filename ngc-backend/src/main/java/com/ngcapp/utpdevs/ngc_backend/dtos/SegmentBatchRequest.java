package com.ngcapp.utpdevs.ngc_backend.dtos;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SegmentBatchRequest {
    
    @JsonProperty("route_id")
    private UUID routeId;
    
    private List<SegmentRequest> segments;
    
    // Getters y Setters
    public UUID getRouteId() {
        return routeId;
    }
    
    public void setRouteId(UUID routeId) {
        this.routeId = routeId;
    }
    
    public List<SegmentRequest> getSegments() {
        return segments;
    }
    
    public void setSegments(List<SegmentRequest> segments) {
        this.segments = segments;
    }
}