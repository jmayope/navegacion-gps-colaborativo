package com.ngcapp.utpdevs.ngc_backend.services;

import com.ngcapp.utpdevs.ngc_backend.dtos.ChatConversation;
import com.ngcapp.utpdevs.ngc_backend.dtos.ChatMessage;
import com.ngcapp.utpdevs.ngc_backend.models.Chat;
import com.ngcapp.utpdevs.ngc_backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {
    
    @Autowired
    private SupabaseCrudService crudService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RouteService routeService;
    
    private static final String TABLE = "chats";
    
    // Obtener todos los mensajes
    public List<Chat> findAll() {
        return crudService.findAll(TABLE, Chat[].class);
    }
    
    // Obtener mensaje por ID
    public Chat findById(UUID id) {
        return crudService.findById(TABLE, id, Chat[].class);
    }
    
    // Obtener mensajes por ruta
    public List<Chat> findByRouteId(UUID routeId) {
        return crudService.find(
            TABLE, 
            "route_id=eq." + routeId + "&order=created_at.asc", 
            Chat[].class
        );
    }
    
    // Obtener mensajes por usuario
    public List<Chat> findByUserId(UUID userId) {
        return crudService.find(
            TABLE, 
            "user_id=eq." + userId + "&order=created_at.desc", 
            Chat[].class
        );
    }
    
    // Obtener mensajes por destinatario
    public List<Chat> findByRecipientUserId(UUID recipientUserId) {
        return crudService.find(
            TABLE, 
            "recipient_user_id=eq." + recipientUserId + "&order=created_at.desc", 
            Chat[].class
        );
    }
    
    // Obtener mensajes entre usuarios (conversación)
    public List<Chat> findConversation(UUID userId, UUID otherUserId) {
        String query = "or=(and(user_id.eq." + userId + ",recipient_user_id.eq." + otherUserId + 
                      "),and(user_id.eq." + otherUserId + ",recipient_user_id.eq." + userId + "))" +
                      "&order=created_at.asc";
        return crudService.find(TABLE, query, Chat[].class);
    }
    
    // Obtener mensajes por tipo
    public List<Chat> findByMessageType(String messageType) {
        return crudService.find(
            TABLE, 
            "message_type=eq." + messageType + "&order=created_at.desc", 
            Chat[].class
        );
    }
    
    // Obtener mensajes no leídos de un usuario
    public List<Chat> findUnreadByUser(UUID userId) {
        return crudService.find(
            TABLE, 
            "recipient_user_id=eq." + userId + "&is_read=eq.false&order=created_at.asc", 
            Chat[].class
        );
    }
    
    // Obtener mensajes compartidos
    public List<Chat> findSharedMessages() {
        return crudService.find(
            TABLE, 
            "is_shared=eq.true&order=created_at.desc", 
            Chat[].class
        );
    }
    
    // Obtener mensajes de alerta (incidente/pánico)
    public List<Chat> findAlertMessages() {
        return crudService.find(
            TABLE, 
            "or=(message_type.eq.incident_alert,message_type.eq.panic_alert)&order=created_at.desc", 
            Chat[].class
        );
    }
    
    // Crear mensaje
    public Chat create(Chat chat) {
        // Si es alerta, asegurar que se envíe a todos los usuarios de la ruta
        if ("incident_alert".equals(chat.getMessageType()) || 
            "panic_alert".equals(chat.getMessageType())) {
            chat.setIsRead(false);
        }
        
        if (chat.getIsShared() == null) {
            chat.setIsShared(false);
        }
        
        return crudService.insert(TABLE, chat, Chat[].class);
    }
    
    // Marcar como leído
    public Chat markAsRead(UUID id) {
        Chat existing = findById(id);
        if (existing == null) {
            return null;
        }
        
        existing.setIsRead(true);
        existing.setReadAt(OffsetDateTime.now());
        return update(id, existing);
    }
    
    // Marcar múltiples mensajes como leídos
    public List<Chat> markMultipleAsRead(List<UUID> ids) {
        List<Chat> updated = new ArrayList<>();
        for (UUID id : ids) {
            Chat chat = markAsRead(id);
            if (chat != null) {
                updated.add(chat);
            }
        }
        return updated;
    }
    
    // Marcar todos los mensajes de un usuario como leídos
    public List<Chat> markAllAsReadForUser(UUID userId) {
        List<Chat> unread = findUnreadByUser(userId);
        List<Chat> updated = new ArrayList<>();
        
        for (Chat chat : unread) {
            chat.setIsRead(true);
            chat.setReadAt(OffsetDateTime.now());
            Chat result = update(chat.getId(), chat);
            if (result != null) {
                updated.add(result);
            }
        }
        
        return updated;
    }
    
    // Compartir mensaje
    public Chat shareMessage(UUID id, String shareLink, OffsetDateTime expiresAt) {
        Chat existing = findById(id);
        if (existing == null) {
            return null;
        }
        
        existing.setIsShared(true);
        existing.setShareLink(shareLink);
        if (expiresAt != null) {
            existing.setShareExpiresAt(expiresAt);
        }
        
        return update(id, existing);
    }
    
    // Actualizar mensaje
    public Chat update(UUID id, Chat chat) {
        return crudService.update(TABLE, id, chat, Chat[].class);
    }
    
    // Eliminar mensaje
    public void delete(UUID id) {
        crudService.delete(TABLE, id);
    }
    
    // Eliminar todos los mensajes de una ruta
    public void deleteByRouteId(UUID routeId) {
        crudService.delete(TABLE, "route_id=eq." + routeId);
    }
    
    // Eliminar todos los mensajes de un usuario
    public void deleteByUserId(UUID userId) {
        crudService.delete(TABLE, "user_id=eq." + userId);
    }
    
    // Verificar si existe
    public boolean existsById(UUID id) {
        return crudService.exists(TABLE, "id=eq." + id);
    }
    
    // Obtener conversaciones de un usuario
    public List<ChatConversation> getConversations(UUID userId) {
        List<Chat> messages = findByUserId(userId);
        messages.addAll(findByRecipientUserId(userId));
        
        // Agrupar por interlocutor
        Map<UUID, List<Chat>> conversationsByUser = new HashMap<>();
        
        for (Chat message : messages) {
            UUID otherUserId;
            if (message.getUserId().equals(userId)) {
                otherUserId = message.getRecipientUserId();
            } else {
                otherUserId = message.getUserId();
            }
            
            if (otherUserId != null) {
                conversationsByUser.computeIfAbsent(otherUserId, k -> new ArrayList<>())
                    .add(message);
            }
        }
        
        List<ChatConversation> result = new ArrayList<>();
        
        for (Map.Entry<UUID, List<Chat>> entry : conversationsByUser.entrySet()) {
            UUID otherUserId = entry.getKey();
            List<Chat> userMessages = entry.getValue();
            
            // Ordenar por fecha
            userMessages.sort(Comparator.comparing(Chat::getCreatedAt));
            
            ChatConversation conversation = new ChatConversation();
            conversation.setUserId(otherUserId);
            
            // Obtener nombre del usuario
            User user = userService.findById(otherUserId);
            if (user != null) {
                conversation.setUserName(user.getFullName());
            }
            
            // Obtener información de la ruta del último mensaje
            Chat lastMessage = userMessages.get(userMessages.size() - 1);
            conversation.setRouteId(lastMessage.getRouteId());
            
            // Obtener nombre de la ruta
            // Asumiendo que RouteService tiene un método para obtener nombre
            // conversation.setRouteName(routeService.getRouteName(lastMessage.getRouteId()));
            
            conversation.setTotalMessages(userMessages.size());
            
            // Contar no leídos
            long unreadCount = userMessages.stream()
                .filter(m -> m.getRecipientUserId() != null && 
                            m.getRecipientUserId().equals(userId) && 
                            !m.getIsRead())
                .count();
            conversation.setUnreadCount((int) unreadCount);
            
            // Último mensaje
            ChatMessage lastMsg = new ChatMessage();
            lastMsg.setId(lastMessage.getId());
            lastMsg.setUserId(lastMessage.getUserId());
            if (user != null) {
                lastMsg.setUserName(user.getFullName());
            }
            lastMsg.setMessageType(lastMessage.getMessageType());
            lastMsg.setMessageContent(lastMessage.getMessageContent());
            lastMsg.setLocationLat(lastMessage.getLocationLat());
            lastMsg.setLocationLng(lastMessage.getLocationLng());
            lastMsg.setIsRead(lastMessage.getIsRead());
            lastMsg.setCreatedAt(lastMessage.getCreatedAt());
            lastMsg.setReadAt(lastMessage.getReadAt());
            conversation.setLastMessage(lastMsg);
            
            // Todos los mensajes
            List<ChatMessage> chatMessages = new ArrayList<>();
            for (Chat chat : userMessages) {
                ChatMessage msg = new ChatMessage();
                msg.setId(chat.getId());
                msg.setUserId(chat.getUserId());
                // Obtener nombre del remitente
                User sender = userService.findById(chat.getUserId());
                if (sender != null) {
                    msg.setUserName(sender.getFullName());
                }
                msg.setMessageType(chat.getMessageType());
                msg.setMessageContent(chat.getMessageContent());
                msg.setLocationLat(chat.getLocationLat());
                msg.setLocationLng(chat.getLocationLng());
                msg.setIsRead(chat.getIsRead());
                msg.setCreatedAt(chat.getCreatedAt());
                msg.setReadAt(chat.getReadAt());
                chatMessages.add(msg);
            }
            conversation.setMessages(chatMessages);
            
            result.add(conversation);
        }
        
        // Ordenar por fecha del último mensaje
        result.sort((c1, c2) -> {
            OffsetDateTime d1 = c1.getLastMessage() != null ? 
                c1.getLastMessage().getCreatedAt() : OffsetDateTime.MIN;
            OffsetDateTime d2 = c2.getLastMessage() != null ? 
                c2.getLastMessage().getCreatedAt() : OffsetDateTime.MIN;
            return d2.compareTo(d1);
        });
        
        return result;
    }
    
    // Obtener conteo de no leídos de un usuario
    public long getUnreadCount(UUID userId) {
        List<Chat> unread = findUnreadByUser(userId);
        return unread.size();
    }
}