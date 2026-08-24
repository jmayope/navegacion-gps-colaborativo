package com.ngcapp.utpdevs.ngc_backend.controllers;

import com.ngcapp.utpdevs.ngc_backend.dtos.MovementBatchRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.MovementHistoryRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.MovementPoint;
import com.ngcapp.utpdevs.ngc_backend.dtos.MovementStats;
import com.ngcapp.utpdevs.ngc_backend.models.MovementHistory;
import com.ngcapp.utpdevs.ngc_backend.services.MovementHistoryService;
import com.ngcapp.utpdevs.ngc_backend.services.RouteService;
import com.ngcapp.utpdevs.ngc_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/movement")
@CrossOrigin(origins = "*")
public class MovementHistoryController {

  @Autowired
  private MovementHistoryService movementService;

  @Autowired
  private RouteService routeService;

  @Autowired
  private UserService userService;

  // ============================================================
  // GET ENDPOINTS
  // ============================================================

  @GetMapping
  public ResponseEntity<List<MovementHistory>> getAll() {
    return ResponseEntity.ok(movementService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<MovementHistory> getById(@PathVariable UUID id) {
    MovementHistory movement = movementService.findById(id);
    if (movement == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(movement);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<MovementHistory>> getByUser(@PathVariable UUID userId) {
    return ResponseEntity.ok(movementService.findByUserId(userId));
  }

  @GetMapping("/route/{routeId}")
  public ResponseEntity<List<MovementHistory>> getByRoute(@PathVariable UUID routeId) {
    return ResponseEntity.ok(movementService.findByRouteId(routeId));
  }

  @GetMapping("/user/{userId}/route/{routeId}")
  public ResponseEntity<List<MovementHistory>> getByUserAndRoute(
        @PathVariable UUID userId,
        @PathVariable UUID routeId) {
    return ResponseEntity.ok(movementService.findByUserIdAndRouteId(userId, routeId));
  }

  @GetMapping("/user/{userId}/last")
  public ResponseEntity<MovementHistory> getLastLocation(@PathVariable UUID userId) {
    MovementHistory location = movementService.findLastLocationByUser(userId);
    if (location == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(location);
  }

  @GetMapping("/user/{userId}/last/{limit}")
  public ResponseEntity<List<MovementHistory>> getLastNLocations(
        @PathVariable UUID userId,
        @PathVariable int limit) {
    return ResponseEntity.ok(movementService.findLastNByUser(userId, limit));
  }

  @GetMapping("/user/{userId}/date")
  public ResponseEntity<List<MovementHistory>> getByDate(
        @PathVariable UUID userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return ResponseEntity.ok(movementService.findByDate(userId, date));
  }

  @GetMapping("/user/{userId}/range")
  public ResponseEntity<List<MovementHistory>> getByDateRange(
        @PathVariable UUID userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end) {
    return ResponseEntity.ok(movementService.findByDateRange(userId, start, end));
  }

  // ============================================================
  // STATS ENDPOINTS
  // ============================================================

  @GetMapping("/stats")
  public ResponseEntity<MovementStats> getStats(
        @RequestParam UUID userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end) {
    return ResponseEntity.ok(movementService.calculateStats(userId, start, end));
  }

  @GetMapping("/stats/route/{routeId}")
  public ResponseEntity<MovementStats> getStatsByRoute(@PathVariable UUID routeId) {
    List<MovementHistory> points = movementService.findByRouteId(routeId);
    return ResponseEntity.ok(movementService.calculateStats(points));
  }

  @GetMapping("/distance/route/{routeId}")
  public ResponseEntity<Map<String, Object>> getRouteDistance(@PathVariable UUID routeId) {
    BigDecimal distance = movementService.calculateTotalDistance(routeId);
    
    Map<String, Object> response = new HashMap<>();
    response.put("routeId", routeId);
    response.put("totalDistanceKm", distance);
    response.put("points", movementService.getRoutePath(routeId).size());
    
    return ResponseEntity.ok(response);
  }

  @GetMapping("/summary")
  public ResponseEntity<Map<String, Object>> getSummary(
        @RequestParam UUID userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return ResponseEntity.ok(movementService.getMovementSummary(userId, date));
  }

  // ============================================================
  // POST ENDPOINTS
  // ============================================================

  @PostMapping
  public ResponseEntity<?> create(@RequestBody MovementHistoryRequest request) {
    // Validar existencia
    Map<String, String> errors = new HashMap<>();
    
    if (!userService.existsById(request.getUserId())) {
        errors.put("user_id", "El usuario no existe");
    }
    
    if (request.getRouteId() != null && !routeService.existsById(request.getRouteId())) {
        errors.put("route_id", "La ruta no existe");
    }
    
    if (!errors.isEmpty()) {
        return ResponseEntity.badRequest().body(errors);
    }
    
    MovementHistory movement = new MovementHistory();
    movement.setUserId(request.getUserId());
    movement.setRouteId(request.getRouteId());
    movement.setLatitude(request.getLatitude());
    movement.setLongitude(request.getLongitude());
    movement.setAltitude(request.getAltitude());
    movement.setSpeed(request.getSpeed());
    movement.setHeading(request.getHeading());
    movement.setAccuracy(request.getAccuracy());
    movement.setIsMoving(request.getIsMoving());
    movement.setBatteryLevel(request.getBatteryLevel());
    movement.setRecordedAt(request.getRecordedAt());
    
    MovementHistory created = movementService.create(movement);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PostMapping("/batch")
  public ResponseEntity<?> createBatch(@RequestBody MovementBatchRequest request) {
    // Validar existencia
    Map<String, String> errors = new HashMap<>();
    
    if (!userService.existsById(request.getUserId())) {
        errors.put("user_id", "El usuario no existe");
    }
    
    if (request.getRouteId() != null && !routeService.existsById(request.getRouteId())) {
        errors.put("route_id", "La ruta no existe");
    }
    
    if (request.getPoints() == null || request.getPoints().isEmpty()) {
        errors.put("points", "Se requiere al menos un punto");
    }
    
    if (!errors.isEmpty()) {
        return ResponseEntity.badRequest().body(errors);
    }
    
    List<MovementHistory> movements = new ArrayList<>();
    
    for (MovementPoint point : request.getPoints()) {
        MovementHistory movement = new MovementHistory();
        movement.setUserId(request.getUserId());
        movement.setRouteId(request.getRouteId());
        movement.setLatitude(point.getLatitude());
        movement.setLongitude(point.getLongitude());
        movement.setAltitude(point.getAltitude());
        movement.setSpeed(point.getSpeed());
        movement.setHeading(point.getHeading());
        movement.setAccuracy(point.getAccuracy());
        movement.setIsMoving(point.getIsMoving());
        movement.setBatteryLevel(point.getBatteryLevel());
        movement.setRecordedAt(point.getRecordedAt());
        
        movements.add(movement);
    }
    
    List<MovementHistory> created = movementService.createBatch(movements);
    
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Puntos de movimiento registrados exitosamente");
    response.put("count", created.size());
    response.put("userId", request.getUserId());
    response.put("routeId", request.getRouteId());
    response.put("points", created);
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // ============================================================
  // PUT ENDPOINTS
  // ============================================================

  @PutMapping("/{id}")
  public ResponseEntity<?> update(
        @PathVariable UUID id,
        @RequestBody MovementHistoryRequest request) {
    
    MovementHistory existing = movementService.findById(id);
    if (existing == null) {
        return ResponseEntity.notFound().build();
    }
    
    // No permitir cambiar userId
    existing.setRouteId(request.getRouteId());
    existing.setLatitude(request.getLatitude());
    existing.setLongitude(request.getLongitude());
    existing.setAltitude(request.getAltitude());
    existing.setSpeed(request.getSpeed());
    existing.setHeading(request.getHeading());
    existing.setAccuracy(request.getAccuracy());
    existing.setIsMoving(request.getIsMoving());
    existing.setBatteryLevel(request.getBatteryLevel());
    existing.setRecordedAt(request.getRecordedAt());
    
    MovementHistory updated = movementService.update(id, existing);
    return ResponseEntity.ok(updated);
  }

  // ============================================================
  // DELETE ENDPOINTS
  // ============================================================

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    if (!movementService.existsById(id)) {
        return ResponseEntity.notFound().build();
    }
    movementService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/route/{routeId}")
  public ResponseEntity<?> deleteByRoute(@PathVariable UUID routeId) {
    List<MovementHistory> movements = movementService.findByRouteId(routeId);
    if (movements.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", "No se encontraron registros para esta ruta"));
    }
    
    movementService.deleteByRouteId(routeId);
    
    return ResponseEntity.ok(Map.of(
        "message", "Registros eliminados exitosamente",
        "routeId", routeId,
        "deletedCount", movements.size()
    ));
  }

  @DeleteMapping("/user/{userId}")
  public ResponseEntity<?> deleteByUser(@PathVariable UUID userId) {
    List<MovementHistory> movements = movementService.findByUserId(userId);
    if (movements.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", "No se encontraron registros para este usuario"));
    }
    
    movementService.deleteByUserId(userId);
    
    return ResponseEntity.ok(Map.of(
        "message", "Registros eliminados exitosamente",
        "userId", userId,
        "deletedCount", movements.size()
    ));
  }

  @DeleteMapping("/user/{userId}/range")
  public ResponseEntity<?> deleteByDateRange(
        @PathVariable UUID userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end) {
    
    List<MovementHistory> movements = movementService.findByDateRange(userId, start, end);
    if (movements.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", "No se encontraron registros en el rango de fechas"));
    }
    
    movementService.deleteByDateRange(userId, start, end);
    
    return ResponseEntity.ok(Map.of(
        "message", "Registros eliminados exitosamente",
        "userId", userId,
        "start", start,
        "end", end,
        "deletedCount", movements.size()
    ));
  }
}