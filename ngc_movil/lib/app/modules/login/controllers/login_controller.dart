import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:ngc_movil/app/data/constants.dart';
import 'package:ngc_movil/app/data/preference.service.dart';
import 'package:ngc_movil/app/data/supabase.service.dart';
import 'package:ngc_movil/app/routes/app_pages.dart';
import 'package:ngc_movil/models/index.dart';

class LoginController extends GetxController {
  
  final email = TextEditingController();
  final password = TextEditingController();
  final showPassword = false.obs;
  final logging = false.obs;
  final loading = true.obs;
  final supabase = Get.put(SupabaseService());
  
  final userLoged = User().obs;

  @override
  void onInit() {
    super.onInit();
    getInitialData();
  }

  Future<void> getInitialData() async {
    userLoged.value = await PreferenceService.getSession();
    if (userLoged.value.id != null) {
      Get.offAllNamed(Routes.HOME);
      return;
    }
    loading.value = false;
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {
    super.onClose();
  }


  Future<void> login() async {
    print("Iniciando sesión");
    if (logging.value) return;
    logging.value = true;

    if (email.value.text.isEmpty || password.value.text.isEmpty) {
      Get.snackbar('Validación', 'Ingresa el Correo Electrónico y la Contraseña.');
      logging.value = false;
      return;
    }

    final resultLogin = await supabase.select(USERS, filters: {"email": email.value.text, "password": password.value.text});
    print(resultLogin);

    List<User> usersFounds = (resultLogin as Iterable).map((u) => User.fromJson(u)).toList();
    logging.value = false;
    if (usersFounds.isEmpty) {
      Get.snackbar('Error', 'Credenciales incorrectas');
      return;
    }
    User userLoged = usersFounds.first;
    bool userSaved = await PreferenceService.setSession(userLoged);
    Get.snackbar('Éxito', 'Bienvenido ${userLoged.fullName}');
    Get.offAllNamed(Routes.HOME);
  }
  
  Future<void> register() async {
    Get.offAllNamed(Routes.REGISTER);
  }

}
