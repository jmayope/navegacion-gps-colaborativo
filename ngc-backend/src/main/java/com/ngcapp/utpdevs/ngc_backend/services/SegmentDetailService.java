package com.ngcapp.utpdevs.ngc_backend.services;

import com.ngcapp.utpdevs.ngc_backend.models.SegmentDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SegmentDetailService {
    
  @Autowired
  private SupabaseCrudService crudService;

  private static final String TABLE = "segment_details";

  // Obtener todos los detalles
  public List<SegmentDetail> findAll() {
    return crudService.findAll(TABLE, SegmentDetail[].class);
  }

  // Obtener detalle por ID
  public SegmentDetail findById(UUID id) {
    return crudService.findById(TABLE, id, SegmentDetail[].class);
  }

  // Obtener detalles por segmento
  public List<SegmentDetail> findBySegmentId(UUID segmentId) {
    return crudService.find(
        TABLE, 
        "segment_id=eq." + segmentId, 
        SegmentDetail[].class
    );
  }

  // Obtener detalles por tipo de instrucción
  public List<SegmentDetail> findByInstructionType(String instructionType) {
    return crudService.find(
        TABLE, 
        "instruction_type=eq." + instructionType, 
        SegmentDetail[].class
    );
  }

  // Obtener detalles por maniobra
  public List<SegmentDetail> findByManeuver(String maneuver) {
    return crudService.find(
        TABLE, 
        "maneuver=eq." + maneuver, 
        SegmentDetail[].class
    );
  }

  // Obtener detalles por tipo y maniobra
  public List<SegmentDetail> findByInstructionTypeAndManeuver(
        String instructionType, 
        String maneuver) {
    return crudService.find(
        TABLE, 
        "instruction_type=eq." + instructionType + "&maneuver=eq." + maneuver, 
        SegmentDetail[].class
    );
  }

  // Obtener detalles con salida (autopistas)
  public List<SegmentDetail> findByExitNumberNotNull() {
    return crudService.find(
        TABLE, 
        "exit_number=not.is.null", 
        SegmentDetail[].class
    );
  }

  // Obtener detalles por lado
  public List<SegmentDetail> findBySide(String side) {
    return crudService.find(
        TABLE, 
        "side=eq." + side, 
        SegmentDetail[].class
    );
  }

  // Crear nuevo detalle
  public SegmentDetail create(SegmentDetail detail) {
    return crudService.insert(TABLE, detail, SegmentDetail[].class);
  }

  // Crear múltiples detalles para un segmento
  public List<SegmentDetail> createBatch(List<SegmentDetail> details) {
    List<SegmentDetail> created = new ArrayList<>();
    for (SegmentDetail detail : details) {
        SegmentDetail result = crudService.insert(TABLE, detail, SegmentDetail[].class);
        created.add(result);
    }
    return created;
  }

  // Actualizar detalle
  public SegmentDetail update(UUID id, SegmentDetail detail) {
    return crudService.update(TABLE, id, detail, SegmentDetail[].class);
  }

  // Eliminar detalle
  public void delete(UUID id) {
    crudService.delete(TABLE, id);
  }

  // Eliminar todos los detalles de un segmento
  public void deleteBySegmentId(UUID segmentId) {
    crudService.delete(TABLE, "segment_id=eq." + segmentId);
  }

  // Verificar si existe detalle
  public boolean existsById(UUID id) {
    return crudService.exists(TABLE, "id=eq." + id);
  }

  // Contar detalles de un segmento
  public long countBySegmentId(UUID segmentId) {
    return crudService.count(TABLE, "segment_id=eq." + segmentId);
  }

  // Obtener resumen de instrucciones de un segmento
  public String getInstructionsSummary(UUID segmentId) {
    List<SegmentDetail> details = findBySegmentId(segmentId);
    if (details.isEmpty()) {
        return "Sin instrucciones";
    }
    
    StringBuilder summary = new StringBuilder();
    for (int i = 0; i < details.size(); i++) {
        SegmentDetail detail = details.get(i);
        summary.append(i + 1).append(". ");
        summary.append(detail.getInstruction());
        if (detail.getDistanceToNext() != null) {
            summary.append(" (").append(detail.getDistanceToNext()).append("m)");
        }
        if (i < details.size() - 1) {
            summary.append(" → ");
        }
    }
    return summary.toString();
  }
}