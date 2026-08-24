package com.ngcapp.utpdevs.ngc_backend.controllers;

import com.ngcapp.utpdevs.ngc_backend.dtos.AuthResponse;
import com.ngcapp.utpdevs.ngc_backend.dtos.ChangePasswordRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.LoginRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.RefreshTokenRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.RegisterRequest;
import com.ngcapp.utpdevs.ngc_backend.models.User;
import com.ngcapp.utpdevs.ngc_backend.services.AuthService;
import com.ngcapp.utpdevs.ngc_backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserService userService;
    
    // ============================================================
    // REGISTER
    // ============================================================
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    // ============================================================
    // LOGIN
    // ============================================================
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        
        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    // ============================================================
    // LOGOUT
    // ============================================================
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Obtener userId del token (desde el header Authorization)
        String authHeader = request.getHeader("Authorization");
        UUID userId = extractUserIdFromToken(authHeader);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Usuario no autenticado"));
        }
        
        // Obtener sessionId (desde el header o body)
        String sessionId = request.getHeader("X-Session-Id");
        UUID sessionUUID = sessionId != null ? UUID.fromString(sessionId) : null;
        
        boolean loggedOut = authService.logout(userId, sessionUUID);
        
        if (!loggedOut) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al cerrar sesión"));
        }
        
        return ResponseEntity.ok(Map.of(
            "message", "Sesión cerrada exitosamente",
            "userId", userId
        ));
    }
    
    // ============================================================
    // CHANGE PASSWORD
    // ============================================================
    
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            HttpServletRequest request,
            @RequestBody ChangePasswordRequest changeRequest) {
        
        // Validar que la nueva contraseña coincida
        if (!changeRequest.getNewPassword().equals(changeRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Las contraseñas no coinciden"));
        }
        
        // Obtener userId del token
        String authHeader = request.getHeader("Authorization");
        UUID userId = extractUserIdFromToken(authHeader);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Usuario no autenticado"));
        }
        
        boolean changed = authService.changePassword(
            userId,
            changeRequest.getCurrentPassword(),
            changeRequest.getNewPassword()
        );
        
        if (!changed) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Contraseña actual incorrecta"));
        }
        
        return ResponseEntity.ok(Map.of(
            "message", "Contraseña actualizada exitosamente",
            "userId", userId
        ));
    }
    
    // ============================================================
    // REFRESH TOKEN
    // ============================================================
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request.getRefreshToken());
        
        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    // ============================================================
    // VERIFY USER
    // ============================================================
    
    @PostMapping("/verify/{userId}")
    public ResponseEntity<?> verifyUser(@PathVariable UUID userId) {
        User user = userService.findById(userId);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Usuario no encontrado"));
        }
        
        if (user.getIsVerified()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "El usuario ya está verificado"));
        }
        
        user.setIsVerified(true);
        user.setLastActivityAt(OffsetDateTime.now());
        userService.update(userId, user);
        
        return ResponseEntity.ok(Map.of(
            "message", "Usuario verificado exitosamente",
            "userId", userId,
            "isVerified", true
        ));
    }
    
    // ============================================================
    // RESEND VERIFICATION
    // ============================================================
    
    @PostMapping("/resend-verification/{userId}")
    public ResponseEntity<?> resendVerification(@PathVariable UUID userId) {
        User user = userService.findById(userId);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Usuario no encontrado"));
        }
        
        if (user.getIsVerified()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "El usuario ya está verificado"));
        }
        
        // Aquí enviarías el email de verificación nuevamente
        // sendVerificationEmail(usuario);
        
        return ResponseEntity.ok(Map.of(
            "message", "Correo de verificación reenviado exitosamente",
            "userId", userId,
            "email", user.getEmail()
        ));
    }
    
    // ============================================================
    // FORGOT PASSWORD
    // ============================================================
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "El email es requerido"));
        }
        
        User user = userService.findByEmail(email);
        
        if (user == null) {
            // No revelar si el email existe o no por seguridad
            return ResponseEntity.ok(Map.of(
                "message", "Si el email existe, recibirás un enlace para restablecer tu contraseña"
            ));
        }
        
        // Aquí generarías un token de reset y enviarías el email
        // String resetToken = generateResetToken(usuario);
        // sendResetPasswordEmail(usuario, resetToken);
        
        return ResponseEntity.ok(Map.of(
            "message", "Si el email existe, recibirás un enlace para restablecer tu contraseña",
            "emailSent", true
        ));
    }
    
    // ============================================================
    // RESET PASSWORD
    // ============================================================
    
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");
        
        // Validaciones
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Token requerido"));
        }
        
        if (newPassword == null || newPassword.isEmpty() || newPassword.length() < 6) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "La contraseña debe tener al menos 6 caracteres"));
        }
        
        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Las contraseñas no coinciden"));
        }
        
        // Validar token y obtener userId
        UUID userId = validateResetToken(token);
        if (userId == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Token inválido o expirado"));
        }
        
        // Actualizar contraseña
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Usuario no encontrado"));
        }
        
        user.setPassword(authService.encodePassword(newPassword));
        userService.update(userId, user);
        
        return ResponseEntity.ok(Map.of(
            "message", "Contraseña restablecida exitosamente",
            "userId", userId
        ));
    }
    
    // ============================================================
    // CHECK AUTH STATUS
    // ============================================================
    
    @GetMapping("/status")
    public ResponseEntity<?> checkAuthStatus(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("authenticated", false));
        }
        
        String token = authHeader.substring(7);
        boolean isValid = authService.validateToken(token);
        
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("authenticated", false));
        }
        
        UUID userId = authService.getUserIdFromToken(token);
        User user = userService.findById(userId);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("authenticated", false));
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", true);
        response.put("userId", user.getId());
        response.put("email", user.getEmail());
        response.put("fullName", user.getFullName());
        response.put("isVerified", user.getIsVerified());
        response.put("isActive", user.getIsActive());
        
        return ResponseEntity.ok(response);
    }
    
    // ============================================================
    // PRIVATE METHODS
    // ============================================================
    
    private UUID extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        
        String token = authHeader.substring(7);
        return authService.getUserIdFromToken(token);
    }
    
    private UUID validateResetToken(String token) {
        // Validar token de reset y retornar userId
        // Implementación real: verificar en base de datos o JWT
        try {
            // Simulación
            return UUID.fromString(token);
        } catch (Exception e) {
            return null;
        }
    }
}