package com.ngcapp.utpdevs.ngc_backend.controllers;

import com.ngcapp.utpdevs.ngc_backend.dtos.ChatConversation;
import com.ngcapp.utpdevs.ngc_backend.dtos.ChatReadRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.ChatRequest;
import com.ngcapp.utpdevs.ngc_backend.dtos.ChatShareRequest;
import com.ngcapp.utpdevs.ngc_backend.models.Chat;
import com.ngcapp.utpdevs.ngc_backend.services.ChatService;
import com.ngcapp.utpdevs.ngc_backend.services.RouteService;
import com.ngcapp.utpdevs.ngc_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
@CrossOrigin(origins = "*")
public class ChatController {
    
    @Autowired
    private ChatService chatService;
    
    @Autowired
    private RouteService routeService;
    
    @Autowired
    private UserService userService;
    
    // ============================================================
    // GET ENDPOINTS
    // ============================================================
    
    @GetMapping
    public ResponseEntity<List<Chat>> getAll() {
        return ResponseEntity.ok(chatService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Chat> getById(@PathVariable UUID id) {
        Chat chat = chatService.findById(id);
        if (chat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chat);
    }
    
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<Chat>> getByRoute(@PathVariable UUID routeId) {
        return ResponseEntity.ok(chatService.findByRouteId(routeId));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Chat>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(chatService.findByUserId(userId));
    }
    
    @GetMapping("/recipient/{recipientUserId}")
    public ResponseEntity<List<Chat>> getByRecipient(@PathVariable UUID recipientUserId) {
        return ResponseEntity.ok(chatService.findByRecipientUserId(recipientUserId));
    }
    
    @GetMapping("/conversation")
    public ResponseEntity<List<Chat>> getConversation(
            @RequestParam UUID userId,
            @RequestParam UUID otherUserId) {
        return ResponseEntity.ok(chatService.findConversation(userId, otherUserId));
    }
    
    @GetMapping("/type/{messageType}")
    public ResponseEntity<List<Chat>> getByType(@PathVariable String messageType) {
        return ResponseEntity.ok(chatService.findByMessageType(messageType));
    }
    
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<Chat>> getUnread(@PathVariable UUID userId) {
        return ResponseEntity.ok(chatService.findUnreadByUser(userId));
    }
    
    @GetMapping("/unread/count/{userId}")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable UUID userId) {
        long count = chatService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }
    
    @GetMapping("/shared")
    public ResponseEntity<List<Chat>> getShared() {
        return ResponseEntity.ok(chatService.findSharedMessages());
    }
    
    @GetMapping("/alerts")
    public ResponseEntity<List<Chat>> getAlerts() {
        return ResponseEntity.ok(chatService.findAlertMessages());
    }
    
    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<ChatConversation>> getConversations(@PathVariable UUID userId) {
        return ResponseEntity.ok(chatService.getConversations(userId));
    }
    
    // ============================================================
    // POST ENDPOINTS
    // ============================================================
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ChatRequest request) {
        // Validar existencia
        Map<String, String> errors = new HashMap<>();
        
        if (!routeService.existsById(request.getRouteId())) {
            errors.put("route_id", "La ruta no existe");
        }
        
        if (!userService.existsById(request.getUserId())) {
            errors.put("user_id", "El usuario no existe");
        }
        
        if (request.getRecipientUserId() != null && 
            !userService.existsById(request.getRecipientUserId())) {
            errors.put("recipient_user_id", "El usuario destinatario no existe");
        }
        
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        
        Chat chat = new Chat();
        chat.setRouteId(request.getRouteId());
        chat.setUserId(request.getUserId());
        chat.setRecipientUserId(request.getRecipientUserId());
        chat.setMessageType(request.getMessageType());
        chat.setMessageContent(request.getMessageContent());
        chat.setLocationLat(request.getLocationLat());
        chat.setLocationLng(request.getLocationLng());
        chat.setIsShared(request.getIsShared() != null ? request.getIsShared() : false);
        chat.setShareLink(request.getShareLink());
        chat.setShareExpiresAt(request.getShareExpiresAt());
        chat.setIsRead(false);
        
        // Para mensajes de alerta, forzar que sea para todos
        if ("incident_alert".equals(chat.getMessageType()) || 
            "panic_alert".equals(chat.getMessageType())) {
            // Aquí podrías notificar a todos los usuarios de la ruta
        }
        
        Chat created = chatService.create(chat);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Mensaje enviado exitosamente");
        response.put("chat", created);
        
        // Si es alerta, respuesta especial
        if ("incident_alert".equals(created.getMessageType())) {
            response.put("alert", "Alerta de incidente enviada");
        } else if ("panic_alert".equals(created.getMessageType())) {
            response.put("alert", "Alerta de pánico enviada");
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    // ============================================================
    // PUT ENDPOINTS
    // ============================================================
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody ChatRequest request) {
        
        Chat existing = chatService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Solo permitir actualizar contenido, ubicación y compartir
        existing.setMessageContent(request.getMessageContent());
        existing.setLocationLat(request.getLocationLat());
        existing.setLocationLng(request.getLocationLng());
        existing.setIsShared(request.getIsShared() != null ? request.getIsShared() : existing.getIsShared());
        existing.setShareLink(request.getShareLink());
        existing.setShareExpiresAt(request.getShareExpiresAt());
        
        Chat updated = chatService.update(id, existing);
        return ResponseEntity.ok(updated);
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable UUID id) {
        Chat chat = chatService.markAsRead(id);
        if (chat == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(Map.of(
            "message", "Mensaje marcado como leído",
            "chat", chat,
            "readAt", chat.getReadAt()
        ));
    }
    
    @PutMapping("/read-batch")
    public ResponseEntity<?> markMultipleAsRead(@RequestBody ChatReadRequest request) {
        if (request.getChatIds().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Se requiere al menos un ID"));
        }
        
        List<Chat> updated = chatService.markMultipleAsRead(request.getChatIds());
        
        return ResponseEntity.ok(Map.of(
            "message", "Mensajes marcados como leídos",
            "count", updated.size(),
            "chats", updated
        ));
    }
    
    @PutMapping("/read-all/{userId}")
    public ResponseEntity<?> markAllAsRead(@PathVariable UUID userId) {
        List<Chat> updated = chatService.markAllAsReadForUser(userId);
        
        return ResponseEntity.ok(Map.of(
            "message", "Todos los mensajes marcados como leídos",
            "count", updated.size(),
            "chats", updated
        ));
    }
    
    @PutMapping("/share")
    public ResponseEntity<?> shareMessage(@RequestBody ChatShareRequest request) {
        Chat chat = chatService.shareMessage(
            request.getChatId(),
            request.getShareLink(),
            request.getShareExpiresAt()
        );
        
        if (chat == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(Map.of(
            "message", "Mensaje compartido exitosamente",
            "chat", chat,
            "shareLink", chat.getShareLink(),
            "expiresAt", chat.getShareExpiresAt()
        ));
    }
    
    // ============================================================
    // DELETE ENDPOINTS
    // ============================================================
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!chatService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        chatService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/route/{routeId}")
    public ResponseEntity<?> deleteByRoute(@PathVariable UUID routeId) {
        List<Chat> chats = chatService.findByRouteId(routeId);
        if (chats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "No se encontraron mensajes para esta ruta"));
        }
        
        chatService.deleteByRouteId(routeId);
        
        return ResponseEntity.ok(Map.of(
            "message", "Mensajes eliminados exitosamente",
            "routeId", routeId,
            "deletedCount", chats.size()
        ));
    }
    
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteByUser(@PathVariable UUID userId) {
        List<Chat> chats = chatService.findByUserId(userId);
        if (chats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "No se encontraron mensajes para este usuario"));
        }
        
        chatService.deleteByUserId(userId);
        
        return ResponseEntity.ok(Map.of(
            "message", "Mensajes eliminados exitosamente",
            "userId", userId,
            "deletedCount", chats.size()
        ));
    }
}