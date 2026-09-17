import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:ngc_movil/app/theme/app_colors.dart';
import '../controllers/home_controller.dart';

class HomeView extends GetView<HomeController> {
  const HomeView({super.key});
  @override
  Widget build(BuildContext context) {
     return Scaffold(
      backgroundColor: Colors.white,
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildHeader(context),
            _buildContentArea(context),
          ],
        ),
      ),
    );
  }
 
  // ---------------------------------------------------------
  // HEADER: saludo, avatar, notificación y stats
  // ---------------------------------------------------------
  Widget _buildHeader(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.fromLTRB(20, 60, 20, 28),
      decoration: const BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            AppColors.navyDark,
            AppColors.navy,
            AppColors.navyLight,
          ],
        ),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              // Avatar con iniciales (tu_controller_usuario.iniciales)
              InkWell(
                onTap: () => controller.goToProfile(),
                child: Container(
                  width: 44,
                  height: 44,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    border: Border.all(color: Colors.white, width: 1.5),
                    color: Colors.white.withOpacity(0.15),
                  ),
                  child: const Center(
                    child: Text(
                      'JP', // tu_controller_usuario.iniciales
                      style: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                        fontSize: 14,
                      ),
                    ),
                  ),
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      'Bienvenido de vuelta,',
                      style: TextStyle(color: Colors.white70, fontSize: 12),
                    ),
                    const Text(
                      'Juan Pérez', // tu_controller_usuario.nombre
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
              // Botón notificaciones
              InkWell(
                onTap: () {
                  // TODO: acción notificaciones
                },
                borderRadius: BorderRadius.circular(12),
                child: Container(
                  width: 40,
                  height: 40,
                  decoration: BoxDecoration(
                    color: Colors.white.withOpacity(0.15),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: const Icon(
                    Icons.notifications_none,
                    color: Colors.white,
                    size: 20,
                  ),
                ),
              ),
              SizedBox(
                width: 10,
              ),
              InkWell(
                onTap: () {
                  controller.logout();
                },
                borderRadius: BorderRadius.circular(12),
                child: Container(
                  width: 40,
                  height: 40,
                  decoration: BoxDecoration(
                    color: Colors.redAccent.withOpacity(0.15),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: const Icon(
                    Icons.logout,
                    color: Colors.red,
                    size: 20,
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 20),
 
          // Fila de estadísticas
          Row(
            children: [
              Expanded(
                child: _buildStatCard('24', 'Viajes totales'),
              ),
              const SizedBox(width: 10),
              Expanded(
                child: _buildStatCard('312', 'Km recorridos'),
              ),
              const SizedBox(width: 10),
              Expanded(
                child: _buildStatCard('2', 'Alertas enviadas'),
              ),
            ],
          ),
        ],
      ),
    );
  }
 
  Widget _buildStatCard(String value, String label) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 14),
      decoration: BoxDecoration(
        color: Colors.white.withOpacity(0.12),
        borderRadius: BorderRadius.circular(14),
      ),
      child: Column(
        children: [
          Text(
            value, // tu_controller_dashboard.valor
            style: const TextStyle(
              color: Colors.white,
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 2),
          Text(
            label,
            textAlign: TextAlign.center,
            style: const TextStyle(color: Colors.white70, fontSize: 10),
          ),
        ],
      ),
    );
  }
 
  // ---------------------------------------------------------
  // CONTENIDO: tarjeta de viaje activo, accesos rápidos, viajes
  // ---------------------------------------------------------
  Widget _buildContentArea(BuildContext context) {
    return Transform.translate(
      offset: const Offset(0, -18),
      child: Container(
        width: double.infinity,
        padding: const EdgeInsets.fromLTRB(20, 20, 20, 24),
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(24),
            topRight: Radius.circular(24),
          ),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildActiveTripCard(),
            const SizedBox(height: 24),
            _buildSectionTitle('ACCESO RÁPIDO'),
            // const SizedBox(height: 12),
            _buildQuickAccessGrid(),
            
            _buildSectionTitle('ÚLTIMOS VIAJES', actionLabel: 'Ver todos'),
            const SizedBox(height: 12),
            _buildTripItem(
              icon: Icons.navigation_outlined,
              iconColor: AppColors.safe,
              title: 'Centro → Los Próceres 12...',
              subtitle: 'Hoy, 08:30 AM · 12.4 km',
              status: 'ACTIVO',
              statusColor: AppColors.safe,
            ),
            const SizedBox(height: 10),
            _buildTripItem(
              icon: Icons.navigation_outlined,
              iconColor: Colors.grey,
              title: 'Sur → Este',
              subtitle: 'Ayer, 07:45 PM · 13.1 km',
              status: 'OK',
              statusColor: Colors.grey,
            ),
            const SizedBox(height: 10),
            _buildTripItem(
              icon: Icons.navigation_outlined,
              iconColor: Colors.grey,
              title: 'Oeste → Centro',
              subtitle: '18 May, 06:20 PM · 11.8 km',
              status: 'OK',
              statusColor: Colors.grey,
            ),
          ],
        ),
      ),
    );
  }
 
  // Tarjeta "viaje en progreso" con imagen de mapa de fondo
  Widget _buildActiveTripCard() {
    return ClipRRect(
      borderRadius: BorderRadius.circular(16),
      child: Container(
        padding: EdgeInsets.all(15),
        height: 70,
        width: double.infinity,
        decoration: const BoxDecoration(
          // Imagen/mapa de fondo de referencia (reemplazar por tu asset o mapa real)
          // image: DecorationImage(
          //   image: AssetImage('assets/images/map_preview.png'),
          //   fit: BoxFit.cover,
          // ),
          color: AppColors.navyLight, // color de respaldo si no carga la imagen
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Row(
              children: [
                Icon(Icons.car_crash_outlined, size: 40, color: Colors.white,),
                SizedBox(width: 10,),
                Text(
                  'En progreso · 25 min · 12.4 km', // tu_controller_dashboard.viajeActivo
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 13,
                    fontWeight: FontWeight.w600,
                  ),
                )
              ],
            ),
            IconButton(
              onPressed: () {
                controller.goToRouteActive();
              }, 
              icon: Icon(Icons.arrow_forward_ios, color: Colors.white,)
            )
          ],
        ),
      ),
    );
  }
 
  Widget _buildSectionTitle(String title, {String? actionLabel}) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text(
          title,
          style: const TextStyle(
            fontSize: 13,
            fontWeight: FontWeight.bold,
            color: Colors.black54,
            letterSpacing: 0.5,
          ),
        ),
        if (actionLabel != null)
          GestureDetector(
            onTap: () {
              // TODO: acción ver todos
            },
            child: Text(
              actionLabel,
              style: const TextStyle(
                fontSize: 13,
                fontWeight: FontWeight.w600,
                color: AppColors.navy,
              ),
            ),
          ),
      ],
    );
  }
 
  // Grid de 6 accesos rápidos (3 columnas x 2 filas)
  Widget _buildQuickAccessGrid() {
    final items = <_QuickAccessItem>[
      _QuickAccessItem(
        icon: Icons.my_location,
        label: 'Nueva ruta',
        iconColor: AppColors.navy,
        bgColor: const Color(0xFFEFF2FB),
      ),
      _QuickAccessItem(
        icon: Icons.navigation,
        label: 'Viaje activo',
        iconColor: AppColors.safe,
        bgColor: const Color(0xFFEAF9EE),
      ),
      _QuickAccessItem(
        icon: Icons.people_alt_outlined,
        label: 'Acompañantes',
        iconColor: const Color(0xFF7C3AED),
        bgColor: const Color(0xFFF3EEFD),
      ),
      _QuickAccessItem(
        icon: Icons.warning_amber_rounded,
        label: 'Alertas',
        iconColor: AppColors.warn,
        bgColor: const Color(0xFFFDF0E6),
      ),
      _QuickAccessItem(
        icon: Icons.access_time,
        label: 'Historial',
        iconColor: Colors.blueGrey,
        bgColor: const Color(0xFFEFF4F7),
      ),
      _QuickAccessItem(
        icon: Icons.phone_in_talk_outlined,
        label: 'Pánico SOS',
        iconColor: AppColors.sos,
        bgColor: const Color(0xFFFDECEC),
      ),
    ];
 
    return GridView.count(
      padding: EdgeInsets.all(10),
      crossAxisCount: 3,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      mainAxisSpacing: 12,
      crossAxisSpacing: 12,
      childAspectRatio: 1,
      children: items.map((item) => _buildQuickAccessCard(item)).toList(),
    );
  }
 
  Widget _buildQuickAccessCard(_QuickAccessItem item) {
    return InkWell(
      onTap: () {
        // TODO: acción de navegación para item.label
      },
      borderRadius: BorderRadius.circular(14),
      child: Container(
        decoration: BoxDecoration(
          color: item.bgColor,
          borderRadius: BorderRadius.circular(14),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(item.icon, color: item.iconColor, size: 24),
            const SizedBox(height: 6),
            Text(
              item.label,
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 11,
                fontWeight: FontWeight.w600,
                color: item.iconColor,
              ),
            ),
          ],
        ),
      ),
    );
  }
 
  // Item de la lista "Últimos viajes"
  Widget _buildTripItem({
    required IconData icon,
    required Color iconColor,
    required String title,
    required String subtitle,
    required String status,
    required Color statusColor,
  }) {
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: const Color(0xFFF9FAFB),
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: Colors.grey.shade200),
      ),
      child: Row(
        children: [
          Container(
            width: 36,
            height: 36,
            decoration: BoxDecoration(
              color: iconColor.withOpacity(0.12),
              shape: BoxShape.circle,
            ),
            child: Icon(icon, color: iconColor, size: 18),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  title, // tu_controller_dashboard.viaje.titulo
                  style: const TextStyle(
                    fontSize: 13,
                    fontWeight: FontWeight.w600,
                    color: Colors.black87,
                  ),
                  overflow: TextOverflow.ellipsis,
                ),
                const SizedBox(height: 2),
                Text(
                  subtitle,
                  style: TextStyle(fontSize: 11, color: Colors.grey.shade600),
                ),
              ],
            ),
          ),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
            decoration: BoxDecoration(
              color: statusColor.withOpacity(0.12),
              borderRadius: BorderRadius.circular(20),
            ),
            child: Text(
              status,
              style: TextStyle(
                fontSize: 10,
                fontWeight: FontWeight.bold,
                color: statusColor,
              ),
            ),
          ),
        ],
      ),
    );
  }
 
  // ---------------------------------------------------------
  // BOTTOM NAV
  // ---------------------------------------------------------
  Widget _buildBottomNav() {
    final items = <_NavItem>[
      _NavItem('Login', Icons.login),
      _NavItem('Rutas', Icons.alt_route),
      _NavItem('Mapa', Icons.map_outlined),
      _NavItem('Check', Icons.check_circle_outline),
      _NavItem('SOS', Icons.sos, color: AppColors.sos),
      _NavItem('Acomp.', Icons.people_outline),
      _NavItem('Historial', Icons.history),
      _NavItem('Perfil', Icons.person_outline, active: true),
    ];
 
    return Container(
      color: AppColors.navyDark,
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: SafeArea(
        top: false,
        child: SingleChildScrollView(
          scrollDirection: Axis.horizontal,
          child: Row(
            children: items.map((item) => _buildNavItem(item)).toList(),
          ),
        ),
      ),
    );
  }
 
  Widget _buildNavItem(_NavItem item) {
    final bgColor = item.active ? AppColors.navyLight : Colors.transparent;
    final iconColor = item.color ?? Colors.white70;
 
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 4),
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
        decoration: BoxDecoration(
          color: bgColor,
          borderRadius: BorderRadius.circular(10),
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(
              item.icon,
              size: 18,
              color: item.active ? Colors.white : iconColor,
            ),
            const SizedBox(height: 2),
            Text(
              item.label,
              style: TextStyle(
                fontSize: 10,
                color: item.active ? Colors.white : iconColor,
                fontWeight: item.active ? FontWeight.bold : FontWeight.normal,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
 
class _QuickAccessItem {
  final IconData icon;
  final String label;
  final Color iconColor;
  final Color bgColor;
 
  _QuickAccessItem({
    required this.icon,
    required this.label,
    required this.iconColor,
    required this.bgColor,
  });
}
 
class _NavItem {
  final String label;
  final IconData icon;
  final bool active;
  final Color? color;
 
  _NavItem(this.label, this.icon, {this.active = false, this.color});
}