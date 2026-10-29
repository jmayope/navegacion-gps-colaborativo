import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:ngc_movil/app/routes/app_pages.dart';
import 'package:ngc_movil/app/theme/app_colors.dart';
import 'package:ngc_movil/app/theme/app_input_field.dart';

import '../controllers/route_controller.dart';

class RouteView extends GetView<RouteController> {
  const RouteView({super.key});
  @override
  Widget build(BuildContext context) {
    final container = Obx(() {
      return controller.loading.value ? 
        Center(
          child: Column(
            children: [
              CircularProgressIndicator(),
              Text("Cargando Información Inicial")
            ],
          ),
        )
      :
       Expanded(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildOriginDestinationCard(),
              const SizedBox(height: 22),
              const Text(
                'Rutas sugeridas',
                style: TextStyle(
                  fontSize: 14,
                  fontWeight: FontWeight.w600,
                  color: Colors.black87,
                ),
              ),
              const SizedBox(height: 12),
              controller.routes.isEmpty ? 
              Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Icon(Icons.close),
                    Text("No tienes rutas para sugerir")
                  ],
                ),
              )
              :
              Column(
                children: [
                  ...controller.routes.map((r) {
                    int index = controller.routes.indexOf(r);
                    return Padding(
                      padding: EdgeInsetsGeometry.only(bottom: 12),
                      child: _buildRouteCard(index, r),
                    );
                  })
                ],
              )
            ],
          ),
        ),
      );
    }); 

    return Scaffold(
      appBar: AppBar(
        backgroundColor: AppColors.navy,
        leading: IconButton(
          onPressed: () {
            Get.offAllNamed(Routes.HOME);
          },
          icon: Icon(Icons.home, color: Colors.white,)
        ),
        title: Text(
          "Seleccionar Ruta",
          style: TextStyle(
            color: Colors.white
          ),
        ),
        actions: [
          ElevatedButton(
            onPressed: () {}, 
            child: Icon(Icons.tune)
          )
        ],
      ),
      backgroundColor: const Color(0xFFF3F5F9),
      body: Column(
        children: [
          // _buildHeader(context),
          container
        ],
      ),
    );
  }
 
  // ---------------------------------------------------------
  // HEADER
  // ---------------------------------------------------------
  Widget _buildHeader(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.fromLTRB(
        16,
        MediaQuery.of(context).padding.top + 12,
        16,
        14,
      ),
      color: AppColors.navy,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          _headerButton(Icons.arrow_back_ios_new, () {
            Navigator.maybePop(context);
          }),
          const Text(
            'Seleccionar ruta',
            style: TextStyle(
              color: Colors.white,
              fontSize: 17,
              fontWeight: FontWeight.bold,
            ),
          ),
          _headerButton(Icons.tune, () {
            // TODO: acción derecha (filtros, etc.)
          }),
        ],
      ),
    );
  }
 
  Widget _headerButton(IconData icon, VoidCallback onTap) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(10),
      child: Container(
        width: 32,
        height: 32,
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(10),
        ),
        child: Icon(icon, size: 16, color: AppColors.navy),
      ),
    );
  }
 
  // ---------------------------------------------------------
  // ORIGEN / DESTINO
  // ---------------------------------------------------------
  Widget _buildOriginDestinationCard() {
    return Container(
      padding: const EdgeInsets.fromLTRB(16, 14, 12, 14),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.04),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Expanded(
                child: Column(
                  children: [
                    AppInput(
                      controller: controller.origin, 
                      hint: '¿De donde va?', 
                      icon: Icons.pin_drop_outlined
                    ),
                    SizedBox(height: 10,),
                    AppInput(
                      controller: controller.destiny, 
                      hint: '¿Hacia donde va?', 
                      icon: Icons.edit_location_alt_sharp
                    ),
                  ],
                ),
              ),
              Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  IconButton(
                    onPressed: () {
                      String temporal = controller.destiny.text;
                      controller.destiny.value = TextEditingValue(text: controller.origin.text);
                      controller.origin.value = TextEditingValue(text: temporal);
                    },
                    icon: const Icon(Icons.swap_vert, color: Colors.grey),
                  ),
                ],
              )
            ],
          ),
          SizedBox(height: 10,),
          ListTile(
            title: Text("¿Es ruta favorita?"),
            trailing: IconButton(
              onPressed: () {
                controller.isFavorite.value = !controller.isFavorite.value;
              }, 
              icon: Icon(
                controller.isFavorite.value ? Icons.check_box_outlined : Icons.check_box_outline_blank,
                color: controller.isFavorite.value ? Colors.green.shade800 : Colors.grey,
              )
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: AppColors.navy
                ),
                onPressed: controller.saving.value ? null : () {
                  controller.save();
                }, 
                child: controller.saving.value ? CircularProgressIndicator() : Text(
                  "Guardar Ruta",
                  style: TextStyle(
                    color: Colors.white
                  ),
                )
              )
            ],
          )
        ],
      ),
    );
  }
 
  // ---------------------------------------------------------
  // TARJETA DE RUTA
  // ---------------------------------------------------------
  Widget _buildRouteCard(int index, RouteData route) {
    final selected = controller.selectedRoute.value == index;
 
    return InkWell(
      onTap: () {
        controller.selectedRoute.value = index;
        controller.selectSuggestRoute(index);
      },
      borderRadius: BorderRadius.circular(16),
      child: Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(16),
          border: Border.all(
            color: selected ? AppColors.navy : Colors.transparent,
            width: 1.5,
          ),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.04),
              blurRadius: 8,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Row(
          children: [
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    route.name,
                    style: const TextStyle(
                      fontSize: 15,
                      fontWeight: FontWeight.bold,
                      color: Colors.black87,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    route.info,
                    style: TextStyle(fontSize: 12, color: Colors.grey.shade600),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    route.traffic,
                    style: TextStyle(
                      fontSize: 12,
                      fontWeight: FontWeight.w600,
                      color: route.color,
                    ),
                  ),
                ],
              ),
            ),
            _RouteThumbnail(color: route.color),
          ],
        ),
      ),
    );
  }
}
 
// ==========================================================
// Miniatura de ruta (dibujada con CustomPainter)
// Si prefieres una imagen: reemplaza por Image.asset('assets/images/...')
// ==========================================================
class _RouteThumbnail extends StatelessWidget {
  final Color color;
  const _RouteThumbnail({required this.color});
 
  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(8),
      child: SizedBox(
        width: 72,
        height: 52,
        child: CustomPaint(painter: _RoutePainter(color)),
      ),
    );
  }
}
 
class _RoutePainter extends CustomPainter {
  final Color color;
  _RoutePainter(this.color);
 
  @override
  void paint(Canvas canvas, Size size) {
    // Fondo
    canvas.drawRect(
      Offset.zero & size,
      Paint()..color = const Color(0xFFEEF1F6),
    );
 
    // Cuadrícula
    final grid = Paint()
      ..color = const Color(0xFFDDE2EA)
      ..strokeWidth = 1;
    canvas.drawLine(Offset(size.width * 0.35, 0),
        Offset(size.width * 0.35, size.height), grid);
    canvas.drawLine(Offset(size.width * 0.75, 0),
        Offset(size.width * 0.75, size.height), grid);
    canvas.drawLine(Offset(0, size.height * 0.4),
        Offset(size.width, size.height * 0.4), grid);
    canvas.drawLine(Offset(0, size.height * 0.75),
        Offset(size.width, size.height * 0.75), grid);
 
    // Curva de ruta
    final start = Offset(size.width * 0.1, size.height * 0.85);
    final end = Offset(size.width * 0.92, size.height * 0.15);
    final path = Path()
      ..moveTo(start.dx, start.dy)
      ..cubicTo(
        size.width * 0.15, size.height * 0.35,
        size.width * 0.4, size.height * 0.15,
        end.dx, end.dy,
      );
    canvas.drawPath(
      path,
      Paint()
        ..color = color
        ..style = PaintingStyle.stroke
        ..strokeWidth = 2.5
        ..strokeCap = StrokeCap.round,
    );
 
    // Puntos inicio / fin
    canvas.drawCircle(start, 3.5, Paint()..color = const Color(0xFF3B82F6));
    canvas.drawCircle(end, 3.5, Paint()..color = AppColors.safe);
  }
 
  @override
  bool shouldRepaint(covariant _RoutePainter old) => old.color != color;
}