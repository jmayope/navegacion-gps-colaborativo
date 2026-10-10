import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:ngc_movil/app/theme/app_colors.dart';

import '../controllers/map_controller.dart';

class MapView extends GetView<MapController> {
  const MapView({super.key});
  @override
  Widget build(BuildContext context) {
    final topPadding = MediaQuery.of(context).padding.top;

    return Scaffold(
      backgroundColor: const Color(0xFF151A30),
      body: Stack(
        children: [
          // ================= MAPA (fondo) =================
          // TODO: reemplazar por tu widget de mapa real (GoogleMap, flutter_map, etc.)
          // o por una imagen: Image.asset('assets/images/map_dark.png', fit: BoxFit.cover)
          Positioned.fill(
            child: CustomPaint(painter: _MapPlaceholderPainter()),
          ),
 
          // ================= HEADER + PANEL INFERIOR =================
          Column(
            children: [
              _buildHeader(context, topPadding),
              const Spacer(),
              _buildStatsPanel(),
            ],
          ),
 
          // ================= BOTONES FLOTANTES =================
          Positioned(
            top: topPadding,
            right: 12,
            child: Column(
              children: [
                _FloatingActionBox(
                  color: AppColors.warn,
                  icon: Icons.warning_rounded,
                  label: 'Alerta',
                  onTap: () {
                    // TODO: acción alerta
                  },
                ),
                const SizedBox(height: 12),
                _FloatingActionBox(
                  color: AppColors.sos,
                  title: 'SOS',
                  label: 'PÁNICO',
                  onTap: () {
                    // TODO: acción pánico SOS
                  },
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
 
  // ---------------------------------------------------------
  // HEADER verde
  // ---------------------------------------------------------
  Widget _buildHeader(BuildContext context, double topPadding) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.fromLTRB(16, topPadding + 12, 16, 14),
      color: AppColors.safe,
      child: Row(
        children: [
          InkWell(
            onTap: () => Navigator.maybePop(context),
            borderRadius: BorderRadius.circular(10),
            child: Container(
              width: 32,
              height: 32,
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(10),
              ),
              child: const Icon(
                Icons.arrow_back_ios_new,
                size: 16,
                color: AppColors.safe,
              ),
            ),
          ),
          const Expanded(
            child: Text(
              'Viaje activo',
              textAlign: TextAlign.center,
              style: TextStyle(
                color: Colors.white,
                fontSize: 17,
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
          // Espacio a la derecha (queda detrás de los botones flotantes)
          const SizedBox(width: 32),
        ],
      ),
    );
  }
 
  // ---------------------------------------------------------
  // PANEL INFERIOR: velocidad, tiempo, distancia
  // ---------------------------------------------------------
  Widget _buildStatsPanel() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.fromLTRB(24, 18, 24, 26),
      color: Colors.white,
      child: SafeArea(
        top: false,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          crossAxisAlignment: CrossAxisAlignment.end,
          children: [
            _statColumn(
              label: 'Velocidad',
              value: '45', // tu_controller_viaje.velocidad
              unit: 'km/h',
            ),
            _statColumn(
              label: 'Tiempo de viaje',
              value: '00:25:47', // tu_controller_viaje.tiempo
              valueSize: 22,
            ),
            _statColumn(
              label: 'Distancia',
              value: '12.4', // tu_controller_viaje.distancia
              unit: 'km',
            ),
          ],
        ),
      ),
    );
  }
 
  Widget _statColumn({
    required String label,
    required String value,
    String? unit,
    double valueSize = 24,
  }) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        Text(
          label,
          style: TextStyle(fontSize: 11, color: Colors.grey.shade600),
        ),
        const SizedBox(height: 4),
        Text(
          value,
          style: TextStyle(
            fontSize: valueSize,
            fontWeight: FontWeight.bold,
            color: const Color(0xFF1F2937),
          ),
        ),
        if (unit != null) ...[
          const SizedBox(height: 2),
          Text(
            unit,
            style: TextStyle(fontSize: 10, color: Colors.grey.shade600),
          ),
        ] else
          const SizedBox(height: 14), // mantiene alineación con las otras columnas
      ],
    );
  }
}
 
// ==========================================================
// Botón flotante cuadrado (Alerta / SOS)
// ==========================================================
class _FloatingActionBox extends StatelessWidget {
  final Color color;
  final IconData? icon;
  final String? title;
  final String label;
  final VoidCallback onTap;
 
  const _FloatingActionBox({
    required this.color,
    required this.label,
    required this.onTap,
    this.icon,
    this.title,
  });
 
  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(14),
      child: Container(
        width: 56,
        height: 56,
        decoration: BoxDecoration(
          color: color,
          borderRadius: BorderRadius.circular(14),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.3),
              blurRadius: 8,
              offset: const Offset(0, 3),
            ),
          ],
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            if (icon != null) Icon(icon, color: Colors.white, size: 22),
            if (title != null)
              Text(
                title!,
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 15,
                  fontWeight: FontWeight.w900,
                ),
              ),
            const SizedBox(height: 2),
            Text(
              label,
              style: const TextStyle(
                color: Colors.white,
                fontSize: 8,
                fontWeight: FontWeight.w600,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
 
// ==========================================================
// Mapa oscuro de ejemplo: cuadrícula + ruta + posición actual
// ==========================================================
class _MapPlaceholderPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    // Fondo
    canvas.drawRect(
      Offset.zero & size,
      Paint()..color = const Color(0xFF151A30),
    );
 
    // Calles (cuadrícula)
    final thin = Paint()
      ..color = const Color(0xFF1F2540)
      ..strokeWidth = 3;
    final thick = Paint()
      ..color = const Color(0xFF232A47)
      ..strokeWidth = 5;
 
    final xs = [0.2, 0.5, 0.85];
    final ys = [0.25, 0.55, 0.75];
 
    for (final x in xs) {
      canvas.drawLine(Offset(size.width * x, 0),
          Offset(size.width * x, size.height), thick);
    }
    for (final y in ys) {
      canvas.drawLine(Offset(0, size.height * y),
          Offset(size.width, size.height * y), thin);
    }
 
    // Ruta recorrida
    final start = Offset(size.width * 0.22, size.height * 0.36);
    final path = Path()
      ..moveTo(start.dx, start.dy)
      ..cubicTo(
        size.width * 0.25, size.height * 0.22,
        size.width * 0.35, size.height * 0.15,
        size.width * 0.45, size.height * 0.05,
      )
      ..lineTo(size.width * 0.55, 0);
 
    canvas.drawPath(
      path,
      Paint()
        ..color = const Color(0xFF3B82F6).withOpacity(0.35)
        ..style = PaintingStyle.stroke
        ..strokeWidth = 8
        ..strokeCap = StrokeCap.round,
    );
    canvas.drawPath(
      path,
      Paint()
        ..color = const Color(0xFF4F7DF3)
        ..style = PaintingStyle.stroke
        ..strokeWidth = 4
        ..strokeCap = StrokeCap.round,
    );
 
    // Posición actual
    canvas.drawCircle(
        start, 10, Paint()..color = const Color(0xFF3B82F6).withOpacity(0.3));
    canvas.drawCircle(start, 6, Paint()..color = Colors.white);
    canvas.drawCircle(start, 4, Paint()..color = const Color(0xFF3B82F6));
  }
 
  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}