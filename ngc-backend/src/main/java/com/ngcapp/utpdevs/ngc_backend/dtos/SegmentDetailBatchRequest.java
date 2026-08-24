package com.ngcapp.utpdevs.ngc_backend.dtos;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SegmentDetailBatchRequest {
    
    @JsonProperty("segment_id")
    private UUID segmentId;
    
    private List<SegmentDetailRequest> details;
    
    // Getters y Setters
    public UUID getSegmentId() {
        return segmentId;
    }
    
    public void setSegmentId(UUID segmentId) {
        this.segmentId = segmentId;
    }
    
    public List<SegmentDetailRequest> getDetails() {
        return details;
    }
    
    public void setDetails(List<SegmentDetailRequest> details) {
        this.details = details;
    }
}