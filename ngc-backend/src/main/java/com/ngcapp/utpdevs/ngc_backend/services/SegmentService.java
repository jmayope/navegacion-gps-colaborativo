package com.ngcapp.utpdevs.ngc_backend.services;

import com.ngcapp.utpdevs.ngc_backend.models.Segment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class SegmentService {
    
    @Autowired
    private SupabaseCrudService crudService;
    
    private static final String TABLE = "segments";
    
    // Obtener todos los segmentos
    public List<Segment> findAll() {
        return crudService.findAll(TABLE, Segment[].class);
    }
    
    // Obtener segmento por ID
    public Segment findById(UUID id) {
        return crudService.findById(TABLE, id, Segment[].class);
    }
    
    // Obtener segmentos por ruta (ordenados por sequence_number)
    public List<Segment> findByRouteId(UUID routeId) {
        return crudService.find(
            TABLE, 
            "route_id=eq." + routeId + "&order=sequence_number.asc", 
            Segment[].class
        );
    }
    
    // Obtener segmentos por tipo de vía
    public List<Segment> findByRoadType(String roadType) {
        return crudService.find(
            TABLE, 
            "road_type=eq." + roadType, 
            Segment[].class
        );
    }
    
    // Obtener segmentos por rango de distancia
    public List<Segment> findByDistanceRange(BigDecimal minDistance, BigDecimal maxDistance) {
        return crudService.find(
            TABLE, 
            "distance=gte." + minDistance + "&distance=lte." + maxDistance, 
            Segment[].class
        );
    }
    
    // Crear nuevo segmento
    public Segment create(Segment segment) {
        return crudService.insert(TABLE, segment, Segment[].class);
    }
    
    // Crear múltiples segmentos (uno por uno por ahora)
    public List<Segment> createBatch(List<Segment> segments) {
        // Supabase REST no soporta batch insert directamente con el mismo endpoint
        // pero podemos hacerlo uno por uno
        for (Segment segment : segments) {
            crudService.insert(TABLE, segment, Segment[].class);
        }
        return segments;
    }
    
    // Actualizar segmento
    public Segment update(UUID id, Segment segment) {
        return crudService.update(TABLE, id, segment, Segment[].class);
    }
    
    // Actualizar orden de los segmentos (reordenar)
    public List<Segment> reorderSegments(UUID routeId, List<UUID> segmentIds) {
        // Obtener todos los segmentos de la ruta
        List<Segment> existingSegments = findByRouteId(routeId);
        
        if (existingSegments.isEmpty()) {
            return existingSegments;
        }
        
        // Actualizar sequence_number basado en el nuevo orden
        for (int i = 0; i < segmentIds.size(); i++) {
            UUID segmentId = segmentIds.get(i);
            Segment segment = existingSegments.stream()
                .filter(s -> s.getId().equals(segmentId))
                .findFirst()
                .orElse(null);
            
            if (segment != null) {
                segment.setSequenceNumber(i + 1);
                update(segmentId, segment);
            }
        }
        
        // Retornar los segmentos actualizados en el nuevo orden
        return findByRouteId(routeId);
    }
    
    // Eliminar segmento
    public void delete(UUID id) {
        crudService.delete(TABLE, id);
    }
    
    // Eliminar todos los segmentos de una ruta
    public void deleteByRouteId(UUID routeId) {
        crudService.delete(TABLE, "route_id=eq." + routeId);
    }
    
    // Verificar si existe segmento
    public boolean existsById(UUID id) {
        return crudService.exists(TABLE, "id=eq." + id);
    }
    
    // Contar segmentos de una ruta
    public long countByRouteId(UUID routeId) {
        return crudService.count(TABLE, "route_id=eq." + routeId);
    }
    
    // Obtener el siguiente número de secuencia para una ruta
    public int getNextSequenceNumber(UUID routeId) {
        List<Segment> segments = findByRouteId(routeId);
        return segments.isEmpty() ? 1 : segments.size() + 1;
    }
}