package com.ngcapp.utpdevs.ngc_backend.services;

import com.ngcapp.utpdevs.ngc_backend.dtos.MovementStats;
import com.ngcapp.utpdevs.ngc_backend.models.MovementHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovementHistoryService {

  @Autowired
  private SupabaseCrudService crudService;

  private static final String TABLE = "movement_history";
  private static final double EARTH_RADIUS_KM = 6371.0;

  // Obtener todos los registros
  public List<MovementHistory> findAll() {
    return crudService.findAll(TABLE, MovementHistory[].class);
  }

  // Obtener por ID
  public MovementHistory findById(UUID id) {
    return crudService.findById(TABLE, id, MovementHistory[].class);
  }

  // Obtener por usuario
  public List<MovementHistory> findByUserId(UUID userId) {
    return crudService.find(
        TABLE, 
        "user_id=eq." + userId + "&order=recorded_at.desc", 
        MovementHistory[].class
    );
  }

  // Obtener por ruta
  public List<MovementHistory> findByRouteId(UUID routeId) {
    return crudService.find(
        TABLE, 
        "route_id=eq." + routeId + "&order=recorded_at.asc", 
        MovementHistory[].class
    );
  }

  // Obtener por usuario y ruta
  public List<MovementHistory> findByUserIdAndRouteId(UUID userId, UUID routeId) {
    return crudService.find(
        TABLE, 
        "user_id=eq." + userId + "&route_id=eq." + routeId + "&order=recorded_at.asc", 
        MovementHistory[].class
    );
  }

  // Obtener por rango de fechas
  public List<MovementHistory> findByDateRange(UUID userId, OffsetDateTime start, OffsetDateTime end) {
    return crudService.find(
        TABLE, 
        "user_id=eq." + userId + 
        "&recorded_at=gte." + start + 
        "&recorded_at=lte." + end + 
        "&order=recorded_at.asc", 
        MovementHistory[].class
    );
  }

  // Obtener por fecha
  public List<MovementHistory> findByDate(UUID userId, LocalDate date) {
    OffsetDateTime startOfDay = date.atStartOfDay().atOffset(ZoneOffset.UTC);
    OffsetDateTime endOfDay = date.atTime(23, 59, 59).atOffset(ZoneOffset.UTC);
    return findByDateRange(userId, startOfDay, endOfDay);
  }

  // Obtener últimos N registros de un usuario
  public List<MovementHistory> findLastNByUser(UUID userId, int limit) {
    return crudService.find(
        TABLE, 
        "user_id=eq." + userId + "&order=recorded_at.desc&limit=" + limit, 
        MovementHistory[].class
    );
  }

  // Obtener última ubicación de un usuario
  public MovementHistory findLastLocationByUser(UUID userId) {
    List<MovementHistory> result = findLastNByUser(userId, 1);
    return result.isEmpty() ? null : result.get(0);
  }

  // Obtener puntos de ruta (trayectoria)
  public List<MovementHistory> getRoutePath(UUID routeId) {
    return crudService.find(
        TABLE, 
        "route_id=eq." + routeId + "&order=recorded_at.asc", 
        MovementHistory[].class
    );
  }

  // Crear registro
  public MovementHistory create(MovementHistory movement) {
    return crudService.insert(TABLE, movement, MovementHistory[].class);
  }

  // Crear múltiples registros (batch)
  public List<MovementHistory> createBatch(List<MovementHistory> movements) {
    List<MovementHistory> created = new ArrayList<>();
    for (MovementHistory movement : movements) {
        MovementHistory result = crudService.insert(TABLE, movement, MovementHistory[].class);
        created.add(result);
    }
    return created;
  }

  // Actualizar
  public MovementHistory update(UUID id, MovementHistory movement) {
    return crudService.update(TABLE, id, movement, MovementHistory[].class);
  }

  // Eliminar
  public void delete(UUID id) {
    crudService.delete(TABLE, id);
  }

  // Eliminar por ruta
  public void deleteByRouteId(UUID routeId) {
    crudService.delete(TABLE, "route_id=eq." + routeId);
  }

  // Eliminar por usuario
  public void deleteByUserId(UUID userId) {
    crudService.delete(TABLE, "user_id=eq." + userId);
  }

  // Eliminar por rango de fechas
  public void deleteByDateRange(UUID userId, OffsetDateTime start, OffsetDateTime end) {
    crudService.delete(
        TABLE, 
        "user_id=eq." + userId + 
        "&recorded_at=gte." + start + 
        "&recorded_at=lte." + end
    );
  }

  // Verificar si existe
  public boolean existsById(UUID id) {
    return crudService.exists(TABLE, "id=eq." + id);
  }

  // Contar registros de un usuario
  public long countByUserId(UUID userId) {
    return crudService.count(TABLE, "user_id=eq." + userId);
  }

  // ============================================================
  // CÁLCULOS Y ESTADÍSTICAS
  // ============================================================

  // Calcular distancia entre dos puntos (Haversine formula)
  private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
    
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return EARTH_RADIUS_KM * c;
  }

  // Calcular distancia total de una ruta
  public BigDecimal calculateTotalDistance(UUID routeId) {
    List<MovementHistory> points = getRoutePath(routeId);
    return calculateTotalDistance(points);
  }

  // Calcular distancia total de una lista de puntos
  public BigDecimal calculateTotalDistance(List<MovementHistory> points) {
    if (points.size() < 2) {
        return BigDecimal.ZERO;
    }
    
    double totalDistance = 0.0;
    
    for (int i = 0; i < points.size() - 1; i++) {
        MovementHistory p1 = points.get(i);
        MovementHistory p2 = points.get(i + 1);
        
        double distance = calculateDistance(
            p1.getLatitude().doubleValue(),
            p1.getLongitude().doubleValue(),
            p2.getLatitude().doubleValue(),
            p2.getLongitude().doubleValue()
        );
        
        totalDistance += distance;
    }
    
    return BigDecimal.valueOf(totalDistance).setScale(2, RoundingMode.HALF_UP);
  }

  // Calcular estadísticas de movimiento
  public MovementStats calculateStats(UUID userId, OffsetDateTime start, OffsetDateTime end) {
    List<MovementHistory> points = findByDateRange(userId, start, end);
    return calculateStats(points);
  }

  // Calcular estadísticas de una lista de puntos
  public MovementStats calculateStats(List<MovementHistory> points) {
    MovementStats stats = new MovementStats();
    
    if (points.isEmpty()) {
        stats.setTotalPoints(0);
        stats.setTotalDistanceKm(BigDecimal.ZERO);
        stats.setAvgSpeedKmh(BigDecimal.ZERO);
        stats.setMaxSpeedKmh(BigDecimal.ZERO);
        stats.setTotalDurationMinutes(0);
        stats.setMovingTimeMinutes(0);
        stats.setStoppedTimeMinutes(0);
        stats.setAvgBatteryLevel(BigDecimal.ZERO);
        stats.setMinBatteryLevel(0);
        stats.setMaxBatteryLevel(0);
        return stats;
    }
    
    // Total de puntos
    stats.setTotalPoints(points.size());
    
    // Distancia total
    BigDecimal totalDistance = calculateTotalDistance(points);
    stats.setTotalDistanceKm(totalDistance);
    
    // Velocidades
    BigDecimal maxSpeed = points.stream()
        .filter(p -> p.getSpeed() != null)
        .map(MovementHistory::getSpeed)
        .max(BigDecimal::compareTo)
        .orElse(BigDecimal.ZERO);
    stats.setMaxSpeedKmh(maxSpeed);
    
    BigDecimal avgSpeed = points.stream()
        .filter(p -> p.getSpeed() != null)
        .map(MovementHistory::getSpeed)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .divide(BigDecimal.valueOf(points.stream().filter(p -> p.getSpeed() != null).count()), 
                2, RoundingMode.HALF_UP);
    stats.setAvgSpeedKmh(avgSpeed);
    
    // Duración total
    if (points.size() >= 2) {
        OffsetDateTime first = points.get(0).getRecordedAt();
        OffsetDateTime last = points.get(points.size() - 1).getRecordedAt();
        long durationMinutes = Duration.between(first, last).toMinutes();
        stats.setTotalDurationMinutes(durationMinutes);
        
        // Tiempo en movimiento vs detenido
        long movingTime = 0;
        long stoppedTime = 0;
        
        for (MovementHistory point : points) {
            if (point.getIsMoving() != null) {
                if (point.getIsMoving()) {
                    movingTime++;
                } else {
                    stoppedTime++;
                }
            }
        }
        
        // Estimación basada en puntos (cada punto representa un intervalo)
        if (movingTime + stoppedTime > 0) {
            long totalIntervals = movingTime + stoppedTime;
            stats.setMovingTimeMinutes((long) ((movingTime / (double) totalIntervals) * durationMinutes));
            stats.setStoppedTimeMinutes((long) ((stoppedTime / (double) totalIntervals) * durationMinutes));
        }
    }
    
    // Batería
    List<Integer> batteryLevels = points.stream()
        .filter(p -> p.getBatteryLevel() != null)
        .map(MovementHistory::getBatteryLevel)
        .collect(Collectors.toList());
    
    if (!batteryLevels.isEmpty()) {
        double avgBattery = batteryLevels.stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0.0);
        stats.setAvgBatteryLevel(BigDecimal.valueOf(avgBattery).setScale(2, RoundingMode.HALF_UP));
        
        stats.setMinBatteryLevel(Collections.min(batteryLevels));
        stats.setMaxBatteryLevel(Collections.max(batteryLevels));
    }
    
    return stats;
  }

  // Obtener resumen de movimiento de un usuario
  public Map<String, Object> getMovementSummary(UUID userId, LocalDate date) {
    List<MovementHistory> points = findByDate(userId, date);
    
    Map<String, Object> summary = new HashMap<>();
    summary.put("date", date.toString());
    summary.put("total_points", points.size());
    
    if (!points.isEmpty()) {
        MovementStats stats = calculateStats(points);
        summary.put("total_distance_km", stats.getTotalDistanceKm());
        summary.put("avg_speed_kmh", stats.getAvgSpeedKmh());
        summary.put("max_speed_kmh", stats.getMaxSpeedKmh());
        summary.put("total_duration_minutes", stats.getTotalDurationMinutes());
        summary.put("moving_time_minutes", stats.getMovingTimeMinutes());
        summary.put("stopped_time_minutes", stats.getStoppedTimeMinutes());
        summary.put("avg_battery_level", stats.getAvgBatteryLevel());
        summary.put("min_battery_level", stats.getMinBatteryLevel());
        summary.put("max_battery_level", stats.getMaxBatteryLevel());
        
        // Primera y última ubicación
        MovementHistory first = points.get(0);
        MovementHistory last = points.get(points.size() - 1);
        
        Map<String, Object> startLocation = new HashMap<>();
        startLocation.put("latitude", first.getLatitude());
        startLocation.put("longitude", first.getLongitude());
        startLocation.put("recorded_at", first.getRecordedAt());
        summary.put("start_location", startLocation);
        
        Map<String, Object> endLocation = new HashMap<>();
        endLocation.put("latitude", last.getLatitude());
        endLocation.put("longitude", last.getLongitude());
        endLocation.put("recorded_at", last.getRecordedAt());
        summary.put("end_location", endLocation);
    }
    
    return summary;
  }
}