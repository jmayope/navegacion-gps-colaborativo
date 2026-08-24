package com.ngcapp.utpdevs.ngc_backend.services;

import com.ngcapp.utpdevs.ngc_backend.dtos.SessionStats;
import com.ngcapp.utpdevs.ngc_backend.models.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserSessionService {
    
    @Autowired
    private SupabaseCrudService crudService;
    
    @Value("${session.timeout.minutes:30}")
    private int sessionTimeoutMinutes;
    
    private static final String TABLE = "user_sessions";
    
    // Obtener todas las sesiones
    public List<UserSession> findAll() {
        return crudService.findAll(TABLE, UserSession[].class);
    }
    
    // Obtener sesión por ID
    public UserSession findById(UUID id) {
        return crudService.findById(TABLE, id, UserSession[].class);
    }
    
    // Obtener sesiones por usuario
    public List<UserSession> findByUserId(UUID userId) {
        return crudService.find(
            TABLE, 
            "user_id=eq." + userId + "&order=created_at.desc", 
            UserSession[].class
        );
    }
    
    // Obtener sesiones activas de un usuario
    public List<UserSession> findActiveByUserId(UUID userId) {
        return crudService.find(
            TABLE, 
            "user_id=eq." + userId + "&session_status=eq.active&order=created_at.desc", 
            UserSession[].class
        );
    }
    
    // Obtener sesiones activas
    public List<UserSession> findActiveSessions() {
        return crudService.find(
            TABLE, 
            "session_status=eq.active&order=last_ping_at.desc", 
            UserSession[].class
        );
    }
    
    // Obtener sesiones por estado
    public List<UserSession> findByStatus(String status) {
        return crudService.find(
            TABLE, 
            "session_status=eq." + status + "&order=created_at.desc", 
            UserSession[].class
        );
    }
    
    // Obtener sesiones por dispositivo
    public List<UserSession> findByDeviceId(String deviceId) {
        return crudService.find(
            TABLE, 
            "device_id=eq." + deviceId + "&order=created_at.desc", 
            UserSession[].class
        );
    }
    
    // Obtener sesión activa más reciente de un usuario
    public UserSession findLatestActiveByUser(UUID userId) {
        List<UserSession> sessions = findActiveByUserId(userId);
        return sessions.isEmpty() ? null : sessions.get(0);
    }
    
    // Iniciar sesión
    public UserSession startSession(UserSession session) {
        // Finalizar sesiones activas previas del mismo usuario
        List<UserSession> activeSessions = findActiveByUserId(session.getUserId());
        for (UserSession active : activeSessions) {
            active.setSessionStatus("inactive");
            active.setEndedAt(OffsetDateTime.now());
            update(active.getId(), active);
        }
        
        session.setSessionStatus("active");
        session.setStartedAt(OffsetDateTime.now());
        session.setLastPingAt(OffsetDateTime.now());
        
        return crudService.insert(TABLE, session, UserSession[].class);
    }
    
    // Actualizar ping (heartbeat)
    public UserSession pingSession(UUID sessionId) {
        UserSession session = findById(sessionId);
        if (session == null) {
            return null;
        }
        
        if (!"active".equals(session.getSessionStatus())) {
            return null;
        }
        
        session.setLastPingAt(OffsetDateTime.now());
        return update(sessionId, session);
    }
    
    // Finalizar sesión
    public UserSession endSession(UUID sessionId) {
        UserSession session = findById(sessionId);
        if (session == null) {
            return null;
        }
        
        session.setSessionStatus("inactive");
        session.setEndedAt(OffsetDateTime.now());
        return update(sessionId, session);
    }
    
    // Finalizar todas las sesiones de un usuario
    public List<UserSession> endAllSessionsByUser(UUID userId) {
        List<UserSession> sessions = findActiveByUserId(userId);
        List<UserSession> updated = new ArrayList<>();
        
        for (UserSession session : sessions) {
            session.setSessionStatus("inactive");
            session.setEndedAt(OffsetDateTime.now());
            UserSession result = update(session.getId(), session);
            if (result != null) {
                updated.add(result);
            }
        }
        
        return updated;
    }
    
    // Actualizar sesión
    public UserSession update(UUID id, UserSession session) {
        return crudService.update(TABLE, id, session, UserSession[].class);
    }
    
    // Verificar sesiones expiradas y marcarlas
    public int expireInactiveSessions() {
        OffsetDateTime timeoutThreshold = OffsetDateTime.now().minusMinutes(sessionTimeoutMinutes);
        
        List<UserSession> activeSessions = findActiveSessions();
        int expiredCount = 0;
        
        for (UserSession session : activeSessions) {
            if (session.getLastPingAt() != null && 
                session.getLastPingAt().isBefore(timeoutThreshold)) {
                session.setSessionStatus("expired");
                session.setEndedAt(OffsetDateTime.now());
                update(session.getId(), session);
                expiredCount++;
            }
        }
        
        return expiredCount;
    }
    
    // Verificar si una sesión está activa
    public boolean isSessionActive(UUID sessionId) {
        UserSession session = findById(sessionId);
        if (session == null) {
            return false;
        }
        
        if (!"active".equals(session.getSessionStatus())) {
            return false;
        }
        
        // Verificar timeout
        OffsetDateTime timeoutThreshold = OffsetDateTime.now().minusMinutes(sessionTimeoutMinutes);
        if (session.getLastPingAt() != null && 
            session.getLastPingAt().isBefore(timeoutThreshold)) {
            // Marcar como expirada
            session.setSessionStatus("expired");
            session.setEndedAt(OffsetDateTime.now());
            update(sessionId, session);
            return false;
        }
        
        return true;
    }
    
    // Eliminar sesión
    public void delete(UUID id) {
        crudService.delete(TABLE, id);
    }
    
    // Eliminar sesiones de un usuario
    public void deleteByUserId(UUID userId) {
        crudService.delete(TABLE, "user_id=eq." + userId);
    }
    
    // Eliminar sesiones expiradas
    public int deleteExpiredSessions() {
        List<UserSession> expired = findByStatus("expired");
        for (UserSession session : expired) {
            delete(session.getId());
        }
        return expired.size();
    }
    
    // Verificar si existe
    public boolean existsById(UUID id) {
        return crudService.exists(TABLE, "id=eq." + id);
    }
    
    // Contar sesiones activas
    public long countActiveSessions() {
        return crudService.count(TABLE, "session_status=eq.active");
    }
    
    // ============================================================
    // ESTADÍSTICAS
    // ============================================================
    
    // Obtener estadísticas generales
    public SessionStats getStats() {
        List<UserSession> sessions = findAll();
        return calculateStats(sessions);
    }
    
    // Obtener estadísticas por usuario
    public SessionStats getStatsByUser(UUID userId) {
        List<UserSession> sessions = findByUserId(userId);
        return calculateStats(sessions);
    }
    
    // Calcular estadísticas
    private SessionStats calculateStats(List<UserSession> sessions) {
        SessionStats stats = new SessionStats();
        
        if (sessions.isEmpty()) {
            stats.setTotalSessions(0);
            stats.setActiveSessions(0);
            stats.setInactiveSessions(0);
            stats.setExpiredSessions(0);
            stats.setByDeviceOs(new HashMap<>());
            stats.setByAppVersion(new HashMap<>());
            stats.setAvgSessionDurationMinutes(0);
            stats.setUniqueDevices(0);
            stats.setUniqueUsers(0);
            return stats;
        }
        
        // Totales por estado
        stats.setTotalSessions(sessions.size());
        
        long active = sessions.stream().filter(s -> "active".equals(s.getSessionStatus())).count();
        long inactive = sessions.stream().filter(s -> "inactive".equals(s.getSessionStatus())).count();
        long expired = sessions.stream().filter(s -> "expired".equals(s.getSessionStatus())).count();
        
        stats.setActiveSessions(active);
        stats.setInactiveSessions(inactive);
        stats.setExpiredSessions(expired);
        
        // Por sistema operativo
        Map<String, Long> byDeviceOs = sessions.stream()
            .filter(s -> s.getDeviceOs() != null && !s.getDeviceOs().isEmpty())
            .collect(Collectors.groupingBy(
                s -> s.getDeviceOs() != null ? s.getDeviceOs() : "unknown",
                Collectors.counting()
            ));
        stats.setByDeviceOs(byDeviceOs);
        
        // Por versión de la app
        Map<String, Long> byAppVersion = sessions.stream()
            .filter(s -> s.getAppVersion() != null && !s.getAppVersion().isEmpty())
            .collect(Collectors.groupingBy(
                s -> s.getAppVersion() != null ? s.getAppVersion() : "unknown",
                Collectors.counting()
            ));
        stats.setByAppVersion(byAppVersion);
        
        // Duración promedio de sesión (solo sesiones finalizadas)
        List<Long> durations = sessions.stream()
            .filter(s -> s.getStartedAt() != null && s.getEndedAt() != null)
            .map(s -> Duration.between(s.getStartedAt(), s.getEndedAt()).toMinutes())
            .collect(Collectors.toList());
        
        if (!durations.isEmpty()) {
            double avgDuration = durations.stream().mapToLong(Long::longValue).average().orElse(0);
            stats.setAvgSessionDurationMinutes(avgDuration);
        }
        
        // Dispositivos únicos
        long uniqueDevices = sessions.stream()
            .filter(s -> s.getDeviceId() != null)
            .map(UserSession::getDeviceId)
            .distinct()
            .count();
        stats.setUniqueDevices(uniqueDevices);
        
        // Usuarios únicos
        long uniqueUsers = sessions.stream()
            .map(UserSession::getUserId)
            .distinct()
            .count();
        stats.setUniqueUsers(uniqueUsers);
        
        return stats;
    }
    
    // Obtener sesiones por hora (actividad)
    public Map<Integer, Long> getSessionsByHour(LocalDate date) {
        // Implementación similar a la de route queries
        // Filtrar sesiones por fecha y agrupar por hora
        List<UserSession> sessions = findAll();
        Map<Integer, Long> activityByHour = new HashMap<>();
        
        for (int i = 0; i < 24; i++) {
            activityByHour.put(i, 0L);
        }
        
        for (UserSession session : sessions) {
            if (session.getCreatedAt() != null) {
                int hour = session.getCreatedAt().getHour();
                activityByHour.merge(hour, 1L, Long::sum);
            }
        }
        
        return activityByHour;
    }
}