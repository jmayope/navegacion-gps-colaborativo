package com.ngcapp.utpdevs.ngc_backend.services;

import com.ngcapp.utpdevs.ngc_backend.dtos.IncidentStats;
import com.ngcapp.utpdevs.ngc_backend.models.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class IncidentService {
    
    @Autowired
    private SupabaseCrudService crudService;
    
    private static final String TABLE = "incidents";
    
    // Obtener todos los incidentes
    public List<Incident> findAll() {
        return crudService.findAll(TABLE, Incident[].class);
    }
    
    // Obtener incidente por ID
    public Incident findById(UUID id) {
        return crudService.findById(TABLE, id, Incident[].class);
    }
    
    // Obtener incidentes por ruta
    public List<Incident> findByRouteId(UUID routeId) {
        return crudService.find(
            TABLE, 
            "route_id=eq." + routeId + "&order=created_at.desc", 
            Incident[].class
        );
    }
    
    // Obtener incidentes por segmento
    public List<Incident> findBySegmentId(UUID segmentId) {
        return crudService.find(
            TABLE, 
            "segment_id=eq." + segmentId + "&order=created_at.desc", 
            Incident[].class
        );
    }
    
    // Obtener incidentes por usuario
    public List<Incident> findByUserId(UUID userId) {
        return crudService.find(
            TABLE, 
            "user_id=eq." + userId + "&order=created_at.desc", 
            Incident[].class
        );
    }
    
    // Obtener incidentes por tipo
    public List<Incident> findByType(String incidentType) {
        return crudService.find(
            TABLE, 
            "incident_type=eq." + incidentType + "&order=created_at.desc", 
            Incident[].class
        );
    }
    
    // Obtener incidentes por severidad
    public List<Incident> findBySeverity(String severity) {
        return crudService.find(
            TABLE, 
            "severity=eq." + severity + "&order=created_at.desc", 
            Incident[].class
        );
    }
    
    // Obtener incidentes activos (no resueltos)
    public List<Incident> findActive() {
        return crudService.find(
            TABLE, 
            "is_resolved=eq.false&order=created_at.desc", 
            Incident[].class
        );
    }
    
    // Obtener incidentes de pánico
    public List<Incident> findPanicAlerts() {
        return crudService.find(
            TABLE, 
            "is_panic=eq.true&order=created_at.desc", 
            Incident[].class
        );
    }
    
    // Obtener incidentes de pánico activos
    public List<Incident> findActivePanicAlerts() {
        return crudService.find(
            TABLE, 
            "is_panic=eq.true&is_resolved=eq.false&order=created_at.desc", 
            Incident[].class
        );
    }
    
    // Obtener incidentes por ubicación (radio)
    public List<Incident> findByLocationRadius(
            BigDecimal lat, 
            BigDecimal lng, 
            BigDecimal radiusKm) {
        // Nota: Para búsqueda por radio en Supabase, necesitas usar la extensión PostGIS
        // O simplemente obtener todos y filtrar en memoria
        // Esta es una implementación simple sin PostGIS
        List<Incident> all = findAll();
        // Aquí podrías implementar filtrado por distancia
        return all;
    }
    
    // Crear incidente
    public Incident create(Incident incident) {
        if (incident.getIsPanic() == null) {
            incident.setIsPanic(false);
        }
        if (incident.getIsResolved() == null) {
            incident.setIsResolved(false);
        }
        return crudService.insert(TABLE, incident, Incident[].class);
    }
    
    // Crear incidente de pánico
    public Incident createPanicAlert(Incident incident) {
        incident.setIsPanic(true);
        incident.setSeverity("high"); // Pánico siempre es de alta severidad
        return create(incident);
    }
    
    // Resolver incidente
    public Incident resolve(UUID id, String resolutionNotes, String reportPhotoUrl) {
        Incident existing = findById(id);
        if (existing == null) {
            return null;
        }
        
        existing.setIsResolved(true);
        existing.setResolvedAt(OffsetDateTime.now());
        if (resolutionNotes != null && !resolutionNotes.isEmpty()) {
            existing.setDescription(existing.getDescription() + "\n\nResolución: " + resolutionNotes);
        }
        if (reportPhotoUrl != null && !reportPhotoUrl.isEmpty()) {
            existing.setReportPhotoUrl(reportPhotoUrl);
        }
        
        return update(id, existing);
    }
    
    // Actualizar incidente
    public Incident update(UUID id, Incident incident) {
        return crudService.update(TABLE, id, incident, Incident[].class);
    }
    
    // Eliminar incidente
    public void delete(UUID id) {
        crudService.delete(TABLE, id);
    }
    
    // Eliminar todos los incidentes de una ruta
    public void deleteByRouteId(UUID routeId) {
        crudService.delete(TABLE, "route_id=eq." + routeId);
    }
    
    // Verificar si existe incidente
    public boolean existsById(UUID id) {
        return crudService.exists(TABLE, "id=eq." + id);
    }
    
    // Obtener estadísticas
    public IncidentStats getStats() {
        IncidentStats stats = new IncidentStats();
        
        List<Incident> all = findAll();
        stats.setTotal(all.size());
        
        // Incidentes activos
        List<Incident> active = findActive();
        stats.setActiveIncidents(active.size());
        
        // Incidentes resueltos
        long resolved = all.stream().filter(i -> i.getIsResolved()).count();
        stats.setResolvedIncidents(resolved);
        
        // Alertas de pánico
        List<Incident> panic = findPanicAlerts();
        stats.setPanicAlerts(panic.size());
        
        // Agrupar por tipo
        Map<String, Long> byType = new HashMap<>();
        for (Incident incident : all) {
            byType.merge(incident.getIncidentType(), 1L, Long::sum);
        }
        stats.setByType(byType);
        
        // Agrupar por severidad
        Map<String, Long> bySeverity = new HashMap<>();
        for (Incident incident : all) {
            String severity = incident.getSeverity() != null ? incident.getSeverity() : "unknown";
            bySeverity.merge(severity, 1L, Long::sum);
        }
        stats.setBySeverity(bySeverity);
        
        return stats;
    }
    
    // Obtener estadísticas de un usuario
    public IncidentStats getStatsByUser(UUID userId) {
        List<Incident> userIncidents = findByUserId(userId);
        IncidentStats stats = new IncidentStats();
        
        stats.setTotal(userIncidents.size());
        
        long active = userIncidents.stream().filter(i -> !i.getIsResolved()).count();
        stats.setActiveIncidents(active);
        
        long resolved = userIncidents.stream().filter(i -> i.getIsResolved()).count();
        stats.setResolvedIncidents(resolved);
        
        long panic = userIncidents.stream().filter(i -> i.getIsPanic()).count();
        stats.setPanicAlerts(panic);
        
        // Agrupar por tipo
        Map<String, Long> byType = new HashMap<>();
        for (Incident incident : userIncidents) {
            byType.merge(incident.getIncidentType(), 1L, Long::sum);
        }
        stats.setByType(byType);
        
        // Agrupar por severidad
        Map<String, Long> bySeverity = new HashMap<>();
        for (Incident incident : userIncidents) {
            String severity = incident.getSeverity() != null ? incident.getSeverity() : "unknown";
            bySeverity.merge(severity, 1L, Long::sum);
        }
        stats.setBySeverity(bySeverity);
        
        return stats;
    }
}