package com.ngcapp.utpdevs.ngc_backend.controllers;

import com.ngcapp.utpdevs.ngc_backend.dtos.SegmentBatchRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.SegmentRequest;
import com.ngcapp.utpdevs.ngc_backend.models.Segment;
import com.ngcapp.utpdevs.ngc_backend.services.RouteService;
import com.ngcapp.utpdevs.ngc_backend.services.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/segments")
@CrossOrigin(origins = "*")
public class SegmentController {
    
    @Autowired
    private SegmentService segmentService;
    
    @Autowired
    private RouteService routeService;
    
    // ============================================================
    // GET ENDPOINTS
    // ============================================================
    
    @GetMapping
    public ResponseEntity<List<Segment>> getAll() {
        return ResponseEntity.ok(segmentService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Segment> getById(@PathVariable UUID id) {
        Segment segment = segmentService.findById(id);
        if (segment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(segment);
    }
    
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<Segment>> getByRoute(@PathVariable UUID routeId) {
        return ResponseEntity.ok(segmentService.findByRouteId(routeId));
    }
    
    @GetMapping("/road-type/{roadType}")
    public ResponseEntity<List<Segment>> getByRoadType(@PathVariable String roadType) {
        return ResponseEntity.ok(segmentService.findByRoadType(roadType));
    }
    
    // ============================================================
    // POST ENDPOINTS
    // ============================================================
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SegmentRequest request) {
        // Verificar que la ruta existe
        if (!routeService.existsById(request.getRouteId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "La ruta no existe"));
        }
        
        Segment segment = new Segment();
        segment.setRouteId(request.getRouteId());
        segment.setSequenceNumber(
            request.getSequenceNumber() != null ? 
            request.getSequenceNumber() : 
            segmentService.getNextSequenceNumber(request.getRouteId())
        );
        segment.setStartLat(request.getStartLat());
        segment.setStartLng(request.getStartLng());
        segment.setEndLat(request.getEndLat());
        segment.setEndLng(request.getEndLng());
        segment.setDistance(request.getDistance());
        segment.setDuration(request.getDuration());
        segment.setStreetName(request.getStreetName());
        segment.setRoadType(request.getRoadType());
        segment.setSpeedLimit(request.getSpeedLimit());
        
        Segment created = segmentService.create(segment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PostMapping("/batch")
    public ResponseEntity<?> createBatch(@RequestBody SegmentBatchRequest request) {
        // Verificar que la ruta existe
        if (!routeService.existsById(request.getRouteId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "La ruta no existe"));
        }
        
        List<Segment> segments = new ArrayList<>();
        int sequenceNumber = 1;
        
        for (SegmentRequest segRequest : request.getSegments()) {
            Segment segment = new Segment();
            segment.setRouteId(request.getRouteId());
            segment.setSequenceNumber(sequenceNumber++);
            segment.setStartLat(segRequest.getStartLat());
            segment.setStartLng(segRequest.getStartLng());
            segment.setEndLat(segRequest.getEndLat());
            segment.setEndLng(segRequest.getEndLng());
            segment.setDistance(segRequest.getDistance());
            segment.setDuration(segRequest.getDuration());
            segment.setStreetName(segRequest.getStreetName());
            segment.setRoadType(segRequest.getRoadType());
            segment.setSpeedLimit(segRequest.getSpeedLimit());
            
            segments.add(segment);
        }
        
        List<Segment> created = segmentService.createBatch(segments);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Segmentos creados exitosamente");
        response.put("count", created.size());
        response.put("segments", created);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    // ============================================================
    // PUT ENDPOINTS
    // ============================================================
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody SegmentRequest request) {
        
        Segment existing = segmentService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Actualizar solo los campos permitidos
        existing.setStartLat(request.getStartLat());
        existing.setStartLng(request.getStartLng());
        existing.setEndLat(request.getEndLat());
        existing.setEndLng(request.getEndLng());
        existing.setDistance(request.getDistance());
        existing.setDuration(request.getDuration());
        existing.setStreetName(request.getStreetName());
        existing.setRoadType(request.getRoadType());
        existing.setSpeedLimit(request.getSpeedLimit());
        
        // No actualizar: routeId, sequenceNumber (a menos que se permita)
        if (request.getSequenceNumber() != null) {
            existing.setSequenceNumber(request.getSequenceNumber());
        }
        
        Segment updated = segmentService.update(id, existing);
        return ResponseEntity.ok(updated);
    }
    
    @PutMapping("/reorder/{routeId}")
    public ResponseEntity<?> reorderSegments(
            @PathVariable UUID routeId,
            @RequestBody List<UUID> segmentIds) {
        
        // Verificar que la ruta existe
        if (!routeService.existsById(routeId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "La ruta no existe"));
        }
        
        // Verificar que todos los segmentos pertenecen a la ruta
        List<Segment> existingSegments = segmentService.findByRouteId(routeId);
        if (existingSegments.size() != segmentIds.size()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "La cantidad de segmentos no coincide"));
        }
        
        List<Segment> reordered = segmentService.reorderSegments(routeId, segmentIds);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Segmentos reordenados exitosamente");
        response.put("routeId", routeId);
        response.put("segments", reordered);
        
        return ResponseEntity.ok(response);
    }
    
    // ============================================================
    // DELETE ENDPOINTS
    // ============================================================
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!segmentService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        segmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/route/{routeId}")
    public ResponseEntity<?> deleteByRoute(@PathVariable UUID routeId) {
        // Verificar que la ruta existe
        if (!routeService.existsById(routeId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "La ruta no existe"));
        }
        
        long count = segmentService.countByRouteId(routeId);
        if (count == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "No se encontraron segmentos para esta ruta"));
        }
        
        segmentService.deleteByRouteId(routeId);
        
        return ResponseEntity.ok(Map.of(
            "message", "Segmentos eliminados exitosamente",
            "routeId", routeId,
            "deletedCount", count
        ));
    }
}