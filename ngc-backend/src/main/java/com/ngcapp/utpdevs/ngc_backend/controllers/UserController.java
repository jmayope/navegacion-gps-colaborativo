package com.ngcapp.utpdevs.ngc_backend.controllers;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ngcapp.utpdevs.ngc_backend.models.UserModel;
import com.ngcapp.utpdevs.ngc_backend.services.UserService;

public class UserController {
  @Autowired
  private UserService service;
  
  @GetMapping
  public List<UserModel> findAll() {
      return service.findAll();
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<UserModel> buscar(@PathVariable UUID id) {
      UserModel user = service.findById(id);
      if (user == null) {
          return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(user);
  }
  
  @GetMapping("/email/{email}")
  public ResponseEntity<UserModel> findByEmail(@PathVariable String email) {
      UserModel user = service.findByEmail(email);
      if (user == null) {
          return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(user);
  }
  
  @PostMapping
  public ResponseEntity<?> crear(@RequestBody UserModel user) {
      // Validar duplicados
      if (service.existsEmail(user.getEmail())) {
          return ResponseEntity.badRequest().body("El email ya está registrado");
      }
      if (service.existsPhone(user.getPhone())) {
          return ResponseEntity.badRequest().body("El teléfono ya está registrado");
      }
      
      user.setLastActivityAt(OffsetDateTime.now());
      UserModel nuevo = service.save(user);
      return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<?> actualizar(@PathVariable UUID id, 
                                      @RequestBody UserModel user) {
      UserModel exists = service.findById(id);
      if (exists == null) {
          return ResponseEntity.notFound().build();
      }
      
      // Mantener valores que no deben cambiar
      user.setId(id);
      user.setCreatedAt(exists.getCreatedAt());
      user.setLastActivityAt(OffsetDateTime.now());
      
      return ResponseEntity.ok(service.save(user));
  }
  
  @PatchMapping("/{id}/activar")
  public ResponseEntity<UserModel> activar(@PathVariable UUID id) {
      UserModel user = service.findById(id);
      if (user == null) {
          return ResponseEntity.notFound().build();
      }
      user.setIsActive(true);
      return ResponseEntity.ok(service.save(user));
  }
  
  @PatchMapping("/{id}/verificar")
  public ResponseEntity<UserModel> verificar(@PathVariable UUID id) {
      UserModel user = service.findById(id);
      if (user == null) {
          return ResponseEntity.notFound().build();
      }
      user.setIsVerified(true);
      return ResponseEntity.ok(service.save(user));
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
      if (service.findById(id) == null) {
          return ResponseEntity.notFound().build();
      }
      service.delete(id);
      return ResponseEntity.noContent().build();
  }
}
