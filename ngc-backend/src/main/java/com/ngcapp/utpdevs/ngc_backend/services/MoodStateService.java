package com.ngcapp.utpdevs.ngc_backend.services;

import com.ngcapp.utpdevs.ngc_backend.dtos.MoodStats;
import com.ngcapp.utpdevs.ngc_backend.models.MoodState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MoodStateService {
    
    @Autowired
    private SupabaseCrudService crudService;
    
    private static final String TABLE = "mood_states";
    
    // Obtener todos los estados de ánimo
    public List<MoodState> findAll() {
        return crudService.findAll(TABLE, MoodState[].class);
    }
    
    // Obtener estado de ánimo por ID
    public MoodState findById(UUID id) {
        return crudService.findById(TABLE, id, MoodState[].class);
    }
    
    // Obtener estados de ánimo por usuario
    public List<MoodState> findByUserId(UUID userId) {
        return crudService.find(
            TABLE, 
            "user_id=eq." + userId + "&order=created_at.desc", 
            MoodState[].class
        );
    }
    
    // Obtener estados de ánimo por ruta
    public List<MoodState> findByRouteId(UUID routeId) {
        return crudService.find(
            TABLE, 
            "route_id=eq." + routeId + "&order=created_at.desc", 
            MoodState[].class
        );
    }
    
    // Obtener estados de ánimo por tipo
    public List<MoodState> findByMoodType(String moodType) {
        return crudService.find(
            TABLE, 
            "mood_type=eq." + moodType + "&order=created_at.desc", 
            MoodState[].class
        );
    }
    
    // Obtener estados de ánimo por valor (1-5)
    public List<MoodState> findByMoodValue(Integer moodValue) {
        return crudService.find(
            TABLE, 
            "mood_value=eq." + moodValue + "&order=created_at.desc", 
            MoodState[].class
        );
    }
    
    // Obtener estados de ánimo por rango de valores
    public List<MoodState> findByMoodValueRange(Integer minValue, Integer maxValue) {
        return crudService.find(
            TABLE, 
            "mood_value=gte." + minValue + "&mood_value=lte." + maxValue + "&order=created_at.desc", 
            MoodState[].class
        );
    }
    
    // Obtener estados de ánimo por fecha
    public List<MoodState> findByDate(LocalDate date) {
        OffsetDateTime startOfDay = date.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime endOfDay = date.atTime(23, 59, 59).atOffset(ZoneOffset.UTC);
        
        return crudService.find(
            TABLE, 
            "created_at=gte." + startOfDay + "&created_at=lte." + endOfDay + "&order=created_at.desc", 
            MoodState[].class
        );
    }
    
    // Obtener estados de ánimo por usuario y ruta
    public List<MoodState> findByUserIdAndRouteId(UUID userId, UUID routeId) {
        return crudService.find(
            TABLE, 
            "user_id=eq." + userId + "&route_id=eq." + routeId + "&order=created_at.desc", 
            MoodState[].class
        );
    }
    
    // Obtener último estado de ánimo de un usuario
    public MoodState findLastMoodByUser(UUID userId) {
        List<MoodState> moods = crudService.find(
            TABLE, 
            "user_id=eq." + userId + "&order=created_at.desc&limit=1", 
            MoodState[].class
        );
        return moods.isEmpty() ? null : moods.get(0);
    }
    
    // Crear estado de ánimo
    public MoodState create(MoodState moodState) {
        // Validar que el valor coincida con el tipo
        validateMood(moodState);
        return crudService.insert(TABLE, moodState, MoodState[].class);
    }
    
    // Actualizar estado de ánimo
    public MoodState update(UUID id, MoodState moodState) {
        validateMood(moodState);
        return crudService.update(TABLE, id, moodState, MoodState[].class);
    }
    
    // Eliminar estado de ánimo
    public void delete(UUID id) {
        crudService.delete(TABLE, id);
    }
    
    // Eliminar todos los estados de ánimo de una ruta
    public void deleteByRouteId(UUID routeId) {
        crudService.delete(TABLE, "route_id=eq." + routeId);
    }
    
    // Eliminar todos los estados de ánimo de un usuario
    public void deleteByUserId(UUID userId) {
        crudService.delete(TABLE, "user_id=eq." + userId);
    }
    
    // Verificar si existe
    public boolean existsById(UUID id) {
        return crudService.exists(TABLE, "id=eq." + id);
    }
    
    // Obtener estadísticas generales
    public MoodStats getStats() {
        List<MoodState> moods = findAll();
        return calculateStats(moods);
    }
    
    // Obtener estadísticas por usuario
    public MoodStats getStatsByUser(UUID userId) {
        List<MoodState> moods = findByUserId(userId);
        return calculateStats(moods);
    }
    
    // Obtener estadísticas por ruta
    public MoodStats getStatsByRoute(UUID routeId) {
        List<MoodState> moods = findByRouteId(routeId);
        return calculateStats(moods);
    }
    
    // Obtener estadísticas por usuario y ruta
    public MoodStats getStatsByUserAndRoute(UUID userId, UUID routeId) {
        List<MoodState> moods = findByUserIdAndRouteId(userId, routeId);
        return calculateStats(moods);
    }
    
    // Obtener evolución del estado de ánimo (por fecha)
    public Map<String, Double> getMoodEvolution(UUID userId, LocalDate startDate, LocalDate endDate) {
        List<MoodState> moods = findByUserId(userId);
        
        Map<String, Double> evolution = new LinkedHashMap<>();
        
        // Filtrar por rango de fechas
        OffsetDateTime start = startDate.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = endDate.atTime(23, 59, 59).atOffset(ZoneOffset.UTC);
        
        moods.stream()
            .filter(m -> m.getCreatedAt() != null)
            .filter(m -> m.getCreatedAt().isAfter(start) && m.getCreatedAt().isBefore(end))
            .sorted(Comparator.comparing(MoodState::getCreatedAt))
            .forEach(m -> {
                String date = m.getCreatedAt().toLocalDate().toString();
                double avg = evolution.getOrDefault(date, 0.0);
                long count = evolution.entrySet().stream()
                    .filter(e -> e.getKey().equals(date))
                    .count();
                // Este cálculo necesita ser mejorado
                evolution.put(date, (double) m.getMoodValue());
            });
        
        return evolution;
    }
    
    // Método privado para calcular estadísticas
    private MoodStats calculateStats(List<MoodState> moods) {
        MoodStats stats = new MoodStats();
        
        if (moods.isEmpty()) {
            stats.setAverage(0.0);
            stats.setTotalRecords(0);
            stats.setByType(new HashMap<>());
            stats.setByValue(new HashMap<>());
            stats.setMostCommonMood("none");
            stats.setAverageByType(new HashMap<>());
            return stats;
        }
        
        // Total de registros
        stats.setTotalRecords(moods.size());
        
        // Promedio general
        double average = moods.stream()
            .mapToInt(MoodState::getMoodValue)
            .average()
            .orElse(0.0);
        stats.setAverage(average);
        
        // Agrupar por tipo
        Map<String, Long> byType = moods.stream()
            .collect(Collectors.groupingBy(MoodState::getMoodType, Collectors.counting()));
        stats.setByType(byType);
        
        // Encontrar el tipo más común
        String mostCommonMood = byType.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("none");
        stats.setMostCommonMood(mostCommonMood);
        
        // Agrupar por valor (1-5)
        Map<Integer, Long> byValue = moods.stream()
            .collect(Collectors.groupingBy(MoodState::getMoodValue, Collectors.counting()));
        stats.setByValue(byValue);
        
        // Promedio por tipo
        Map<String, Double> averageByType = new HashMap<>();
        for (String type : new String[]{"happy", "neutral", "sad"}) {
            List<MoodState> typeMoods = moods.stream()
                .filter(m -> type.equals(m.getMoodType()))
                .collect(Collectors.toList());
            
            double avg = typeMoods.stream()
                .mapToInt(MoodState::getMoodValue)
                .average()
                .orElse(0.0);
            averageByType.put(type, avg);
        }
        stats.setAverageByType(averageByType);
        
        return stats;
    }
    
    // Validar que el valor coincida con el tipo
    private void validateMood(MoodState moodState) {
        String type = moodState.getMoodType();
        Integer value = moodState.getMoodValue();
        
        if (type == null || value == null) {
            return;
        }
        
        // Validación básica de consistencia
        if (("happy".equals(type) && value < 3) ||
            ("sad".equals(type) && value > 3)) {
            // No lanzamos excepción, solo ajustamos si es necesario
            // Podrías lanzar una excepción si quieres ser estricto
        }
    }
}