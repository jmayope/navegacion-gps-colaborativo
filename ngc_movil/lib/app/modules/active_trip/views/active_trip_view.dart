import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:ngc_movil/app/routes/app_pages.dart';
import 'package:ngc_movil/app/theme/app_colors.dart';

import '../controllers/active_trip_controller.dart';

class ActiveTripView extends GetView<ActiveTripController> {
  const ActiveTripView({super.key});
  @override
  Widget build(BuildContext context) {
    final topPadding = MediaQuery.of(context).padding.top;

    void _openCompanionsSheet() {
      showModalBottomSheet(
        context: context,
        isScrollControlled: true,
        backgroundColor: Colors.transparent,
        builder: (_) => _CompanionsSheet(
          companions: controller.companions,
          onChanged: () => {},
        ),
      );
    }

    return Scaffold(
      backgroundColor: Color(0xFF151A30),
      body: Stack(
        children: [
          // ================= MAPA (fondo) =================
          // TODO: reemplazar por tu widget de mapa real o por una imagen:
          // Image.asset('assets/imgs/map_dark.png', fit: BoxFit.cover)
          Positioned.fill(
            child: CustomPaint(painter: _MapPlaceholderPainter()),
          ),
 
          // ================= HEADER + PANEL INFERIOR =================
          Column(
            children: [
              _buildHeader(context, topPadding),
              const Spacer(),
              _buildBottomPanel(),
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
                const SizedBox(height: 12),
                _FloatingActionBox(
                  color: Color(0xFF7C3AED),
                  icon: Icons.people_alt,
                  label: 'Acomp.',
                  badge: controller.companions.length,
                  onTap: _openCompanionsSheet,
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
      color: AppColors.navy,
      child: Row(
        children: [
          InkWell(
            onTap: () => Get.offAllNamed(Routes.HOME),
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
  // PANEL INFERIOR: acompañantes + velocidad, tiempo, distancia
  // ---------------------------------------------------------
  Widget _buildBottomPanel() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.fromLTRB(24, 14, 24, 22),
      color: Colors.white,
      child: SafeArea(
        top: false,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            _buildCompanionsRow(),
            const Padding(
              padding: EdgeInsets.symmetric(vertical: 12),
              child: Divider(height: 1, color: Color(0xFFE5E7EB)),
            ),
            Row(
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
          ],
        ),
      ),
    );
  }
 
  // Fila con avatares de acompañantes + botón agregar
  Widget _buildCompanionsRow() {
    const maxAvatars = 3;
    final visible = controller.companions.take(maxAvatars).toList();
    final extra = controller.companions.length - visible.length;

    void _openCompanionsSheet() {
      showModalBottomSheet(
        context: Get.context!,
        isScrollControlled: true,
        backgroundColor: Colors.transparent,
        builder: (_) => _CompanionsSheet(
          companions: controller.companions.value,
          onChanged: () => {},
        ),
      );
    }

    return Row(
      children: [
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Acompañantes',
                style: TextStyle(fontSize: 11, color: Colors.grey.shade600),
              ),
              const SizedBox(height: 6),
              if (controller.companions.isEmpty)
                Text(
                  'Nadie sigue tu viaje',
                  style: TextStyle(fontSize: 13, color: Colors.grey.shade500),
                )
              else
                Row(
                  children: [
                    for (final c in visible)
                      Padding(
                        padding: const EdgeInsets.only(right: 6),
                        child: _avatar(c.initials),
                      ),
                    if (extra > 0)
                      _avatar('+$extra', color: Colors.grey.shade500),
                  ],
                ),
            ],
          ),
        ),
        OutlinedButton.icon(
          onPressed: _openCompanionsSheet,
          icon: const Icon(Icons.person_add_alt_1, size: 16, color: Color(0xFF7C3AED)),
          label: const Text(
            'Agregar',
            style: TextStyle(
              color: Color(0xFF7C3AED),
              fontWeight: FontWeight.w600,
              fontSize: 13,
            ),
          ),
          style: OutlinedButton.styleFrom(
            side: const BorderSide(color: Color(0xFF7C3AED)),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10),
            ),
          ),
        ),
      ],
    );
  }
 
  Widget _avatar(String text, {Color color = Colors.green}) {
    return Container(
      width: 34,
      height: 34,
      decoration: BoxDecoration(
        color: color.withOpacity(0.12),
        shape: BoxShape.circle,
        border: Border.all(color: color, width: 1.2),
      ),
      child: Center(
        child: Text(
          text,
          style: TextStyle(
            fontSize: 12,
            fontWeight: FontWeight.bold,
            color: color,
          ),
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
          const SizedBox(height: 14),
      ],
    );
  }
}
 
// ==========================================================
// BOTTOM SHEET: lista y alta de acompañantes
// ==========================================================
class _CompanionsSheet extends StatefulWidget {
  final List<Companion> companions;
  final VoidCallback onChanged;
 
  const _CompanionsSheet({
    required this.companions,
    required this.onChanged,
  });
 
  @override
  State<_CompanionsSheet> createState() => _CompanionsSheetState();
}
 
class _CompanionsSheetState extends State<_CompanionsSheet> {
 
  // TODO: reemplazar por tus controllers reales
  final tu_controller_acomp_nombre = TextEditingController();
  final tu_controller_acomp_telefono = TextEditingController();
 
  bool _showError = false;
 
  // TODO: sugeridos = contactos de emergencia de tu controller
  final List<Companion> _suggested = const [
    Companion('Carlos Pérez', '+51 912 345 678'),
    Companion('Lucía Rojas', '+51 955 111 222'),
  ];
 
  @override
  void dispose() {
    tu_controller_acomp_nombre.dispose();
    tu_controller_acomp_telefono.dispose();
    super.dispose();
  }
 
  void _add(Companion c) {
    setState(() {
      widget.companions.add(c);
      _showError = false;
    });
    widget.onChanged();
    // TODO: notificar al acompañante / backend
  }
 
  void _remove(int index) {
    setState(() => widget.companions.removeAt(index));
    widget.onChanged();
    // TODO: dejar de compartir viaje con este acompañante
  }
 
  void _submit() {
    final name = tu_controller_acomp_nombre.text.trim();
    final phone = tu_controller_acomp_telefono.text.trim();
 
    if (name.isEmpty || phone.isEmpty) {
      setState(() => _showError = true);
      return;
    }
 
    _add(Companion(name, phone));
    tu_controller_acomp_nombre.clear();
    tu_controller_acomp_telefono.clear();
    FocusScope.of(context).unfocus();
  }
 
  @override
  Widget build(BuildContext context) {
    final pending = _suggested
        .where((s) => !widget.companions.any((c) => c.phone == s.phone))
        .toList();
 
    return Padding(
      padding: EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom),
      child: Container(
        constraints: BoxConstraints(
          maxHeight: MediaQuery.of(context).size.height * 0.85,
        ),
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.vertical(top: Radius.circular(28)),
        ),
        child: SingleChildScrollView(
          padding: const EdgeInsets.fromLTRB(24, 12, 24, 24),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Handle
              Center(
                child: Container(
                  width: 40,
                  height: 4,
                  decoration: BoxDecoration(
                    color: Colors.grey.shade300,
                    borderRadius: BorderRadius.circular(2),
                  ),
                ),
              ),
              const SizedBox(height: 16),
 
              const Text(
                'Acompañantes',
                style: TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                  color: AppColors.navy,
                ),
              ),
              const SizedBox(height: 4),
              Text(
                'Podrán seguir tu viaje en tiempo real',
                style: TextStyle(fontSize: 13, color: Colors.grey.shade600),
              ),
              const SizedBox(height: 16),
 
              // ---------- Lista actual ----------
              if (widget.companions.isEmpty)
                Container(
                  width: double.infinity,
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    color: const Color(0xFFF9FAFB),
                    borderRadius: BorderRadius.circular(14),
                  ),
                  child: Text(
                    'Aún no has agregado acompañantes.',
                    textAlign: TextAlign.center,
                    style: TextStyle(fontSize: 13, color: Colors.grey.shade600),
                  ),
                )
              else
                for (int i = 0; i < widget.companions.length; i++) ...[
                  _buildCompanionTile(i, widget.companions[i]),
                  const SizedBox(height: 8),
                ],
 
              // ---------- Sugeridos ----------
              if (pending.isNotEmpty) ...[
                const SizedBox(height: 12),
                Text(
                  'CONTACTOS DE EMERGENCIA',
                  style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.bold,
                    color: Colors.grey.shade600,
                    letterSpacing: 0.5,
                  ),
                ),
                const SizedBox(height: 8),
                Wrap(
                  spacing: 8,
                  runSpacing: 8,
                  children: [
                    for (final s in pending)
                      ActionChip(
                        avatar: const Icon(Icons.add, size: 16, color: Color(0xFF7C3AED)),
                        label: Text(
                          s.name,
                          style: const TextStyle(
                            color: Color(0xFF7C3AED),
                            fontWeight: FontWeight.w600,
                            fontSize: 12,
                          ),
                        ),
                        backgroundColor: const Color(0xFFF3EEFD),
                        side: BorderSide.none,
                        onPressed: () => _add(s),
                      ),
                  ],
                ),
              ],
 
              // ---------- Formulario nuevo ----------
              const SizedBox(height: 20),
              Text(
                'AGREGAR NUEVO',
                style: TextStyle(
                  fontSize: 12,
                  fontWeight: FontWeight.bold,
                  color: Colors.grey.shade600,
                  letterSpacing: 0.5,
                ),
              ),
              const SizedBox(height: 10),
              _buildField(
                controller: tu_controller_acomp_nombre,
                hint: 'Nombre completo',
                icon: Icons.person_outline,
                keyboardType: TextInputType.name,
              ),
              const SizedBox(height: 12),
              _buildField(
                controller: tu_controller_acomp_telefono,
                hint: 'Teléfono',
                icon: Icons.phone_outlined,
                keyboardType: TextInputType.phone,
              ),
              if (_showError) ...[
                const SizedBox(height: 8),
                const Text(
                  'Completa el nombre y el teléfono.',
                  style: TextStyle(fontSize: 12, color: AppColors.danger),
                ),
              ],
              const SizedBox(height: 16),
              SizedBox(
                width: double.infinity,
                height: 50,
                child: ElevatedButton.icon(
                  onPressed: _submit,
                  icon: const Icon(Icons.person_add_alt_1,
                      color: Colors.white, size: 18),
                  label: const Text(
                    'AGREGAR ACOMPAÑANTE',
                    style: TextStyle(
                      color: Colors.white,
                      fontWeight: FontWeight.bold,
                      letterSpacing: 0.5,
                    ),
                  ),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: AppColors.navy,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
 
  Widget _buildCompanionTile(int index, Companion c) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
      decoration: BoxDecoration(
        color: const Color(0xFFF9FAFB),
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: Colors.grey.shade200),
      ),
      child: Row(
        children: [
          Container(
            width: 38,
            height: 38,
            decoration: BoxDecoration(
              color: Color(0xFF7C3AED).withOpacity(0.12),
              shape: BoxShape.circle,
            ),
            child: Center(
              child: Text(
                c.initials,
                style: const TextStyle(
                  fontSize: 13,
                  fontWeight: FontWeight.bold,
                  color: Color(0xFF7C3AED),
                ),
              ),
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  c.name,
                  style: const TextStyle(
                    fontSize: 14,
                    fontWeight: FontWeight.w600,
                    color: Colors.black87,
                  ),
                ),
                const SizedBox(height: 2),
                Text(
                  c.phone,
                  style: TextStyle(fontSize: 12, color: Colors.grey.shade600),
                ),
              ],
            ),
          ),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
            decoration: BoxDecoration(
              color: AppColors.safe.withOpacity(0.12),
              borderRadius: BorderRadius.circular(20),
            ),
            child: const Text(
              'SIGUIENDO',
              style: TextStyle(
                fontSize: 9,
                fontWeight: FontWeight.bold,
                color: AppColors.safe,
              ),
            ),
          ),
          IconButton(
            onPressed: () => _remove(index),
            icon: Icon(Icons.close, size: 18, color: Colors.grey.shade500),
          ),
        ],
      ),
    );
  }
 
  Widget _buildField({
    required TextEditingController controller,
    required String hint,
    required IconData icon,
    TextInputType? keyboardType,
  }) {
    return TextField(
      controller: controller,
      keyboardType: keyboardType,
      decoration: InputDecoration(
        hintText: hint,
        prefixIcon: Icon(icon),
        filled: true,
        fillColor: Colors.grey.shade100,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide.none,
        ),
      ),
    );
  }
}
 
// ==========================================================
// Botón flotante cuadrado (Alerta / SOS / Acomp.)
// ==========================================================
class _FloatingActionBox extends StatelessWidget {
  final Color color;
  final IconData? icon;
  final String? title;
  final String label;
  final int? badge;
  final VoidCallback onTap;
 
  const _FloatingActionBox({
    required this.color,
    required this.label,
    required this.onTap,
    this.icon,
    this.title,
    this.badge,
  });
 
  @override
  Widget build(BuildContext context) {
    return Stack(
      clipBehavior: Clip.none,
      children: [
        InkWell(
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
        ),
        if (badge != null && badge! > 0)
          Positioned(
            top: -6,
            right: -6,
            child: Container(
              width: 20,
              height: 20,
              decoration: BoxDecoration(
                color: Colors.white,
                shape: BoxShape.circle,
                border: Border.all(color: color, width: 2),
              ),
              child: Center(
                child: Text(
                  '$badge',
                  style: TextStyle(
                    fontSize: 10,
                    fontWeight: FontWeight.bold,
                    color: color,
                  ),
                ),
              ),
            ),
          ),
      ],
    );
  }
}
 
// ==========================================================
// Mapa oscuro de ejemplo: cuadrícula + ruta + posición actual
// ==========================================================
class _MapPlaceholderPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    canvas.drawRect(
      Offset.zero & size,
      Paint()..color = const Color(0xFF151A30),
    );
 
    final thin = Paint()
      ..color = const Color(0xFF1F2540)
      ..strokeWidth = 3;
    final thick = Paint()
      ..color = const Color(0xFF232A47)
      ..strokeWidth = 5;
 
    for (final x in [0.2, 0.5, 0.85]) {
      canvas.drawLine(Offset(size.width * x, 0),
          Offset(size.width * x, size.height), thick);
    }
    for (final y in [0.25, 0.55, 0.75]) {
      canvas.drawLine(Offset(0, size.height * y),
          Offset(size.width, size.height * y), thin);
    }
 
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
 
    canvas.drawCircle(
        start, 10, Paint()..color = const Color(0xFF3B82F6).withOpacity(0.3));
    canvas.drawCircle(start, 6, Paint()..color = Colors.white);
    canvas.drawCircle(start, 4, Paint()..color = const Color(0xFF3B82F6));
  }
 
  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}
 