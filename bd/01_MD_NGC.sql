-- =============================================
-- TABLAS PARA APLICACIÓN DE NAVEGACIÓN
-- =============================================

-- 1. TABLA USUARIOS
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    document_type VARCHAR(20), -- 'DNI', 'CE', 'PASAPORTE'
    document_number VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    is_verified BOOLEAN DEFAULT false,
    last_location_lat DECIMAL(10, 8),
    last_location_lng DECIMAL(11, 8),
    last_activity_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. TABLA RUTAS
CREATE TABLE routes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    origin_name VARCHAR(255) NOT NULL,
    origin_lat DECIMAL(10, 8) NOT NULL,
    origin_lng DECIMAL(11, 8) NOT NULL,
    origin_address TEXT,
    destination_name VARCHAR(255) NOT NULL,
    destination_lat DECIMAL(10, 8) NOT NULL,
    destination_lng DECIMAL(11, 8) NOT NULL,
    destination_address TEXT,
    estimated_distance DECIMAL(10, 2), -- en kilómetros
    estimated_duration INTEGER, -- en minutos
    is_favorite BOOLEAN DEFAULT false,
    status VARCHAR(20) DEFAULT 'planning', -- 'planning', 'in_progress', 'completed', 'cancelled'
    started_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. TABLA SEGMENTOS
CREATE TABLE segments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    route_id UUID NOT NULL REFERENCES routes(id) ON DELETE CASCADE,
    sequence_number INTEGER NOT NULL,
    start_lat DECIMAL(10, 8) NOT NULL,
    start_lng DECIMAL(11, 8) NOT NULL,
    end_lat DECIMAL(10, 8) NOT NULL,
    end_lng DECIMAL(11, 8) NOT NULL,
    distance DECIMAL(10, 2), -- en kilómetros
    duration INTEGER, -- en minutos
    street_name VARCHAR(255),
    road_type VARCHAR(50), -- 'highway', 'primary', 'secondary', 'residential'
    speed_limit INTEGER, -- en km/h
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 4. TABLA DETALLE DE SEGMENTOS
CREATE TABLE segment_details (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    segment_id UUID NOT NULL REFERENCES segments(id) ON DELETE CASCADE,
    instruction TEXT NOT NULL,
    instruction_type VARCHAR(50), -- 'turn', 'straight', 'arrive', 'depart'
    maneuver VARCHAR(50), -- 'turn-left', 'turn-right', 'u-turn', 'roundabout'
    distance_to_next DECIMAL(10, 2), -- en metros
    duration_to_next INTEGER, -- en segundos
    exit_number INTEGER, -- para salidas en autopistas
    side VARCHAR(10), -- 'left', 'right'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. TABLA INCIDENCIAS
CREATE TABLE incidents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    route_id UUID NOT NULL REFERENCES routes(id) ON DELETE CASCADE,
    segment_id UUID REFERENCES segments(id) ON DELETE SET NULL,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    incident_type VARCHAR(50) NOT NULL, -- 'accident', 'robbery', 'traffic', 'road_closure', 'hazard'
    severity VARCHAR(20), -- 'low', 'medium', 'high', 'critical'
    description TEXT,
    location_lat DECIMAL(10, 8) NOT NULL,
    location_lng DECIMAL(11, 8) NOT NULL,
    location_address TEXT,
    is_panic BOOLEAN DEFAULT false, -- botón de pánico
    is_resolved BOOLEAN DEFAULT false,
    resolved_at TIMESTAMP WITH TIME ZONE,
    report_photo_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 6. TABLA DE ESTADOS DE ÁNIMO (ACOMPAÑAMIENTO)
CREATE TABLE mood_states (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    route_id UUID NOT NULL REFERENCES routes(id) ON DELETE CASCADE,
    mood_type VARCHAR(20) NOT NULL, -- 'happy', 'neutral', 'sad'
    mood_value INTEGER CHECK (mood_value >= 1 AND mood_value <= 5), -- 1=mal, 3=regular, 5=bien
    comment TEXT,
    location_lat DECIMAL(10, 8),
    location_lng DECIMAL(11, 8),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 7. TABLA CHATS / COMPARTICIONES
CREATE TABLE chats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    route_id UUID NOT NULL REFERENCES routes(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipient_user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    message_type VARCHAR(30) NOT NULL, -- 'text', 'location', 'incident_alert', 'panic_alert'
    message_content TEXT,
    location_lat DECIMAL(10, 8),
    location_lng DECIMAL(11, 8),
    is_read BOOLEAN DEFAULT false,
    read_at TIMESTAMP WITH TIME ZONE,
    is_shared BOOLEAN DEFAULT false, -- para compartir información de ruta
    share_link VARCHAR(255),
    share_expires_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 8. TABLA HISTORIAL DE MOVIMIENTOS
CREATE TABLE movement_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    route_id UUID REFERENCES routes(id) ON DELETE SET NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    altitude DECIMAL(8, 2),
    speed DECIMAL(6, 2), -- en km/h
    heading DECIMAL(5, 2), -- grados
    accuracy DECIMAL(6, 2), -- en metros
    is_moving BOOLEAN,
    battery_level INTEGER CHECK (battery_level >= 0 AND battery_level <= 100),
    recorded_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 9. TABLA CONSULTAS DE RUTAS
CREATE TABLE route_queries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    query_type VARCHAR(30) NOT NULL, -- 'search', 'filter', 'view', 'navigation'
    origin_lat DECIMAL(10, 8),
    origin_lng DECIMAL(11, 8),
    destination_lat DECIMAL(10, 8),
    destination_lng DECIMAL(11, 8),
    query_filters JSONB, -- filtros aplicados (ej: {"avoid_tolls": true, "prefer_highways": false})
    result_count INTEGER,
    response_time_ms INTEGER,
    selected_route_id UUID REFERENCES routes(id) ON DELETE SET NULL,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 10. TABLA DE SESIONES DE USUARIO (OPCIONAL PARA CONTROL DE ESTADO)
CREATE TABLE user_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    device_id VARCHAR(255),
    device_name VARCHAR(255),
    device_os VARCHAR(50),
    app_version VARCHAR(20),
    session_status VARCHAR(20) DEFAULT 'active', -- 'active', 'inactive', 'expired'
    last_ping_at TIMESTAMP WITH TIME ZONE,
    started_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- ÍNDICES PARA OPTIMIZAR CONSULTAS
-- =============================================

-- Índices para búsquedas por usuario y fechas
CREATE INDEX idx_routes_user_id ON routes(user_id);
CREATE INDEX idx_routes_status ON routes(status);
CREATE INDEX idx_routes_created_at ON routes(created_at);

CREATE INDEX idx_segments_route_id ON segments(route_id);
CREATE INDEX idx_segments_sequence ON segments(route_id, sequence_number);

CREATE INDEX idx_segment_details_segment_id ON segment_details(segment_id);

CREATE INDEX idx_incidents_route_id ON incidents(route_id);
CREATE INDEX idx_incidents_user_id ON incidents(user_id);
CREATE INDEX idx_incidents_created_at ON incidents(created_at);
CREATE INDEX idx_incidents_is_panic ON incidents(is_panic);
CREATE INDEX idx_incidents_location ON incidents(location_lat, location_lng);

CREATE INDEX idx_mood_states_user_id ON mood_states(user_id);
CREATE INDEX idx_mood_states_route_id ON mood_states(route_id);
CREATE INDEX idx_mood_states_created_at ON mood_states(created_at);

CREATE INDEX idx_chats_route_id ON chats(route_id);
CREATE INDEX idx_chats_user_id ON chats(user_id);
CREATE INDEX idx_chats_recipient_user_id ON chats(recipient_user_id);
CREATE INDEX idx_chats_created_at ON chats(created_at);

CREATE INDEX idx_movement_history_user_id ON movement_history(user_id);
CREATE INDEX idx_movement_history_route_id ON movement_history(route_id);
CREATE INDEX idx_movement_history_recorded_at ON movement_history(recorded_at);

CREATE INDEX idx_route_queries_user_id ON route_queries(user_id);
CREATE INDEX idx_route_queries_created_at ON route_queries(created_at);
CREATE INDEX idx_route_queries_query_type ON route_queries(query_type);

CREATE INDEX idx_user_sessions_user_id ON user_sessions(user_id);
CREATE INDEX idx_user_sessions_session_status ON user_sessions(session_status);

-- Índices espaciales para consultas geoespaciales
CREATE INDEX idx_users_location ON users(last_location_lat, last_location_lng);
CREATE INDEX idx_routes_origin ON routes(origin_lat, origin_lng);
CREATE INDEX idx_routes_destination ON routes(destination_lat, destination_lng);

-- =============================================
-- TRIGGER PARA ACTUALIZAR updated_at
-- =============================================

-- Función para actualizar el campo updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Aplicar trigger a todas las tablas que tienen updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_routes_updated_at BEFORE UPDATE ON routes FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_segments_updated_at BEFORE UPDATE ON segments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_segment_details_updated_at BEFORE UPDATE ON segment_details FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_incidents_updated_at BEFORE UPDATE ON incidents FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_chats_updated_at BEFORE UPDATE ON chats FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_user_sessions_updated_at BEFORE UPDATE ON user_sessions FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- =============================================
-- COMENTARIOS PARA DOCUMENTACIÓN
-- =============================================

COMMENT ON TABLE users IS 'Usuarios registrados en la aplicación';
COMMENT ON TABLE routes IS 'Rutas guardadas con origen y destino';
COMMENT ON TABLE segments IS 'Segmentos de cada ruta';
COMMENT ON TABLE segment_details IS 'Detalles de cada segmento (instrucciones paso a paso)';
COMMENT ON TABLE incidents IS 'Incidencias reportadas durante el viaje';
COMMENT ON TABLE mood_states IS 'Estados de ánimo registrados durante el acompañamiento';
COMMENT ON TABLE chats IS 'Conversaciones y comparticiones de información';
COMMENT ON TABLE movement_history IS 'Historial de movimientos y ubicaciones';
COMMENT ON TABLE route_queries IS 'Registro de consultas de rutas realizadas';
COMMENT ON TABLE user_sessions IS 'Sesiones activas de usuarios';

-- =============================================
-- EJEMPLOS DE CONSULTAS ÚTILES
-- =============================================

-- Obtener rutas activas con sus incidentes
-- SELECT r.*, COUNT(i.id) as incident_count 
-- FROM routes r 
-- LEFT JOIN incidents i ON r.id = i.route_id 
-- WHERE r.status = 'in_progress' 
-- GROUP BY r.id;

-- Obtener historial de ubicaciones de un usuario en las últimas 24 horas
-- SELECT * FROM movement_history 
-- WHERE user_id = 'uuid-here' 
-- AND recorded_at > NOW() - INTERVAL '24 hours' 
-- ORDER BY recorded_at DESC;

-- Obtener estadísticas de estado de ánimo por ruta
-- SELECT route_id, mood_type, COUNT(*) as count 
-- FROM mood_states 
-- GROUP BY route_id, mood_type;

-- Buscar incidentes de pánico cerca de una ubicación
-- SELECT * FROM incidents 
-- WHERE is_panic = true 
-- AND location_lat BETWEEN lat_min AND lat_max 
-- AND location_lng BETWEEN lng_min AND lng_max;