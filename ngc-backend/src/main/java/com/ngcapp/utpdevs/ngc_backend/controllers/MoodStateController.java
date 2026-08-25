package com.ngcapp.utpdevs.ngc_backend.controllers;

import com.ngcapp.utpdevs.ngc_backend.dtos.MoodStateRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.MoodStats;
import com.ngcapp.utpdevs.ngc_backend.models.MoodState;
import com.ngcapp.utpdevs.ngc_backend.services.MoodStateService;
import com.ngcapp.utpdevs.ngc_backend.services.RouteService;
import com.ngcapp.utpdevs.ngc_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/moods")
@CrossOrigin(origins = "*")
public class MoodStateController {
    
    @Autowired
    private MoodStateService moodService;
    
    @Autowired
    private RouteService routeService;
    
    @Autowired
    private UserService userService;
    
    // ============================================================
    // GET ENDPOINTS
    // ============================================================
    
    @GetMapping
    public ResponseEntity<List<MoodState>> getAll() {
        return ResponseEntity.ok(moodService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MoodState> getById(@PathVariable UUID id) {
        MoodState mood = moodService.findById(id);
        if (mood == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mood);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MoodState>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(moodService.findByUserId(userId));
    }
    
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<MoodState>> getByRoute(@PathVariable UUID routeId) {
        return ResponseEntity.ok(moodService.findByRouteId(routeId));
    }
    
    @GetMapping("/user/{userId}/route/{routeId}")
    public ResponseEntity<List<MoodState>> getByUserAndRoute(
            @PathVariable UUID userId,
            @PathVariable UUID routeId) {
        return ResponseEntity.ok(moodService.findByUserIdAndRouteId(userId, routeId));
    }
    
    @GetMapping("/type/{moodType}")
    public ResponseEntity<List<MoodState>> getByType(@PathVariable String moodType) {
        return ResponseEntity.ok(moodService.findByMoodType(moodType));
    }
    
    @GetMapping("/value/{moodValue}")
    public ResponseEntity<List<MoodState>> getByValue(@PathVariable Integer moodValue) {
        return ResponseEntity.ok(moodService.findByMoodValue(moodValue));
    }
    
    @GetMapping("/value-range")
    public ResponseEntity<List<MoodState>> getByValueRange(
            @RequestParam Integer min,
            @RequestParam Integer max) {
        return ResponseEntity.ok(moodService.findByMoodValueRange(min, max));
    }
    
    @GetMapping("/date")
    public ResponseEntity<List<MoodState>> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(moodService.findByDate(date));
    }
    
    @GetMapping("/user/{userId}/last")
    public ResponseEntity<MoodState> getLastMoodByUser(@PathVariable UUID userId) {
        MoodState mood = moodService.findLastMoodByUser(userId);
        if (mood == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mood);
    }
    
    // ============================================================
    // STATS ENDPOINTS
    // ============================================================
    
    @GetMapping("/stats")
    public ResponseEntity<MoodStats> getStats() {
        return ResponseEntity.ok(moodService.getStats());
    }
    
    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<MoodStats> getStatsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(moodService.getStatsByUser(userId));
    }
    
    @GetMapping("/stats/route/{routeId}")
    public ResponseEntity<MoodStats> getStatsByRoute(@PathVariable UUID routeId) {
        return ResponseEntity.ok(moodService.getStatsByRoute(routeId));
    }
    
    @GetMapping("/stats/user/{userId}/route/{routeId}")
    public ResponseEntity<MoodStats> getStatsByUserAndRoute(
            @PathVariable UUID userId,
            @PathVariable UUID routeId) {
        return ResponseEntity.ok(moodService.getStatsByUserAndRoute(userId, routeId));
    }
    
    @GetMapping("/evolution")
    public ResponseEntity<Map<String, Double>> getEvolution(
            @RequestParam UUID userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(moodService.getMoodEvolution(userId, startDate, endDate));
    }
    
    // ============================================================
    // POST ENDPOINTS
    // ============================================================
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody MoodStateRequest request) {
        // Validar existencia de relaciones
        Map<String, String> errors = new HashMap<>();
        
        if (!userService.existsById(request.getUserId())) {
            errors.put("user_id", "El usuario no existe");
        }
        
        if (!routeService.existsById(request.getRouteId())) {
            errors.put("route_id", "La ruta no existe");
        }
        
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        
        MoodState mood = new MoodState();
        mood.setUserId(request.getUserId());
        mood.setRouteId(request.getRouteId());
        mood.setMoodType(request.getMoodType());
        mood.setMoodValue(request.getMoodValue());
        mood.setComment(request.getComment());
        mood.setLocationLat(request.getLocationLat());
        mood.setLocationLng(request.getLocationLng());
        
        MoodState created = moodService.create(mood);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // ============================================================
    // PUT ENDPOINTS
    // ============================================================
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody MoodStateRequest request) {
        
        MoodState existing = moodService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        // No permitir cambiar userId ni routeId
        existing.setMoodType(request.getMoodType());
        existing.setMoodValue(request.getMoodValue());
        existing.setComment(request.getComment());
        existing.setLocationLat(request.getLocationLat());
        existing.setLocationLng(request.getLocationLng());
        
        MoodState updated = moodService.update(id, existing);
        return ResponseEntity.ok(updated);
    }
    
    // ============================================================
    // DELETE ENDPOINTS
    // ============================================================
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!moodService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        moodService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/route/{routeId}")
    public ResponseEntity<?> deleteByRoute(@PathVariable UUID routeId) {
        List<MoodState> moods = moodService.findByRouteId(routeId);
        if (moods.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "No se encontraron estados de ánimo para esta ruta"));
        }
        
        moodService.deleteByRouteId(routeId);
        
        return ResponseEntity.ok(Map.of(
            "message", "Estados de ánimo eliminados exitosamente",
            "routeId", routeId,
            "deletedCount", moods.size()
        ));
    }
    
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteByUser(@PathVariable UUID userId) {
        List<MoodState> moods = moodService.findByUserId(userId);
        if (moods.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "No se encontraron estados de ánimo para este usuario"));
        }
        
        moodService.deleteByUserId(userId);
        
        return ResponseEntity.ok(Map.of(
            "message", "Estados de ánimo eliminados exitosamente",
            "userId", userId,
            "deletedCount", moods.size()
        ));
    }
}