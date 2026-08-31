package com.ngcapp.utpdevs.ngc_backend.services;

import com.ngcapp.utpdevs.ngc_backend.dtos.AuthResponse;
import com.ngcapp.utpdevs.ngc_backend.dtos.LoginRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.RegisterRequest;
import com.ngcapp.utpdevs.ngc_backend.models.User;
import com.ngcapp.utpdevs.ngc_backend.models.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserSessionService sessionService;
    
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    
    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpirationMs; // 24 horas por defecto
    
    @Value("${app.jwt.refresh-expiration:604800000}")
    private long refreshExpirationMs; // 7 días por defecto
    
    private static final String DEVICE_ID = "auth-device";
    private static final String DEVICE_NAME = "Authentication Device";
    private static final String DEVICE_OS = "Web";
    private static final String APP_VERSION = "1.0.0";
    
    // ============================================================
    // REGISTER
    // ============================================================
    
    public AuthResponse register(RegisterRequest request) {
        // Validar duplicados
        Map<String, String> errors = new HashMap<>();
        
        if (userService.existsByEmail(request.getEmail())) {
            errors.put("email", "El email ya está registrado");
        }
        
        if (userService.existsByPhone(request.getPhone())) {
            errors.put("phone", "El teléfono ya está registrado");
        }
        
        if (!errors.isEmpty()) {
            return new AuthResponse(
                "Error de validación: " + errors.toString(), 
                false
            );
        }
        
        // Crear usuario
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setFullName(request.getFullName());
        user.setPassword(encodePassword(request.getPassword()));
        user.setDocumentType(request.getDocumentType());
        user.setDocumentNumber(request.getDocumentNumber());
        user.setLastLocationLat(request.getLastLocationLat());
        user.setLastLocationLng(request.getLastLocationLng());
        user.setIsActive(true);
        user.setIsVerified(false);
        user.setLastActivityAt(OffsetDateTime.now());

        User saved = userService.create(user);
        
        // Generar token
        String token = generateToken(saved);
        OffsetDateTime expiresAt = OffsetDateTime.now().plusSeconds(jwtExpirationMs);
        
        System.out.println("================================");
        System.out.println("GRABADO ID: " + saved.getId());
        System.out.println("================================");

        // Iniciar sesión automáticamente
        startUserSession(saved);
        
        return new AuthResponse(
            saved.getId(),
            saved.getEmail(),
            saved.getFullName(),
            saved.getIsVerified(),
            saved.getIsActive(),
            token,
            expiresAt,
            "Usuario registrado exitosamente"
        );
    }
    
    // ============================================================
    // LOGIN
    // ============================================================
    
    public AuthResponse login(LoginRequest request) {
        // Buscar usuario por username o email
        User user = userService.findByEmail(request.getEmail());
        
        if (user == null) {
            return new AuthResponse("Credenciales inválidas", true);
        }
        
        // Validar contraseña
        if (!matchesPassword(request.getPassword(), user.getPassword())) {
            return new AuthResponse("Credenciales inválidas", true);
        }
        
        // Verificar si está activo
        if (!user.getIsActive()) {
            return new AuthResponse("La cuenta está desactivada. Contacte al soporte.", true);
        }
        
        // Actualizar última actividad
        user.setLastActivityAt(OffsetDateTime.now());
        userService.update(user.getId(), user);
        
        // Generar token
        String token = generateToken(user);
        OffsetDateTime expiresAt = OffsetDateTime.now().plusSeconds(jwtExpirationMs);
        
        // Iniciar sesión
        startUserSession(user);
        
        return new AuthResponse(
            user.getId(),
            user.getEmail(),
            user.getFullName(),
            user.getIsVerified(),
            user.getIsActive(),
            token,
            expiresAt,
            "Login exitoso"
        );
    }
    
    // ============================================================
    // LOGOUT
    // ============================================================
    
    public boolean logout(UUID userId, UUID sessionId) {
        // Finalizar la sesión específica
        if (sessionId != null) {
            UserSession session = sessionService.findById(sessionId);
            if (session != null && session.getUserId().equals(userId)) {
                sessionService.endSession(sessionId);
                return true;
            }
        }
        
        // Si no se especifica sesión, finalizar todas
        sessionService.endAllSessionsByUser(userId);
        return true;
    }
    
    // ============================================================
    // CHANGE PASSWORD
    // ============================================================
    
    public boolean changePassword(UUID userId, String currentPassword, String newPassword) {
        User user = userService.findById(userId);
        if (user == null) {
            return false;
        }
        
        // Validar contraseña actual
        if (!matchesPassword(currentPassword, user.getPassword())) {
            return false;
        }
        
        // Actualizar contraseña
        user.setPassword(encodePassword(newPassword));
        userService.update(userId, user);
        
        return true;
    }
    
    // ============================================================
    // VALIDATE TOKEN
    // ============================================================
    
    public boolean validateToken(String token) {
        // Aquí validarías el JWT
        // Por ahora, simulación simple
        try {
            // Decodificar token y verificar firma
            // Si es válido, retornar true
            return token != null && !token.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    public UUID getUserIdFromToken(String token) {
        // Aquí extraerías el userId del JWT
        // Simulación
        try {
            // Decodificar token y obtener userId
            return UUID.randomUUID(); // Reemplazar con lógica real
        } catch (Exception e) {
            return null;
        }
    }
    
    // ============================================================
    // REFRESH TOKEN
    // ============================================================
    
    public AuthResponse refreshToken(String refreshToken) {
        // Validar refresh token
        if (refreshToken == null || refreshToken.isEmpty()) {
            return new AuthResponse("Token de refresco inválido", false);
        }
        
        // Obtener userId del refresh token
        UUID userId = getUserIdFromToken(refreshToken);
        if (userId == null) {
            return new AuthResponse("Token de refresco inválido", false);
        }
        
        // Obtener usuario
        User user = userService.findById(userId);
        if (user == null) {
            return new AuthResponse("Usuario no encontrado", false);
        }
        
        // Generar nuevo token
        String newToken = generateToken(user);
        OffsetDateTime expiresAt = OffsetDateTime.now().plusSeconds(jwtExpirationMs);
        
        return new AuthResponse(
            user.getId(),
            user.getEmail(),
            user.getFullName(),
            user.getIsVerified(),
            user.getIsActive(),
            newToken,
            expiresAt,
            "Token refrescado exitosamente"
        );
    }
    
    // ============================================================
    // PRIVATE METHODS
    // ============================================================
    
    public String encodePassword(String rawPassword) {
        // En producción, usar BCrypt
        // return new BCryptPasswordEncoder().encode(rawPassword);
        return rawPassword; // SIMULACIÓN: sin encriptar
    }
    
    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        // En producción, usar BCrypt
        // return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
        return rawPassword.equals(encodedPassword); // SIMULACIÓN
    }
    
    private String generateToken(User user) {
        // En producción, generar JWT
        // Simulación: token simple con userId
        return "jwt-token-" + user.getId().toString() + "-" + System.currentTimeMillis();
    }
    
    private void startUserSession(User user) {
        // Iniciar sesión automáticamente
        UserSession session = new UserSession();
        session.setId(UUID.randomUUID());
        session.setUserId(user.getId());
        session.setDeviceId(DEVICE_ID + "-" + user.getId().toString().substring(0, 8));
        session.setDeviceName(DEVICE_NAME);
        session.setDeviceOs(DEVICE_OS);
        session.setAppVersion(APP_VERSION);
        sessionService.startSession(session);
    }
}