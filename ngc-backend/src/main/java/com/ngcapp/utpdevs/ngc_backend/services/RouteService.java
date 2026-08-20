package com.ngcapp.utpdevs.ngc_backend.services;

import com.ngcapp.utpdevs.ngc_backend.models.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RouteService {
    
  @Autowired
  private SupabaseCrudService crudService;
  
  private static final String TABLE = "routes";
  
  // Obtener todas las rutas
  public List<Route> findAll() {
      return crudService.findAll(TABLE, Route[].class);
  }
  
  // Obtener ruta por ID
  public Route findById(UUID id) {
      return crudService.findById(TABLE, id, Route[].class);
  }
  
  // Obtener rutas por usuario
  public List<Route> findByUserId(UUID userId) {
      return crudService.find(
          TABLE, 
          "user_id=eq." + userId + "&order=created_at.desc", 
          Route[].class
      );
  }
  
  // Obtener rutas favoritas de un usuario
  public List<Route> findFavoritesByUserId(UUID userId) {
      return crudService.find(
          TABLE, 
          "user_id=eq." + userId + "&is_favorite=eq.true&order=created_at.desc", 
          Route[].class
      );
  }
  
  // Obtener rutas por estado
  public List<Route> findByStatus(String status) {
      return crudService.find(
          TABLE, 
          "status=eq." + status + "&order=created_at.desc", 
          Route[].class
      );
  }
  
  // Obtener rutas por usuario y estado
  public List<Route> findByUserIdAndStatus(UUID userId, String status) {
      return crudService.find(
          TABLE, 
          "user_id=eq." + userId + "&status=eq." + status + "&order=created_at.desc", 
          Route[].class
      );
  }
  
  // Crear nueva ruta
  public Route create(Route route) {
      // Si no se especifica estado, usar 'planning'
      if (route.getStatus() == null) {
          route.setStatus("planning");
      }
      return crudService.insert(TABLE, route, Route[].class);
  }
  
  // Actualizar ruta
  public Route update(UUID id, Route route) {
      return crudService.update(TABLE, id, route, Route[].class);
  }
  
  // Actualizar estado de la ruta
  public Route updateStatus(UUID id, String status) {
      // Primero obtenemos la ruta actual
      Route existing = findById(id);
      if (existing == null) {
          return null;
      }
      
      // Actualizar solo el estado y fechas según corresponda
      existing.setStatus(status);
      
      // Si el estado es 'in_progress' y no tiene fecha de inicio, asignarla
      if ("in_progress".equals(status) && existing.getStartedAt() == null) {
          existing.setStartedAt(OffsetDateTime.now());
      }
      
      // Si el estado es 'completed', asignar fecha de finalización
      if ("completed".equals(status)) {
          existing.setCompletedAt(OffsetDateTime.now());
      }
      
      // Si se cancela, limpiar fechas de inicio/fin si existen
      if ("cancelled".equals(status)) {
          // Opcional: mantener las fechas pero podrías querer limpiarlas
          // existing.setStartedAt(null);
          // existing.setCompletedAt(null);
      }
      
      return update(id, existing);
  }
  
  // Marcar como favorito
  public Route toggleFavorite(UUID id) {
      Route existing = findById(id);
      if (existing == null) {
          return null;
      }
      
      existing.setIsFavorite(!existing.getIsFavorite());
      return update(id, existing);
  }
  
  // Eliminar ruta
  public void delete(UUID id) {
      crudService.delete(TABLE, id);
  }
  
  // Eliminar todas las rutas de un usuario
  public void deleteByUserId(UUID userId) {
      crudService.delete(TABLE, "user_id=eq." + userId);
  }
  
  // Verificar si existe ruta
  public boolean existsById(UUID id) {
      return crudService.exists(TABLE, "id=eq." + id);
  }
  
  // Contar rutas de un usuario
  public long countByUserId(UUID userId) {
      return crudService.count(TABLE, "user_id=eq." + userId);
  }
  
  // Contar rutas por estado
  public long countByStatus(String status) {
      return crudService.count(TABLE, "status=eq." + status);
  }
}