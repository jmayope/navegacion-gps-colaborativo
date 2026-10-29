import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:ngc_movil/app/data/constants.dart';
import 'package:ngc_movil/app/routes/app_pages.dart';
import 'package:ngc_movil/app/theme/app_colors.dart';
import 'package:ngc_movil/app/theme/app_dropdown_field.dart';
import 'package:ngc_movil/app/theme/app_input_field.dart';
import 'package:ngc_movil/models/index.dart';

import '../controllers/profile_controller.dart';

class ProfileView extends GetView<ProfileController> {
  const ProfileView({super.key});
  @override
  Widget build(BuildContext context) {
    final container = Obx(() {
      return controller.loading.value ? 
        Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              CircularProgressIndicator(),
              Text(
                "Cargando Información del Usuario"
              )
            ],
          ),
        )
      :
        SingleChildScrollView(
          child: Column(
            children: [
              _buildHeader(context),
              Transform.translate(
                offset: const Offset(0, -24),
                child: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _buildPersonalInfoCard(),
                      const SizedBox(height: 20),
                      _buildSectionTitle('CONTACTOS DE EMERGENCIA',
                          actionLabel: controller.addingContact.value ? 'Cancelar' : 'Agregar', isCancel: controller.addingContact.value),
                      const SizedBox(height: 10),
                      controller.addingContact.value ? 
                        _card(
                          child: Padding(
                            padding: const EdgeInsets.all(12.0),
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.start,
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Icon(Icons.plus_one_outlined, color: AppColors.navy,),
                                    SizedBox(width: 5,),
                                    Text(
                                      "Nuevo Contacto", 
                                      style: TextStyle(
                                        color: AppColors.navy,
                                        fontWeight: FontWeight.bold,
                                        fontSize: 16
                                      ),
                                    ),

                                  ],
                                ),
                                Divider(),
                                AppInput(
                                  controller: controller.contactFullName,
                                  hint: 'Nombre completo',
                                  icon: Icons.person_outline,
                                  keyboardType: TextInputType.name,
                                ),
                                const SizedBox(height: 10),
                                AppInput(
                                  controller: controller.contactPhone,
                                  hint: 'Teléfono',
                                  icon: Icons.phone,
                                  keyboardType: TextInputType.name,
                                ),
                                const SizedBox(height: 10),
                                AppDropdown<TableType>(
                                  hint: 'Relación',
                                  icon: Icons.badge_outlined,
                                  value: controller.contactRelationship.value,
                                  items: controller.relationships.map((c) {
                                    return DropdownMenuItem(
                                      value: c,
                                      child: Text(c.name!),
                                    );
                                  }).toList(),
                                  onChanged: (value) {
                                    controller.contactRelationship.value = value!;
                                  },
                                ),
                                const SizedBox(height: 5),
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  crossAxisAlignment: CrossAxisAlignment.center,
                                  children: [
                                    ElevatedButton(
                                      style: ElevatedButton.styleFrom(
                                        backgroundColor: AppColors.navy
                                      ),
                                      onPressed: controller.adding.value ? null : () {
                                        controller.addContact();
                                      }, 
                                      child: controller.adding.value ? CircularProgressIndicator() : Text(
                                        "Agregar Contacto",
                                        style: TextStyle(
                                          color: Colors.white
                                        ),
                                      )
                                    )
                                  ],
                                )
                              ],
                            ),
                          )
                        )
                      :
                        Container()
                      ,
                      controller.userContacts.isEmpty && !controller.addingContact.value ? 
                        Center(
                          child: Column(
                            children: [
                              Icon(Icons.close, color: AppColors.danger,),
                              Text(
                                "No tienes registrado ningun contacto de emergencia",
                                style: TextStyle(
                                  color: Colors.grey
                                ),
                              )
                            ],
                          ),
                        )
                      :
                        Column(
                          mainAxisAlignment: MainAxisAlignment.start,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            ...controller.userContacts.map((uc) {
                              return Padding(
                                padding: const EdgeInsets.only(bottom: 5),
                                child: _buildContactTile(uc.fullName!, '${uc.relationship ?? '-'} · ${uc.phone}'),
                              );
                            })
                          ],
                        )
                      ,
                      // _buildContactTile('María Pérez', 'Madre · +51 987 654 321'),
                      // const SizedBox(height: 10),
                      // _buildContactTile('Carlos Pérez', 'Hermano · +51 912 345 678'),
                      const SizedBox(height: 20),
                      _buildSectionTitle('CONFIGURACIÓN'),
                      const SizedBox(height: 10),
                      _buildSettingsCard(),
                      const SizedBox(height: 24),
                      _buildLogoutButton(),
                      const SizedBox(height: 24),
                    ],
                  ),
                ),
              ),
            ],
          )
        )
      ;
    });

    return  Scaffold(
      backgroundColor: const Color(0xFFF3F5F9),
      body: container,
    );
  }
 
  // ---------------------------------------------------------
  // HEADER: título, avatar, nombre y correo
  // ---------------------------------------------------------
  Widget _buildHeader(BuildContext context) {
    final topPadding = MediaQuery.of(context).padding.top;
 
    return Container(
      width: double.infinity,
      padding: EdgeInsets.fromLTRB(20, topPadding + 12, 20, 48),
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
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              _headerButton(Icons.arrow_back_ios_new, () {
                Get.offAllNamed(Routes.HOME);
              }),
              const Text(
                'Perfil',
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 17,
                  fontWeight: FontWeight.bold,
                ),
              ),
              _headerButton(Icons.settings_outlined, () {
                // TODO: acción configuración
              }),
            ],
          ),
          const SizedBox(height: 20),
 
          // Avatar con botón editar
          Stack(
            clipBehavior: Clip.none,
            children: [
              Container(
                width: 88,
                height: 88,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  color: Colors.white.withOpacity(0.15),
                  border: Border.all(color: Colors.white, width: 2),
                ),
                child: Center(
                  child: Text(
                    controller.userLoged.value.fullName!.substring(0,2).toUpperCase(),
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 28,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
              // Positioned(
              //   right: -2,
              //   bottom: -2,
              //   child: InkWell(
              //     onTap: () {
              //       // TODO: cambiar foto de perfil
              //     },
              //     child: Container(
              //       width: 30,
              //       height: 30,
              //       decoration: BoxDecoration(
              //         color: Colors.white,
              //         shape: BoxShape.circle,
              //         border: Border.all(color: AppColors.navy, width: 2),
              //       ),
              //       child: const Icon(
              //         Icons.edit,
              //         size: 14,
              //         color: AppColors.navy,
              //       ),
              //     ),
              //   ),
              // ),
            ],
          ),
          const SizedBox(height: 12),
          Text(
            capitalizeWords(controller.userLoged.value.fullName!),
            style: TextStyle(
              color: Colors.white,
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 2),
          Text(
            controller.userLoged.value.email!, // tu_controller_perfil.correo
            style: TextStyle(color: Colors.white70, fontSize: 13),
          ),
        ],
      ),
    );
  }
 
  Widget _headerButton(IconData icon, VoidCallback onTap) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(10),
      child: Container(
        width: 36,
        height: 36,
        decoration: BoxDecoration(
          color: Colors.white.withOpacity(0.15),
          borderRadius: BorderRadius.circular(10),
        ),
        child: Icon(icon, size: 18, color: Colors.white),
      ),
    );
  }
 
  // ---------------------------------------------------------
  // INFORMACIÓN PERSONAL
  // ---------------------------------------------------------
  Widget _buildPersonalInfoCard() {
    return _card(
      child: Column(
        children: [
          _infoRow(Icons.person_outline, 'Nombre completo', capitalizeWords(controller.userLoged.value.fullName!)),
          const Divider(height: 1, color: Color(0xFFE5E7EB)),
          _infoRow(Icons.mail_outline, 'Correo electrónico',
              controller.userLoged.value.email!),
          const Divider(height: 1, color: Color(0xFFE5E7EB)),
          _infoRow(Icons.phone_outlined, 'Teléfono', controller.userLoged.value.phone!),
        ],
      ),
    );
  }
 
  Widget _infoRow(IconData icon, String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      child: Row(
        children: [
          _iconBox(icon, AppColors.navy),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  label,
                  style: TextStyle(fontSize: 11, color: Colors.grey.shade600),
                ),
                const SizedBox(height: 2),
                Text(
                  value,
                  style: const TextStyle(
                    fontSize: 14,
                    fontWeight: FontWeight.w600,
                    color: Colors.black87,
                  ),
                ),
              ],
            ),
          ),
          InkWell(
            onTap: () {
              // TODO: editar campo
            },
            child: Icon(Icons.chevron_right, color: Colors.grey.shade400),
          ),
        ],
      ),
    );
  }
 
  // ---------------------------------------------------------
  // CONTACTOS DE EMERGENCIA
  // ---------------------------------------------------------
  Widget _buildContactTile(String name, String detail) {
    return _card(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Row(
          children: [
            _iconBox(Icons.people_alt_outlined, const Color(0xFF7C3AED)),
            const SizedBox(width: 12),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    name,
                    style: const TextStyle(
                      fontSize: 14,
                      fontWeight: FontWeight.w600,
                      color: Colors.black87,
                    ),
                  ),
                  const SizedBox(height: 2),
                  Text(
                    detail,
                    style: TextStyle(fontSize: 12, color: Colors.grey.shade600),
                  ),
                ],
              ),
            ),
            IconButton(
              onPressed: () {
                // TODO: llamar / eliminar contacto
              },
              icon: const Icon(Icons.phone, color: AppColors.safe, size: 20),
            ),
          ],
        ),
      ),
    );
  }
 
  // ---------------------------------------------------------
  // CONFIGURACIÓN
  // ---------------------------------------------------------
  Widget _buildSettingsCard() {
    return _card(
      child: Column(
        children: [
          _switchRow(
            icon: Icons.notifications_none,
            color: AppColors.warn,
            title: 'Notificaciones',
            value: controller.notifications.value,
            onChanged: (v) {
              controller.notifications.value = v;
              controller.updateUser();
            },
          ),
          const Divider(height: 1, color: Color(0xFFE5E7EB)),
          _switchRow(
            icon: Icons.location_on_outlined,
            color: AppColors.safe,
            title: 'Compartir ubicación',
            value: controller.shareLocation.value,
            onChanged: (v) {
              controller.shareLocation.value = v;
              controller.updateUser();
            },
          ),
          const Divider(height: 1, color: Color(0xFFE5E7EB)),
          _arrowRow(Icons.lock_outline, AppColors.navy, 'Cambiar contraseña'),
          const Divider(height: 1, color: Color(0xFFE5E7EB)),
          _arrowRow(Icons.shield_outlined, Colors.blueGrey, 'Privacidad'),
        ],
      ),
    );
  }
 
  Widget _switchRow({
    required IconData icon,
    required Color color,
    required String title,
    required bool value,
    required ValueChanged<bool> onChanged,
  }) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
      child: Row(
        children: [
          _iconBox(icon, color),
          const SizedBox(width: 12),
          Expanded(
            child: Text(
              title,
              style: const TextStyle(
                fontSize: 14,
                fontWeight: FontWeight.w600,
                color: Colors.black87,
              ),
            ),
          ),
          Switch(
            value: value,
            activeColor: AppColors.safe,
            onChanged: onChanged,
          ),
        ],
      ),
    );
  }
 
  Widget _arrowRow(IconData icon, Color color, String title) {
    return InkWell(
      onTap: () {
        // TODO: acción para $title
      },
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
        child: Row(
          children: [
            _iconBox(icon, color),
            const SizedBox(width: 12),
            Expanded(
              child: Text(
                title,
                style: const TextStyle(
                  fontSize: 14,
                  fontWeight: FontWeight.w600,
                  color: Colors.black87,
                ),
              ),
            ),
            Icon(Icons.chevron_right, color: Colors.grey.shade400),
          ],
        ),
      ),
    );
  }
 
  // ---------------------------------------------------------
  // CERRAR SESIÓN
  // ---------------------------------------------------------
  Widget _buildLogoutButton() {
    return SizedBox(
      width: double.infinity,
      height: 52,
      child: OutlinedButton.icon(
        onPressed: () {
          controller.logout();
        },
        icon: const Icon(Icons.logout, color: AppColors.danger),
        label: const Text(
          'CERRAR SESIÓN',
          style: TextStyle(
            color: AppColors.danger,
            fontWeight: FontWeight.bold,
            letterSpacing: 0.5,
          ),
        ),
        style: OutlinedButton.styleFrom(
          side: const BorderSide(color: AppColors.danger),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
        ),
      ),
    );
  }
 
  // ---------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------
  Widget _buildSectionTitle(String title, {String? actionLabel, bool? isCancel}) {
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
              controller.contactFullName.value = TextEditingValue.empty;
              controller.contactPhone.value = TextEditingValue.empty;
              controller.contactRelationship.value = controller.relationships.first;
              controller.addingContact.value = !controller.addingContact.value;
              // TODO: acción $actionLabel
            },
            child: Text(
              actionLabel,
              style: TextStyle(
                fontSize: 13,
                fontWeight: FontWeight.w600,
                color: (isCancel ?? false) ? AppColors.danger : AppColors.navy,
              ),
            ),
          ),
      ],
    );
  }
 
  Widget _card({required Widget child}) {
    return Container(
      width: double.infinity,
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
      child: child,
    );
  }
 
  Widget _iconBox(IconData icon, Color color) {
    return Container(
      width: 36,
      height: 36,
      decoration: BoxDecoration(
        color: color.withOpacity(0.12),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Icon(icon, color: color, size: 18),
    );
  }
}
 