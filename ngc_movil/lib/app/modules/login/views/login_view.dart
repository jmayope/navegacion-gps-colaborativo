import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:ngc_movil/app/theme/app_colors.dart';
import '../controllers/login_controller.dart';

class LoginView extends GetView<LoginController> {
  const LoginView({super.key});

  @override
  Widget build(BuildContext context) {
    final container = Obx(()  {
      return controller.loading.value ? 
        Center(
          child: Column(
            children: [
              CircularProgressIndicator(),
              Text("Cargando Información Inicial")
            ],
          ),
        )
      : LayoutBuilder(
        builder: (context, constraints) {
          return SingleChildScrollView(
            child: ConstrainedBox(
              constraints: BoxConstraints(minHeight: constraints.maxHeight),
              child: IntrinsicHeight(
                child: Column(
                  children: [
                    _buildBrand(),
                    const Spacer(),
                    _buildLoginCard(context),
                  ],
                ),
              ),
            ),
          );
        },
      );
    });

    return Scaffold(
      backgroundColor: Colors.white,
      body: Stack(
        children: [
          // Fondo: ocupa toda la pantalla
          Positioned.fill(child: _buildHeader()),

          // Contenido: logo arriba, formulario abajo
          SafeArea(
            child: container,
          ),
        ],
      ),
    );
  }

  // ---------------------------------------------------------
  // FONDO: imagen
  // ---------------------------------------------------------
  Widget _buildHeader() {
    return Container(
      decoration: const BoxDecoration(
        image: DecorationImage(
          image: AssetImage('assets/imgs/background.jpeg'),
          fit: BoxFit.cover,
        ),
      ),
    );
  }

  // ---------------------------------------------------------
  // MARCA: logo + título + subtítulo
  // ---------------------------------------------------------
  Widget _buildBrand() {
    return Padding(
      padding: const EdgeInsets.only(top: 24),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Container(
            width: 72,
            height: 72,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              border: Border.all(color: Colors.white, width: 2),
            ),
            child: const Icon(
              Icons.shield_outlined,
              color: Colors.white,
              size: 36,
            ),
          ),
          const SizedBox(height: 12),
          const Text(
            'SafeRoute',
            style: TextStyle(
              color: Colors.white,
              fontSize: 28,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 4),
          const Text(
            'Viaja seguro, llega seguro',
            style: TextStyle(
              color: Colors.white,
              fontSize: 14,
            ),
          ),
        ],
      ),
    );
  }

  // ---------------------------------------------------------
  // FORM: tarjeta translúcida con ancho completo (con márgenes)
  // ---------------------------------------------------------
  Widget _buildLoginCard(BuildContext context) {
    return Container(
      width: double.infinity,
      margin: const EdgeInsets.fromLTRB(20, 0, 20, 24),
      padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 20),
      decoration: BoxDecoration(
        color: Colors.white.withValues(alpha: 0.5),
        borderRadius: const BorderRadius.all(Radius.circular(28)),
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Center(
            child: Text(
              'Iniciar sesión',
              style: TextStyle(
                fontSize: 22,
                fontWeight: FontWeight.bold,
                color: AppColors.navy,
              ),
            ),
          ),
          const SizedBox(height: 15),

          // Campo correo
          TextField(
            controller: controller.email,
            keyboardType: TextInputType.emailAddress,
            decoration: InputDecoration(
              hintText: 'Correo electrónico',
              prefixIcon: const Icon(Icons.mail_outline),
              filled: true,
              fillColor: Colors.grey.shade100,
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: BorderSide.none,
              ),
            ),
          ),
          const SizedBox(height: 14),

          // Campo contraseña
          TextField(
            controller: controller.password,
            obscureText: !controller.showPassword.value,
            decoration: InputDecoration(
              hintText: 'Contraseña',
              prefixIcon: const Icon(Icons.lock_outline),
              suffixIcon: IconButton(
                icon: Icon(
                  controller.showPassword.value
                      ? Icons.visibility_outlined
                      : Icons.visibility_off_outlined,
                ),
                onPressed: () {
                  controller.showPassword.value =
                      !controller.showPassword.value;
                },
              ),
              filled: true,
              fillColor: Colors.grey.shade100,
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: BorderSide.none,
              ),
            ),
          ),
          const SizedBox(height: 10),

          // Olvidaste tu contraseña
          Align(
            alignment: Alignment.centerRight,
            child: TextButton(
              onPressed: () {
                // TODO: acción olvidaste tu contraseña
              },
              child: const Text(
                '¿Olvidaste tu contraseña?',
                style: TextStyle(color: AppColors.navy),
              ),
            ),
          ),
          const SizedBox(height: 10),

          // Botón iniciar sesión
          SizedBox(
            width: double.infinity,
            height: 52,
            child: ElevatedButton(
              onPressed: controller.logging.value
                  ? null
                  : () {
                      controller.login();
                    },
              style: ElevatedButton.styleFrom(
                backgroundColor: AppColors.navy,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
              ),
              child: controller.logging.value
                  ? const SizedBox(
                      width: 24,
                      height: 24,
                      child: CircularProgressIndicator(
                        color: Colors.white,
                        strokeWidth: 2.5,
                      ),
                    )
                  : const Text(
                      'INICIAR SESIÓN',
                      style: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                        letterSpacing: 0.5,
                      ),
                    ),
            ),
          ),
          const SizedBox(height: 18),

          // Registrarse
          Center(
            child: InkWell(
              onTap: () => controller.register(),
              child: RichText(
                text: const TextSpan(
                  style: TextStyle(color: Colors.black87, fontSize: 14),
                  children: [
                    TextSpan(text: '¿No tienes cuenta? '),
                    TextSpan(
                      text: 'Regístrate',
                      style: TextStyle(
                        color: AppColors.navy,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}