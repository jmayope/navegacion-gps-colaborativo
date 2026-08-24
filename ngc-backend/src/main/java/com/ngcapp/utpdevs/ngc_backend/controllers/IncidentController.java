package com.ngcapp.utpdevs.ngc_backend.controllers;

import com.ngcapp.utpdevs.ngc_backend.dtos.IncidentRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.IncidentResolveRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.IncidentStats;
import com.ngcapp.utpdevs.ngc_backend.models.Incident;
import com.ngcapp.utpdevs.ngc_backend.services.IncidentService;
import com.ngcapp.utpdevs.ngc_backend.services.RouteService;
import com.ngcapp.utpdevs.ngc_backend.services.SegmentService;
import com.ngcapp.utpdevs.ngc_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "*")
public class IncidentController {
    
    @Autowired
    private IncidentService incidentService;
    
    @Autowired
    private RouteService routeService;
    
    @Autowired
    private SegmentService segmentService;
    
    @Autowired
    private UserService userService;
    
    // ============================================================
    // GET ENDPOINTS
    // ============================================================
    
    @GetMapping
    public ResponseEntity<List<Incident>> getAll() {
        return ResponseEntity.ok(incidentService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Incident> getById(@PathVariable UUID id) {
        Incident incident = incidentService.findById(id);
        if (incident == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(incident);
    }
    
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<Incident>> getByRoute(@PathVariable UUID routeId) {
        return ResponseEntity.ok(incidentService.findByRouteId(routeId));
    }
    
    @GetMapping("/segment/{segmentId}")
    public ResponseEntity<List<Incident>> getBySegment(@PathVariable UUID segmentId) {
        return ResponseEntity.ok(incidentService.findBySegmentId(segmentId));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Incident>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(incidentService.findByUserId(userId));
    }
    
    @GetMapping("/type/{incidentType}")
    public ResponseEntity<List<Incident>> getByType(@PathVariable String incidentType) {
        return ResponseEntity.ok(incidentService.findByType(incidentType));
    }
    
    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<Incident>> getBySeverity(@PathVariable String severity) {
        return ResponseEntity.ok(incidentService.findBySeverity(severity));
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Incident>> getActive() {
        return ResponseEntity.ok(incidentService.findActive());
    }
    
    @GetMapping("/panic")
    public ResponseEntity<List<Incident>> getPanicAlerts() {
        return ResponseEntity.ok(incidentService.findPanicAlerts());
    }
    
    @GetMapping("/panic/active")
    public ResponseEntity<List<Incident>> getActivePanicAlerts() {
        return ResponseEntity.ok(incidentService.findActivePanicAlerts());
    }
    
    @GetMapping("/stats")
    public ResponseEntity<IncidentStats> getStats() {
        return ResponseEntity.ok(incidentService.getStats());
    }
    
    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<IncidentStats> getStatsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(incidentService.getStatsByUser(userId));
    }
    
    // ============================================================
    // POST ENDPOINTS
    // ============================================================
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody IncidentRequest request) {
        // Validaciones de existencia
        Map<String, String> errors = new HashMap<>();
        
        if (!routeService.existsById(request.getRouteId())) {
            errors.put("route_id", "La ruta no existe");
        }
        
        if (request.getSegmentId() != null && !segmentService.existsById(request.getSegmentId())) {
            errors.put("segment_id", "El segmento no existe");
        }
        
        if (!userService.existsById(request.getUserId())) {
            errors.put("user_id", "El usuario no existe");
        }
        
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        
        Incident incident = new Incident();
        incident.setRouteId(request.getRouteId());
        incident.setSegmentId(request.getSegmentId());
        incident.setUserId(request.getUserId());
        incident.setIncidentType(request.getIncidentType());
        incident.setSeverity(request.getSeverity());
        incident.setDescription(request.getDescription());
        incident.setLocationLat(request.getLocationLat());
        incident.setLocationLng(request.getLocationLng());
        incident.setLocationAddress(request.getLocationAddress());
        incident.setIsPanic(request.getIsPanic() != null ? request.getIsPanic() : false);
        incident.setReportPhotoUrl(request.getReportPhotoUrl());
        
        // Si es pánico, asegurar severidad alta
        if (incident.getIsPanic()) {
            incident.setSeverity("high");
        }
        
        Incident created = incidentService.create(incident);
        
        // Si es pánico, notificación especial
        if (created.getIsPanic()) {
            // Aquí podrías enviar notificaciones push, emails, etc.
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Alerta de pánico registrada");
            response.put("incident", created);
            response.put("panicAlert", true);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PostMapping("/panic")
    public ResponseEntity<?> createPanicAlert(@RequestBody IncidentRequest request) {
        // Validación especial para pánico
        Map<String, String> errors = new HashMap<>();
        
        if (!routeService.existsById(request.getRouteId())) {
            errors.put("route_id", "La ruta no existe");
        }
        
        if (!userService.existsById(request.getUserId())) {
            errors.put("user_id", "El usuario no existe");
        }
        
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        
        Incident incident = new Incident();
        incident.setRouteId(request.getRouteId());
        incident.setSegmentId(request.getSegmentId());
        incident.setUserId(request.getUserId());
        incident.setIncidentType(request.getIncidentType());
        incident.setSeverity("high"); // Forzar severidad alta
        incident.setDescription(request.getDescription() != null ? 
            "ALERTA DE PÁNICO: " + request.getDescription() : 
            "ALERTA DE PÁNICO");
        incident.setLocationLat(request.getLocationLat());
        incident.setLocationLng(request.getLocationLng());
        incident.setLocationAddress(request.getLocationAddress());
        incident.setIsPanic(true);
        incident.setReportPhotoUrl(request.getReportPhotoUrl());
        
        Incident created = incidentService.createPanicAlert(incident);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "ALERTA DE PÁNICO ACTIVADA");
        response.put("incident", created);
        response.put("panicAlert", true);
        response.put("actionRequired", "Se ha notificado a las autoridades");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    // ============================================================
    // PUT ENDPOINTS
    // ============================================================
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody IncidentRequest request) {
        
        Incident existing = incidentService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Validar que no se pueda cambiar el estado de resolución
        // Solo se permite actualizar ciertos campos
        
        existing.setIncidentType(request.getIncidentType());
        existing.setSeverity(request.getSeverity());
        existing.setDescription(request.getDescription());
        existing.setLocationLat(request.getLocationLat());
        existing.setLocationLng(request.getLocationLng());
        existing.setLocationAddress(request.getLocationAddress());
        existing.setReportPhotoUrl(request.getReportPhotoUrl());
        
        Incident updated = incidentService.update(id, existing);
        return ResponseEntity.ok(updated);
    }
    
    @PutMapping("/{id}/resolve")
    public ResponseEntity<?> resolve(
            @PathVariable UUID id,
            @RequestBody(required = false) IncidentResolveRequest request) {
        
        Incident existing = incidentService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (existing.getIsResolved()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "El incidente ya está resuelto"));
        }
        
        String resolutionNotes = request != null ? request.getResolutionNotes() : null;
        String reportPhotoUrl = request != null ? request.getReportPhotoUrl() : null;
        
        Incident resolved = incidentService.resolve(id, resolutionNotes, reportPhotoUrl);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Incidente resuelto exitosamente");
        response.put("incident", resolved);
        response.put("resolvedAt", resolved.getResolvedAt());
        
        return ResponseEntity.ok(response);
    }
    
    // ============================================================
    // PATCH ENDPOINTS
    // ============================================================
    
    @PatchMapping("/{id}/severity")
    public ResponseEntity<?> updateSeverity(
            @PathVariable UUID id,
            @RequestBody Map<String, String> request) {
        
        String newSeverity = request.get("severity");
        if (newSeverity == null || newSeverity.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "El campo severity es requerido"));
        }
        
        Incident existing = incidentService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        existing.setSeverity(newSeverity);
        Incident updated = incidentService.update(id, existing);
        
        return ResponseEntity.ok(Map.of(
            "message", "Severidad actualizada",
            "incident", updated,
            "newSeverity", newSeverity
        ));
    }
    
    // ============================================================
    // DELETE ENDPOINTS
    // ============================================================
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!incidentService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        incidentService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/route/{routeId}")
    public ResponseEntity<?> deleteByRoute(@PathVariable UUID routeId) {
        List<Incident> incidents = incidentService.findByRouteId(routeId);
        if (incidents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "No se encontraron incidentes para esta ruta"));
        }
        
        incidentService.deleteByRouteId(routeId);
        
        return ResponseEntity.ok(Map.of(
            "message", "Incidentes eliminados exitosamente",
            "routeId", routeId,
            "deletedCount", incidents.size()
        ));
    }
}