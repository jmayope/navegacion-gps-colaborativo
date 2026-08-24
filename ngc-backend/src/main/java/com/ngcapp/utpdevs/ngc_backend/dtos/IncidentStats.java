package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class IncidentStats {
    
    private long total;
    
    @JsonProperty("active")
    private long activeIncidents;
    
    @JsonProperty("resolved")
    private long resolvedIncidents;
    
    @JsonProperty("panic_alerts")
    private long panicAlerts;
    
    @JsonProperty("by_type")
    private Map<String, Long> byType;
    
    @JsonProperty("by_severity")
    private Map<String, Long> bySeverity;
    
    // Getters y Setters
    public long getTotal() {
        return total;
    }
    
    public void setTotal(long total) {
        this.total = total;
    }
    
    public long getActiveIncidents() {
        return activeIncidents;
    }
    
    public void setActiveIncidents(long activeIncidents) {
        this.activeIncidents = activeIncidents;
    }
    
    public long getResolvedIncidents() {
        return resolvedIncidents;
    }
    
    public void setResolvedIncidents(long resolvedIncidents) {
        this.resolvedIncidents = resolvedIncidents;
    }
    
    public long getPanicAlerts() {
        return panicAlerts;
    }
    
    public void setPanicAlerts(long panicAlerts) {
        this.panicAlerts = panicAlerts;
    }
    
    public Map<String, Long> getByType() {
        return byType;
    }
    
    public void setByType(Map<String, Long> byType) {
        this.byType = byType;
    }
    
    public Map<String, Long> getBySeverity() {
        return bySeverity;
    }
    
    public void setBySeverity(Map<String, Long> bySeverity) {
        this.bySeverity = bySeverity;
    }
}