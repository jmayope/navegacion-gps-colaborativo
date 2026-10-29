import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:ngc_movil/app/routes/app_pages.dart';
import 'package:ngc_movil/app/theme/app_colors.dart';
import 'package:ngc_movil/app/theme/app_input_field.dart';

import '../controllers/register_controller.dart';

class RegisterView extends GetView<RegisterController> {
  const RegisterView({super.key});
  @override
  Widget build(BuildContext context) {

    final container = Obx(() {
      return SingleChildScrollView(
        child: Column(
          children: [
            _buildHeader(context),
            _buildForm(context),
          ],
        ),
      );
    });

    return Scaffold(
      backgroundColor: AppColors.navyDark,
      body: container,
    );
  }
 
  // ---------------------------------------------------------
  // HEADER: imagen de fondo + gradiente navy + logo + título
  // ---------------------------------------------------------
  Widget _buildHeader(BuildContext context) {
    final topPadding = MediaQuery.of(context).padding.top;
 
    return Container(
      width: double.infinity,
      height: 230,
      decoration: const BoxDecoration(
        // Imagen de fondo de referencia (reemplazar por tu asset real)
        image: DecorationImage(
          image: AssetImage('assets/imgs/background.jpeg'),
          fit: BoxFit.cover,
        ),
      ),
      child: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [
              AppColors.navyDark,
              AppColors.navy,
              AppColors.navyLight,
            ],
          ),
        ),
        child: Stack(
          children: [
            // Botón volver
            Positioned(
              top: topPadding + 10,
              left: 16,
              child: InkWell(
                onTap: () => Get.offAllNamed(Routes.LOGIN),
                borderRadius: BorderRadius.circular(10),
                child: Container(
                  width: 36,
                  height: 36,
                  decoration: BoxDecoration(
                    color: Colors.white.withOpacity(0.15),
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: const Icon(
                    Icons.arrow_back_ios_new,
                    color: Colors.white,
                    size: 16,
                  ),
                ),
              ),
            ),
            // Logo + título
            Center(
              child: Padding(
                padding: EdgeInsets.only(top: topPadding * 0.5),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Container(
                      width: 64,
                      height: 64,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        border: Border.all(color: Colors.white, width: 2),
                      ),
                      child: const Icon(
                        Icons.shield_outlined,
                        color: Colors.white,
                        size: 32,
                      ),
                    ),
                    const SizedBox(height: 12),
                    const Text(
                      'SafeRoute',
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 26,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 2),
                    const Text(
                      'Viaja seguro, llega seguro',
                      style: TextStyle(color: Colors.white70, fontSize: 13),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
 
  // ---------------------------------------------------------
  // FORM: tarjeta blanca con esquinas superiores redondeadas
  // ---------------------------------------------------------
  Widget _buildForm(BuildContext context) {
    return Transform.translate(
      offset: const Offset(0, -24),
      child: Container(
        width: double.infinity,
        padding: const EdgeInsets.fromLTRB(24, 28, 24, 32),
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(28),
            topRight: Radius.circular(28),
          ),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Crear cuenta',
              style: TextStyle(
                fontSize: 22,
                fontWeight: FontWeight.bold,
                color: Colors.black87,
              ),
            ),
            const SizedBox(height: 4),
            Text(
              'Completa tus datos para comenzar',
              style: TextStyle(fontSize: 13, color: Colors.grey.shade600),
            ),
            const SizedBox(height: 20),
 
            AppInput(
              controller: controller.fullName,
              hint: 'Nombre completo',
              icon: Icons.person_outline,
              keyboardType: TextInputType.name,
            ),
            const SizedBox(height: 14),
 
            AppInput(
              controller: controller.email,
              hint: 'Correo electrónico',
              icon: Icons.mail_outline,
              keyboardType: TextInputType.emailAddress,
            ),
            const SizedBox(height: 14),
            AppInput(
              controller: controller.documentNumber,
              hint: 'Número de Documento',
              icon: Icons.card_membership,
              maxLength: 8,
              keyboardType: TextInputType.number,
            ),
            const SizedBox(height: 14),
 
            AppInput(
              controller: controller.phone,
              hint: 'Teléfono',
              icon: Icons.phone_outlined,
              keyboardType: TextInputType.phone,
            ),
            const SizedBox(height: 14),
 
            AppInput(
              controller: controller.password,
              hint: 'Contraseña',
              icon: Icons.lock_outline,
              obscure: !controller.showPassword.value,
              onToggleObscure: () {
                controller.showPassword.value = !controller.showPassword.value;
              },
            ),
            const SizedBox(height: 14),
 
            AppInput(
              controller: controller.confirmPassword,
              hint: 'Confirmar contraseña',
              icon: Icons.lock_outline,
              obscure: !controller.showPassword.value,
              onToggleObscure: () {
                controller.showPassword.value = !controller.showPassword.value;
              },
            ),
            const SizedBox(height: 12),
 
            // Términos y condiciones
            Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                SizedBox(
                  width: 24,
                  height: 24,
                  child: Checkbox(
                    value: controller.acceptTerms.value,
                    activeColor: AppColors.navy,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(4),
                    ),
                    onChanged: (value) {
                      controller.acceptTerms.value = value!;
                    },
                  ),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: RichText(
                    text: TextSpan(
                      style: const TextStyle(
                        color: Colors.black87,
                        fontSize: 12.5,
                      ),
                      children: [
                        const TextSpan(text: 'Acepto los '),
                        TextSpan(
                          text: 'Términos y condiciones',
                          style: const TextStyle(
                            color: AppColors.navy,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 22),
 
            // Botón crear cuenta
            SizedBox(
              width: double.infinity,
              height: 52,
              child: ElevatedButton(
                onPressed: controller.registering.value ? null : controller.acceptTerms.value
                    ? () {
                        controller.register();
                      }
                    : null,
                style: ElevatedButton.styleFrom(
                  backgroundColor: AppColors.navy,
                  disabledBackgroundColor: AppColors.navy.withOpacity(0.4),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                ),
                child: controller.registering.value ? CircularProgressIndicator() : Text(
                  'CREAR CUENTA',
                  style: TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                    letterSpacing: 0.5,
                  ),
                ),
              ),
            ),
            const SizedBox(height: 18),
 
            // Ya tienes cuenta
            Center(
              child: RichText(
                text: TextSpan(
                  style: const TextStyle(color: Colors.black87, fontSize: 14),
                  children: [
                    const TextSpan(text: '¿Ya tienes cuenta? '),
                    TextSpan(
                      text: 'Inicia sesión',
                      style: const TextStyle(
                        color: AppColors.navy,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
 
  // ---------------------------------------------------------
  // Campo de texto reutilizable (mismo estilo del login)
  // ---------------------------------------------------------
  
}