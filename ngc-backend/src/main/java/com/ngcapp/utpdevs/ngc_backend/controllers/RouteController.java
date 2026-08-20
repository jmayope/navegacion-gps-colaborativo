package com.ngcapp.utpdevs.ngc_backend.controllers;

import com.ngcapp.utpdevs.ngc_backend.dtos.RouteRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.RouteStatusUpdate;
import com.ngcapp.utpdevs.ngc_backend.models.Route;
import com.ngcapp.utpdevs.ngc_backend.services.RouteService;
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
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
public class RouteController {
    
  @Autowired
  private RouteService routeService;
  
  // ============================================================
  // GET ENDPOINTS
  // ============================================================
  
  @GetMapping
  public ResponseEntity<List<Route>> getAll() {
      return ResponseEntity.ok(routeService.findAll());
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<Route> getById(@PathVariable UUID id) {
      Route route = routeService.findById(id);
      if (route == null) {
          return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(route);
  }
  
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Route>> getByUser(@PathVariable UUID userId) {
      return ResponseEntity.ok(routeService.findByUserId(userId));
  }
  
  @GetMapping("/user/{userId}/favorites")
  public ResponseEntity<List<Route>> getFavoritesByUser(@PathVariable UUID userId) {
      return ResponseEntity.ok(routeService.findFavoritesByUserId(userId));
  }
  
  @GetMapping("/status/{status}")
  public ResponseEntity<List<Route>> getByStatus(@PathVariable String status) {
      return ResponseEntity.ok(routeService.findByStatus(status));
  }
  
  @GetMapping("/user/{userId}/status/{status}")
  public ResponseEntity<List<Route>> getByUserAndStatus(
          @PathVariable UUID userId,
          @PathVariable String status) {
      return ResponseEntity.ok(routeService.findByUserIdAndStatus(userId, status));
  }
  
  // ============================================================
  // POST ENDPOINTS
  // ============================================================
  
  @PostMapping
  public ResponseEntity<?> create(@RequestBody RouteRequest request) {
      Route route = new Route();
      route.setUserId(request.getUserId());
      route.setOriginName(request.getOriginName());
      route.setOriginLat(request.getOriginLat());
      route.setOriginLng(request.getOriginLng());
      route.setOriginAddress(request.getOriginAddress());
      route.setDestinationName(request.getDestinationName());
      route.setDestinationLat(request.getDestinationLat());
      route.setDestinationLng(request.getDestinationLng());
      route.setDestinationAddress(request.getDestinationAddress());
      route.setEstimatedDistance(request.getEstimatedDistance());
      route.setEstimatedDuration(request.getEstimatedDuration());
      route.setIsFavorite(request.getIsFavorite() != null ? request.getIsFavorite() : false);
      route.setStatus("planning");
      
      Route created = routeService.create(route);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }
  
  // ============================================================
  // PUT ENDPOINTS
  // ============================================================
  
  @PutMapping("/{id}")
  public ResponseEntity<?> update(
          @PathVariable UUID id,
          @RequestBody RouteRequest request) {
      
      Route existing = routeService.findById(id);
      if (existing == null) {
          return ResponseEntity.notFound().build();
      }
      
      // Actualizar solo los campos permitidos
      existing.setOriginName(request.getOriginName());
      existing.setOriginLat(request.getOriginLat());
      existing.setOriginLng(request.getOriginLng());
      existing.setOriginAddress(request.getOriginAddress());
      existing.setDestinationName(request.getDestinationName());
      existing.setDestinationLat(request.getDestinationLat());
      existing.setDestinationLng(request.getDestinationLng());
      existing.setDestinationAddress(request.getDestinationAddress());
      existing.setEstimatedDistance(request.getEstimatedDistance());
      existing.setEstimatedDuration(request.getEstimatedDuration());
      existing.setIsFavorite(request.getIsFavorite() != null ? request.getIsFavorite() : false);
      
      // No actualizar: userId, status, fechas automáticas
      
      Route updated = routeService.update(id, existing);
      return ResponseEntity.ok(updated);
  }
  
  @PutMapping("/{id}/status")
  public ResponseEntity<?> updateStatus(
          @PathVariable UUID id,
          @RequestBody RouteStatusUpdate statusUpdate) {
      
      Route updated = routeService.updateStatus(id, statusUpdate.getStatus());
      if (updated == null) {
          return ResponseEntity.notFound().build();
      }
      
      Map<String, Object> response = new HashMap<>();
      response.put("message", "Estado actualizado exitosamente");
      response.put("route", updated);
      response.put("newStatus", updated.getStatus());
      
      return ResponseEntity.ok(response);
  }
  
  @PutMapping("/{id}/favorite")
  public ResponseEntity<?> toggleFavorite(@PathVariable UUID id) {
      Route updated = routeService.toggleFavorite(id);
      if (updated == null) {
          return ResponseEntity.notFound().build();
      }
      
      Map<String, Object> response = new HashMap<>();
      response.put("message", updated.getIsFavorite() ? "Marcado como favorito" : "Quitado de favoritos");
      response.put("route", updated);
      response.put("isFavorite", updated.getIsFavorite());
      
      return ResponseEntity.ok(response);
  }
  
  // ============================================================
  // PATCH ENDPOINTS (para actualizaciones parciales)
  // ============================================================
  
  @PatchMapping("/{id}/start")
  public ResponseEntity<?> startRoute(@PathVariable UUID id) {
      Route updated = routeService.updateStatus(id, "in_progress");
      if (updated == null) {
          return ResponseEntity.notFound().build();
      }
      
      Map<String, Object> response = new HashMap<>();
      response.put("message", "Ruta iniciada");
      response.put("route", updated);
      response.put("startedAt", updated.getStartedAt());
      
      return ResponseEntity.ok(response);
  }
  
  @PatchMapping("/{id}/complete")
  public ResponseEntity<?> completeRoute(@PathVariable UUID id) {
      Route updated = routeService.updateStatus(id, "completed");
      if (updated == null) {
          return ResponseEntity.notFound().build();
      }
      
      Map<String, Object> response = new HashMap<>();
      response.put("message", "Ruta completada");
      response.put("route", updated);
      response.put("completedAt", updated.getCompletedAt());
      
      return ResponseEntity.ok(response);
  }
  
  @PatchMapping("/{id}/cancel")
  public ResponseEntity<?> cancelRoute(@PathVariable UUID id) {
      Route updated = routeService.updateStatus(id, "cancelled");
      if (updated == null) {
          return ResponseEntity.notFound().build();
      }
      
      Map<String, Object> response = new HashMap<>();
      response.put("message", "Ruta cancelada");
      response.put("route", updated);
      
      return ResponseEntity.ok(response);
  }
  
  // ============================================================
  // DELETE ENDPOINTS
  // ============================================================
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
      if (!routeService.existsById(id)) {
          return ResponseEntity.notFound().build();
      }
      routeService.delete(id);
      return ResponseEntity.noContent().build();
  }
  
  @DeleteMapping("/user/{userId}")
  public ResponseEntity<?> deleteAllByUser(@PathVariable UUID userId) {
      long count = routeService.countByUserId(userId);
      if (count == 0) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(Map.of("message", "No se encontraron rutas para este usuario"));
      }
      
      routeService.deleteByUserId(userId);
      return ResponseEntity.ok(Map.of(
          "message", "Todas las rutas del usuario fueron eliminadas",
          "deletedCount", count
      ));
  }
}