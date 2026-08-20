package com.ngcapp.utpdevs.ngc_backend.controllers;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ngcapp.utpdevs.ngc_backend.models.User;
import com.ngcapp.utpdevs.ngc_backend.services.UserService;

public class UserController {
    @Autowired
    private UserService service;
    
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable UUID id) {
        User user = service.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        // Validar duplicados
        Map<String, String> errors = new HashMap<>();
        
        if (service.existsByEmail(user.getEmail())) {
            errors.put("email", "El email ya está registrado");
        }
        
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        
        user.setLastActivityAt(OffsetDateTime.now());
        User created = service.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody User user) {
        User existing = service.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        user.setId(id);
        user.setCreatedAt(existing.getCreatedAt());
        user.setLastActivityAt(OffsetDateTime.now());
        
        User updated = service.update(id, user);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (service.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
