package com.ngcapp.utpdevs.ngc_backend.controllers;

import com.ngcapp.utpdevs.ngc_backend.dtos.SegmentDetailBatchRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.SegmentDetailRequest;
import com.ngcapp.utpdevs.ngc_backend.models.SegmentDetail;
import com.ngcapp.utpdevs.ngc_backend.services.SegmentDetailService;
import com.ngcapp.utpdevs.ngc_backend.services.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/segment-details")
@CrossOrigin(origins = "*")
public class SegmentDetailController {

  @Autowired
  private SegmentDetailService detailService;

  @Autowired
  private SegmentService segmentService;

  // ============================================================
  // GET ENDPOINTS
  // ============================================================

  @GetMapping
  public ResponseEntity<List<SegmentDetail>> getAll() {
    return ResponseEntity.ok(detailService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<SegmentDetail> getById(@PathVariable UUID id) {
    SegmentDetail detail = detailService.findById(id);
    if (detail == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(detail);
  }

  @GetMapping("/segment/{segmentId}")
  public ResponseEntity<List<SegmentDetail>> getBySegment(@PathVariable UUID segmentId) {
    return ResponseEntity.ok(detailService.findBySegmentId(segmentId));
  }

  @GetMapping("/instruction-type/{instructionType}")
  public ResponseEntity<List<SegmentDetail>> getByInstructionType(
        @PathVariable String instructionType) {
    return ResponseEntity.ok(detailService.findByInstructionType(instructionType));
  }

  @GetMapping("/maneuver/{maneuver}")
  public ResponseEntity<List<SegmentDetail>> getByManeuver(@PathVariable String maneuver) {
    return ResponseEntity.ok(detailService.findByManeuver(maneuver));
  }

  @GetMapping("/side/{side}")
  public ResponseEntity<List<SegmentDetail>> getBySide(@PathVariable String side) {
    return ResponseEntity.ok(detailService.findBySide(side));
  }

  @GetMapping("/with-exit")
  public ResponseEntity<List<SegmentDetail>> getWithExit() {
    return ResponseEntity.ok(detailService.findByExitNumberNotNull());
  }

  @GetMapping("/segment/{segmentId}/summary")
  public ResponseEntity<?> getInstructionsSummary(@PathVariable UUID segmentId) {
    // Verificar que el segmento existe
    if (!segmentService.existsById(segmentId)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "El segmento no existe"));
    }
    
    List<SegmentDetail> details = detailService.findBySegmentId(segmentId);
    
    Map<String, Object> response = new HashMap<>();
    response.put("segmentId", segmentId);
    response.put("totalInstructions", details.size());
    response.put("instructions", details);
    response.put("summary", detailService.getInstructionsSummary(segmentId));
    
    return ResponseEntity.ok(response);
  }

  // ============================================================
  // POST ENDPOINTS
  // ============================================================

  @PostMapping
  public ResponseEntity<?> create(@RequestBody SegmentDetailRequest request) {
    // Verificar que el segmento existe
    if (!segmentService.existsById(request.getSegmentId())) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "El segmento no existe"));
    }
    
    SegmentDetail detail = new SegmentDetail();
    detail.setSegmentId(request.getSegmentId());
    detail.setInstruction(request.getInstruction());
    detail.setInstructionType(request.getInstructionType());
    detail.setManeuver(request.getManeuver());
    detail.setDistanceToNext(request.getDistanceToNext());
    detail.setDurationToNext(request.getDurationToNext());
    detail.setExitNumber(request.getExitNumber());
    detail.setSide(request.getSide());
    
    SegmentDetail created = detailService.create(detail);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PostMapping("/batch")
  public ResponseEntity<?> createBatch(@RequestBody SegmentDetailBatchRequest request) {
    // Verificar que el segmento existe
    if (!segmentService.existsById(request.getSegmentId())) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "El segmento no existe"));
    }
    
    List<SegmentDetail> details = new ArrayList<>();
    for (SegmentDetailRequest detRequest : request.getDetails()) {
        SegmentDetail detail = new SegmentDetail();
        detail.setSegmentId(request.getSegmentId());
        detail.setInstruction(detRequest.getInstruction());
        detail.setInstructionType(detRequest.getInstructionType());
        detail.setManeuver(detRequest.getManeuver());
        detail.setDistanceToNext(detRequest.getDistanceToNext());
        detail.setDurationToNext(detRequest.getDurationToNext());
        detail.setExitNumber(detRequest.getExitNumber());
        detail.setSide(detRequest.getSide());
        
        details.add(detail);
    }
    
    List<SegmentDetail> created = detailService.createBatch(details);
    
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Detalles creados exitosamente");
    response.put("count", created.size());
    response.put("segmentId", request.getSegmentId());
    response.put("details", created);
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // ============================================================
  // PUT ENDPOINTS
  // ============================================================

  @PutMapping("/{id}")
  public ResponseEntity<?> update(
        @PathVariable UUID id,
        @RequestBody SegmentDetailRequest request) {
    
    SegmentDetail existing = detailService.findById(id);
    if (existing == null) {
        return ResponseEntity.notFound().build();
    }
    
    // No permitir cambiar el segmentId
    // Si se envía, se ignora
    
    existing.setInstruction(request.getInstruction());
    existing.setInstructionType(request.getInstructionType());
    existing.setManeuver(request.getManeuver());
    existing.setDistanceToNext(request.getDistanceToNext());
    existing.setDurationToNext(request.getDurationToNext());
    existing.setExitNumber(request.getExitNumber());
    existing.setSide(request.getSide());
    
    SegmentDetail updated = detailService.update(id, existing);
    return ResponseEntity.ok(updated);
  }

  // ============================================================
  // DELETE ENDPOINTS
  // ============================================================

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    if (!detailService.existsById(id)) {
        return ResponseEntity.notFound().build();
    }
    detailService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/segment/{segmentId}")
  public ResponseEntity<?> deleteBySegment(@PathVariable UUID segmentId) {
    // Verificar que el segmento existe
    if (!segmentService.existsById(segmentId)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "El segmento no existe"));
    }
    
    long count = detailService.countBySegmentId(segmentId);
    if (count == 0) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", "No se encontraron detalles para este segmento"));
    }
    
    detailService.deleteBySegmentId(segmentId);
    
    return ResponseEntity.ok(Map.of(
        "message", "Detalles eliminados exitosamente",
        "segmentId", segmentId,
        "deletedCount", count
    ));
  }
}