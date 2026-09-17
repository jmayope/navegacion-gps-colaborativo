import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:ngc_movil/app/routes/app_pages.dart';

class LoginController extends GetxController {
  
  final email = TextEditingController();
  final password = TextEditingController();
  final showPassword = false.obs;
  final logging = false.obs;
  @override
  void onInit() {
    super.onInit();
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
    Future.delayed(Duration(seconds: 3), () {
      Get.snackbar('Éxito', 'Bienvenido @Usuario');
      logging.value = false;
      Get.offAllNamed(Routes.HOME);
    });
  }
  
  Future<void> register() async {
    
  }

}
