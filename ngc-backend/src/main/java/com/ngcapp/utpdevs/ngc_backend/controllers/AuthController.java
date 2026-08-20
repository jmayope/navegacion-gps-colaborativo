package com.ngcapp.utpdevs.ngc_backend.controllers;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ngcapp.utpdevs.ngc_backend.dtos.AuthResponse;
import com.ngcapp.utpdevs.ngc_backend.dtos.LoginRequest;
import com.ngcapp.utpdevs.ngc_backend.models.UserModel;
import com.ngcapp.utpdevs.ngc_backend.repositories.UserRepository;

public class AuthController {
  @Autowired
  private UserRepository repository;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    
    // Buscar por username o email
    UserModel user = repository.findByEmail(
        request.getEmail()
    ).orElse(null);
    
    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Credenciales inválidas"));
    }
    
    // Validar contraseña (en producción: usar BCrypt)
    if (!user.getPassword().equals(request.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Credenciales inválidas"));
    }
    
    // Verificar si está activo
    if (!user.getIsActive()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Usuario inactivo"));
    }
    
    // Actualizar última actividad
    user.setLastActivityAt(OffsetDateTime.now());
    repository.save(user);
    
    AuthResponse response = new AuthResponse(
        user.getId(),
        user.getEmail(),
        user.getFullName(),
        "Login exitoso"
    );
    
    return ResponseEntity.ok(response);
  }
}
