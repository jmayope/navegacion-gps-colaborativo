package com.ngcapp.utpdevs.ngc_backend.controllers;

import com.ngcapp.utpdevs.ngc_backend.dtos.QueryStats;
import com.ngcapp.utpdevs.ngc_backend.dtos.RouteQueryRequest;
import com.ngcapp.utpdevs.ngc_backend.models.RouteQuery;
import com.ngcapp.utpdevs.ngc_backend.services.RouteQueryService;
import com.ngcapp.utpdevs.ngc_backend.services.RouteService;
import com.ngcapp.utpdevs.ngc_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/route-queries")
@CrossOrigin(origins = "*")
public class RouteQueryController {
    
    @Autowired
    private RouteQueryService queryService;
    
    @Autowired
    private RouteService routeService;
    
    @Autowired
    private UserService userService;
    
    // ============================================================
    // GET ENDPOINTS
    // ============================================================
    
    @GetMapping
    public ResponseEntity<List<RouteQuery>> getAll() {
        return ResponseEntity.ok(queryService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RouteQuery> getById(@PathVariable UUID id) {
        RouteQuery query = queryService.findById(id);
        if (query == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(query);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RouteQuery>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(queryService.findByUserId(userId));
    }
    
    @GetMapping("/type/{queryType}")
    public ResponseEntity<List<RouteQuery>> getByType(@PathVariable String queryType) {
        return ResponseEntity.ok(queryService.findByQueryType(queryType));
    }
    
    @GetMapping("/user/{userId}/type/{queryType}")
    public ResponseEntity<List<RouteQuery>> getByUserAndType(
            @PathVariable UUID userId,
            @PathVariable String queryType) {
        return ResponseEntity.ok(queryService.findByUserIdAndType(userId, queryType));
    }
    
    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<RouteQuery>> getRecent(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(queryService.findRecentByUser(userId, limit));
    }
    
    @GetMapping("/user/{userId}/date")
    public ResponseEntity<List<RouteQuery>> getByDate(
            @PathVariable UUID userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(queryService.findByDate(userId, date));
    }
    
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<RouteQuery>> getByDateRange(
            @PathVariable UUID userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end) {
        return ResponseEntity.ok(queryService.findByDateRange(userId, start, end));
    }
    
    @GetMapping("/with-results")
    public ResponseEntity<List<RouteQuery>> getWithResults() {
        return ResponseEntity.ok(queryService.findWithResults());
    }
    
    @GetMapping("/with-selection")
    public ResponseEntity<List<RouteQuery>> getWithSelection() {
        return ResponseEntity.ok(queryService.findWithSelection());
    }
    
    // ============================================================
    // STATS ENDPOINTS
    // ============================================================
    
    @GetMapping("/stats")
    public ResponseEntity<QueryStats> getStats() {
        return ResponseEntity.ok(queryService.getStats());
    }
    
    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<QueryStats> getStatsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(queryService.getStatsByUser(userId));
    }
    
    @GetMapping("/stats/type/{queryType}")
    public ResponseEntity<QueryStats> getStatsByType(@PathVariable String queryType) {
        return ResponseEntity.ok(queryService.getStatsByType(queryType));
    }
    
    @GetMapping("/stats/user/{userId}/range")
    public ResponseEntity<QueryStats> getStatsByDateRange(
            @PathVariable UUID userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end) {
        return ResponseEntity.ok(queryService.getStatsByDateRange(userId, start, end));
    }
    
    @GetMapping("/popular/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getPopularQueries(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(queryService.getPopularQueries(userId, limit));
    }
    
    @GetMapping("/activity/{userId}")
    public ResponseEntity<Map<Integer, Long>> getActivityByHour(
            @PathVariable UUID userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(queryService.getActivityByHour(userId, date));
    }
    
    // ============================================================
    // POST ENDPOINTS
    // ============================================================
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody RouteQueryRequest request) {
        // Validar existencia
        Map<String, String> errors = new HashMap<>();
        
        if (!userService.existsById(request.getUserId())) {
            errors.put("user_id", "El usuario no existe");
        }
        
        if (request.getSelectedRouteId() != null && 
            !routeService.existsById(request.getSelectedRouteId())) {
            errors.put("selected_route_id", "La ruta seleccionada no existe");
        }
        
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        
        RouteQuery query = new RouteQuery();
        query.setUserId(request.getUserId());
        query.setQueryType(request.getQueryType());
        query.setOriginLat(request.getOriginLat());
        query.setOriginLng(request.getOriginLng());
        query.setDestinationLat(request.getDestinationLat());
        query.setDestinationLng(request.getDestinationLng());
        query.setQueryFilters(request.getQueryFilters());
        query.setResultCount(request.getResultCount());
        query.setResponseTimeMs(request.getResponseTimeMs());
        query.setSelectedRouteId(request.getSelectedRouteId());
        query.setIpAddress(request.getIpAddress());
        query.setUserAgent(request.getUserAgent());
        
        RouteQuery created = queryService.create(query);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // ============================================================
    // PUT ENDPOINTS
    // ============================================================
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody RouteQueryRequest request) {
        
        RouteQuery existing = queryService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Actualizar campos permitidos
        existing.setQueryType(request.getQueryType());
        existing.setOriginLat(request.getOriginLat());
        existing.setOriginLng(request.getOriginLng());
        existing.setDestinationLat(request.getDestinationLat());
        existing.setDestinationLng(request.getDestinationLng());
        existing.setQueryFilters(request.getQueryFilters());
        existing.setResultCount(request.getResultCount());
        existing.setResponseTimeMs(request.getResponseTimeMs());
        existing.setSelectedRouteId(request.getSelectedRouteId());
        existing.setUserAgent(request.getUserAgent());
        
        // No permitir cambiar userId ni ipAddress
        
        RouteQuery updated = queryService.update(id, existing);
        return ResponseEntity.ok(updated);
    }
    
    @PutMapping("/{id}/select-route/{routeId}")
    public ResponseEntity<?> selectRoute(
            @PathVariable UUID id,
            @PathVariable UUID routeId) {
        
        RouteQuery query = queryService.findById(id);
        if (query == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (!routeService.existsById(routeId)) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "La ruta seleccionada no existe"));
        }
        
        query.setSelectedRouteId(routeId);
        RouteQuery updated = queryService.update(id, query);
        
        return ResponseEntity.ok(Map.of(
            "message", "Ruta seleccionada registrada exitosamente",
            "query", updated,
            "selectedRouteId", routeId
        ));
    }
    
    // ============================================================
    // DELETE ENDPOINTS
    // ============================================================
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!queryService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        queryService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteByUser(@PathVariable UUID userId) {
        long count = queryService.countByUserId(userId);
        if (count == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "No se encontraron consultas para este usuario"));
        }
        
        queryService.deleteByUserId(userId);
        
        return ResponseEntity.ok(Map.of(
            "message", "Consultas eliminadas exitosamente",
            "userId", userId,
            "deletedCount", count
        ));
    }
    
    @DeleteMapping("/user/{userId}/range")
    public ResponseEntity<?> deleteByDateRange(
            @PathVariable UUID userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end) {
        
        List<RouteQuery> queries = queryService.findByDateRange(userId, start, end);
        if (queries.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "No se encontraron consultas en el rango de fechas"));
        }
        
        queryService.deleteByDateRange(userId, start, end);
        
        return ResponseEntity.ok(Map.of(
            "message", "Consultas eliminadas exitosamente",
            "userId", userId,
            "start", start,
            "end", end,
            "deletedCount", queries.size()
        ));
    }
}