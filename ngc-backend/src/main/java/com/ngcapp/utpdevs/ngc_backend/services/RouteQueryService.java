package com.ngcapp.utpdevs.ngc_backend.services;

import com.ngcapp.utpdevs.ngc_backend.dtos.QueryStats;
import com.ngcapp.utpdevs.ngc_backend.models.RouteQuery;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RouteQueryService {
    
    @Autowired
    private SupabaseCrudService crudService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private static final String TABLE = "route_queries";
    
    // Obtener todas las consultas
    public List<RouteQuery> findAll() {
        return crudService.findAll(TABLE, RouteQuery[].class);
    }
    
    // Obtener consulta por ID
    public RouteQuery findById(UUID id) {
        return crudService.findById(TABLE, id, RouteQuery[].class);
    }
    
    // Obtener consultas por usuario
    public List<RouteQuery> findByUserId(UUID userId) {
        return crudService.find(
            TABLE, 
            "user_id=eq." + userId + "&order=created_at.desc", 
            RouteQuery[].class
        );
    }
    
    // Obtener consultas por tipo
    public List<RouteQuery> findByQueryType(String queryType) {
        return crudService.find(
            TABLE, 
            "query_type=eq." + queryType + "&order=created_at.desc", 
            RouteQuery[].class
        );
    }
    
    // Obtener consultas por usuario y tipo
    public List<RouteQuery> findByUserIdAndType(UUID userId, String queryType) {
        return crudService.find(
            TABLE, 
            "user_id=eq." + userId + "&query_type=eq." + queryType + "&order=created_at.desc", 
            RouteQuery[].class
        );
    }
    
    // Obtener consultas por rango de fechas
    public List<RouteQuery> findByDateRange(UUID userId, OffsetDateTime start, OffsetDateTime end) {
        return crudService.find(
            TABLE, 
            "user_id=eq." + userId + 
            "&created_at=gte." + start + 
            "&created_at=lte." + end + 
            "&order=created_at.desc", 
            RouteQuery[].class
        );
    }
    
    // Obtener consultas por fecha
    public List<RouteQuery> findByDate(UUID userId, LocalDate date) {
        OffsetDateTime startOfDay = date.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime endOfDay = date.atTime(23, 59, 59).atOffset(ZoneOffset.UTC);
        return findByDateRange(userId, startOfDay, endOfDay);
    }
    
    // Obtener consultas con resultados
    public List<RouteQuery> findWithResults() {
        return crudService.find(
            TABLE, 
            "result_count=gt.0&order=created_at.desc", 
            RouteQuery[].class
        );
    }
    
    // Obtener consultas que seleccionaron una ruta
    public List<RouteQuery> findWithSelection() {
        return crudService.find(
            TABLE, 
            "selected_route_id=not.is.null&order=created_at.desc", 
            RouteQuery[].class
        );
    }
    
    // Obtener consultas con filtros específicos
    public List<RouteQuery> findByFilterKey(String filterKey) {
        // Esta es una búsqueda simple, en producción usarías JSONB queries de Postgres
        List<RouteQuery> all = findAll();
        return all.stream()
            .filter(q -> q.getQueryFilters() != null && q.getQueryFilters().containsKey(filterKey))
            .collect(Collectors.toList());
    }
    
    // Obtener consultas recientes de un usuario (últimas N)
    public List<RouteQuery> findRecentByUser(UUID userId, int limit) {
        return crudService.find(
            TABLE, 
            "user_id=eq." + userId + "&order=created_at.desc&limit=" + limit, 
            RouteQuery[].class
        );
    }
    
    // Crear consulta
    public RouteQuery create(RouteQuery query) {
        return crudService.insert(TABLE, query, RouteQuery[].class);
    }
    
    // Actualizar consulta (ej: para agregar selected_route_id después)
    public RouteQuery update(UUID id, RouteQuery query) {
        return crudService.update(TABLE, id, query, RouteQuery[].class);
    }
    
    // Eliminar consulta
    public void delete(UUID id) {
        crudService.delete(TABLE, id);
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
            "&created_at=gte." + start + 
            "&created_at=lte." + end
        );
    }
    
    // Verificar si existe
    public boolean existsById(UUID id) {
        return crudService.exists(TABLE, "id=eq." + id);
    }
    
    // Contar consultas de un usuario
    public long countByUserId(UUID userId) {
        return crudService.count(TABLE, "user_id=eq." + userId);
    }
    
    // ============================================================
    // ESTADÍSTICAS
    // ============================================================
    
    // Obtener estadísticas generales
    public QueryStats getStats() {
        List<RouteQuery> queries = findAll();
        return calculateStats(queries);
    }
    
    // Obtener estadísticas por usuario
    public QueryStats getStatsByUser(UUID userId) {
        List<RouteQuery> queries = findByUserId(userId);
        return calculateStats(queries);
    }
    
    // Obtener estadísticas por tipo
    public QueryStats getStatsByType(String queryType) {
        List<RouteQuery> queries = findByQueryType(queryType);
        return calculateStats(queries);
    }
    
    // Obtener estadísticas por rango de fechas
    public QueryStats getStatsByDateRange(UUID userId, OffsetDateTime start, OffsetDateTime end) {
        List<RouteQuery> queries = findByDateRange(userId, start, end);
        return calculateStats(queries);
    }
    
    // Calcular estadísticas
    private QueryStats calculateStats(List<RouteQuery> queries) {
        QueryStats stats = new QueryStats();
        
        if (queries.isEmpty()) {
            stats.setTotalQueries(0);
            stats.setByType(new HashMap<>());
            stats.setAvgResponseTimeMs(0);
            stats.setMaxResponseTimeMs(0);
            stats.setMinResponseTimeMs(0);
            stats.setTotalResults(0);
            stats.setAvgResultsPerQuery(0);
            stats.setQueriesWithSelection(0);
            stats.setSelectionRate(0);
            stats.setPopularFilters(new HashMap<>());
            return stats;
        }
        
        // Total de consultas
        stats.setTotalQueries(queries.size());
        
        // Agrupar por tipo
        Map<String, Long> byType = queries.stream()
            .collect(Collectors.groupingBy(RouteQuery::getQueryType, Collectors.counting()));
        stats.setByType(byType);
        
        // Tiempos de respuesta
        List<Integer> responseTimes = queries.stream()
            .filter(q -> q.getResponseTimeMs() != null)
            .map(RouteQuery::getResponseTimeMs)
            .collect(Collectors.toList());
        
        if (!responseTimes.isEmpty()) {
            double avg = responseTimes.stream().mapToInt(Integer::intValue).average().orElse(0);
            stats.setAvgResponseTimeMs(avg);
            stats.setMaxResponseTimeMs(Collections.max(responseTimes));
            stats.setMinResponseTimeMs(Collections.min(responseTimes));
        }
        
        // Resultados
        List<Integer> resultCounts = queries.stream()
            .filter(q -> q.getResultCount() != null)
            .map(RouteQuery::getResultCount)
            .collect(Collectors.toList());
        
        if (!resultCounts.isEmpty()) {
            stats.setTotalResults(resultCounts.stream().mapToInt(Integer::intValue).sum());
            stats.setAvgResultsPerQuery(
                resultCounts.stream().mapToInt(Integer::intValue).average().orElse(0)
            );
        }
        
        // Consultas con selección
        long withSelection = queries.stream()
            .filter(q -> q.getSelectedRouteId() != null)
            .count();
        stats.setQueriesWithSelection(withSelection);
        stats.setSelectionRate((double) withSelection / queries.size() * 100);
        
        // Filtros populares
        Map<String, Long> popularFilters = new HashMap<>();
        for (RouteQuery query : queries) {
            if (query.getQueryFilters() != null) {
                for (String key : query.getQueryFilters().keySet()) {
                    popularFilters.merge(key, 1L, Long::sum);
                }
            }
        }
        
        // Ordenar y limitar a los 10 más populares
        Map<String, Long> sortedFilters = popularFilters.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
        stats.setPopularFilters(sortedFilters);
        
        return stats;
    }
    
    // Obtener consultas populares (las que más se repiten)
    public List<Map<String, Object>> getPopularQueries(UUID userId, int limit) {
        List<RouteQuery> queries = findByUserId(userId);
        
        // Agrupar por origen y destino
        Map<String, Long> routePairs = new HashMap<>();
        for (RouteQuery query : queries) {
            if (query.getOriginLat() != null && query.getDestinationLat() != null) {
                String key = query.getOriginLat() + "," + query.getOriginLng() + 
                            "|" + query.getDestinationLat() + "," + query.getDestinationLng();
                routePairs.merge(key, 1L, Long::sum);
            }
        }
        
        // Ordenar por frecuencia
        List<Map<String, Object>> result = new ArrayList<>();
        routePairs.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(limit)
            .forEach(entry -> {
                String[] parts = entry.getKey().split("\\|");
                String[] origin = parts[0].split(",");
                String[] destination = parts[1].split(",");
                
                Map<String, Object> pair = new HashMap<>();
                pair.put("originLat", Double.parseDouble(origin[0]));
                pair.put("originLng", Double.parseDouble(origin[1]));
                pair.put("destinationLat", Double.parseDouble(destination[0]));
                pair.put("destinationLng", Double.parseDouble(destination[1]));
                pair.put("count", entry.getValue());
                
                result.add(pair);
            });
        
        return result;
    }
    
    // Obtener actividad por hora (para análisis de uso)
    public Map<Integer, Long> getActivityByHour(UUID userId, LocalDate date) {
        List<RouteQuery> queries = findByDate(userId, date);
        
        Map<Integer, Long> activityByHour = new HashMap<>();
        for (int i = 0; i < 24; i++) {
            activityByHour.put(i, 0L);
        }
        
        for (RouteQuery query : queries) {
            if (query.getCreatedAt() != null) {
                int hour = query.getCreatedAt().getHour();
                activityByHour.merge(hour, 1L, Long::sum);
            }
        }
        
        return activityByHour;
    }
}