package com.ngcapp.utpdevs.ngc_backend.controllers;

import com.ngcapp.utpdevs.ngc_backend.dtos.SessionPingRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.SessionStartRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.SessionStats;
import com.ngcapp.utpdevs.ngc_backend.models.UserSession;
import com.ngcapp.utpdevs.ngc_backend.services.UserSessionService;
import com.ngcapp.utpdevs.ngc_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sessions")
@CrossOrigin(origins = "*")
public class UserSessionController {
    
    @Autowired
    private UserSessionService sessionService;
    
    @Autowired
    private UserService userService;
    
    // ============================================================
    // GET ENDPOINTS
    // ============================================================
    
    @GetMapping
    public ResponseEntity<List<UserSession>> getAll() {
        return ResponseEntity.ok(sessionService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserSession> getById(@PathVariable UUID id) {
        UserSession session = sessionService.findById(id);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(session);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSession>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(sessionService.findByUserId(userId));
    }
    
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<UserSession>> getActiveByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(sessionService.findActiveByUserId(userId));
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<UserSession>> getActive() {
        return ResponseEntity.ok(sessionService.findActiveSessions());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserSession>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(sessionService.findByStatus(status));
    }
    
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<UserSession>> getByDevice(@PathVariable String deviceId) {
        return ResponseEntity.ok(sessionService.findByDeviceId(deviceId));
    }
    
    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<UserSession> getLatestActive(@PathVariable UUID userId) {
        UserSession session = sessionService.findLatestActiveByUser(userId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(session);
    }
    
    @GetMapping("/{id}/active")
    public ResponseEntity<Map<String, Boolean>> isSessionActive(@PathVariable UUID id) {
        boolean isActive = sessionService.isSessionActive(id);
        return ResponseEntity.ok(Map.of("isActive", isActive));
    }
    
    // ============================================================
    // STATS ENDPOINTS
    // ============================================================
    
    @GetMapping("/stats")
    public ResponseEntity<SessionStats> getStats() {
        return ResponseEntity.ok(sessionService.getStats());
    }
    
    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<SessionStats> getStatsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(sessionService.getStatsByUser(userId));
    }
    
    @GetMapping("/count/active")
    public ResponseEntity<Map<String, Long>> getActiveCount() {
        long count = sessionService.countActiveSessions();
        return ResponseEntity.ok(Map.of("activeSessions", count));
    }
    
    // ============================================================
    // POST ENDPOINTS
    // ============================================================
    
    @PostMapping("/start")
    public ResponseEntity<?> startSession(@RequestBody SessionStartRequest request) {
        // Validar usuario
        if (!userService.existsById(request.getUserId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "El usuario no existe"));
        }
        
        UserSession session = new UserSession();
        session.setUserId(request.getUserId());
        session.setDeviceId(request.getDeviceId());
        session.setDeviceName(request.getDeviceName());
        session.setDeviceOs(request.getDeviceOs());
        session.setAppVersion(request.getAppVersion());
        
        UserSession created = sessionService.startSession(session);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Sesión iniciada exitosamente");
        response.put("session", created);
        response.put("sessionId", created.getId());
        response.put("timeoutMinutes", 30); // Configurable
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/ping")
    public ResponseEntity<?> pingSession(@RequestBody SessionPingRequest request) {
        UserSession session = sessionService.pingSession(request.getSessionId());
        
        if (session == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Sesión no encontrada o inactiva"));
        }
        
        return ResponseEntity.ok(Map.of(
            "message", "Ping recibido",
            "sessionId", session.getId(),
            "lastPingAt", session.getLastPingAt(),
            "status", session.getSessionStatus()
        ));
    }
    
    @PostMapping("/{id}/end")
    public ResponseEntity<?> endSession(@PathVariable UUID id) {
        UserSession session = sessionService.endSession(id);
        
        if (session == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Sesión no encontrada"));
        }
        
        return ResponseEntity.ok(Map.of(
            "message", "Sesión finalizada exitosamente",
            "session", session,
            "endedAt", session.getEndedAt()
        ));
    }
    
    @PostMapping("/user/{userId}/end-all")
    public ResponseEntity<?> endAllSessions(@PathVariable UUID userId) {
        List<UserSession> sessions = sessionService.endAllSessionsByUser(userId);
        
        return ResponseEntity.ok(Map.of(
            "message", "Todas las sesiones finalizadas",
            "userId", userId,
            "sessionsEnded", sessions.size()
        ));
    }
    
    // ============================================================
    // ADMIN ENDPOINTS
    // ============================================================
    
    @PostMapping("/admin/expire")
    public ResponseEntity<?> expireInactiveSessions() {
        int expired = sessionService.expireInactiveSessions();
        return ResponseEntity.ok(Map.of(
            "message", "Sesiones expiradas procesadas",
            "expiredCount", expired
        ));
    }
    
    @PostMapping("/admin/cleanup")
    public ResponseEntity<?> cleanupExpired() {
        int deleted = sessionService.deleteExpiredSessions();
        return ResponseEntity.ok(Map.of(
            "message", "Sesiones expiradas eliminadas",
            "deletedCount", deleted
        ));
    }
    
    // ============================================================
    // DELETE ENDPOINTS
    // ============================================================
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!sessionService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteByUser(@PathVariable UUID userId) {
        List<UserSession> sessions = sessionService.findByUserId(userId);
        if (sessions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "No se encontraron sesiones para este usuario"));
        }
        
        sessionService.deleteByUserId(userId);
        
        return ResponseEntity.ok(Map.of(
            "message", "Sesiones eliminadas exitosamente",
            "userId", userId,
            "deletedCount", sessions.size()
        ));
    }
}