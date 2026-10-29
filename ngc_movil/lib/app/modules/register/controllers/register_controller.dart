import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:ngc_movil/app/data/constants.dart';
import 'package:ngc_movil/app/data/supabase.service.dart';
import 'package:ngc_movil/app/routes/app_pages.dart';
import 'package:ngc_movil/models/index.dart';

class RegisterController extends GetxController {
  //TODO: Implement RegisterController

  final fullName = TextEditingController();
  final email = TextEditingController();
  final phone = TextEditingController();
  final documentTypes = <TableType>[].obs;
  final documentTypeSelected = TableType().obs;
  final documentNumber = TextEditingController();
  final password = TextEditingController();
  final confirmPassword = TextEditingController();
  final showPassword = false.obs;
  final acceptTerms = false.obs;

  final registering = false.obs;

  final supabase = Get.put(SupabaseService());

  @override
  void onInit() {
    super.onInit();
    getInitialData();
  }

  Future<void> getInitialData() async {
    documentTypeSelected.value = TableType(code: "DNI");
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {
    super.onClose();
  }

  Future<void> register() async {
    if (registering.value) return;
    registering.value = true;

    final newUser = {
      "email": email.value.text,
      "phone": phone.value.text,
      "full_name": fullName.value.text,
      "document_type": documentTypeSelected.value.code,
      "document_number": documentNumber.value.text,
      "is_active": true,
      "is_verified": true,
      "created_at": DateTime.now().toIso8601String(),
      "password": password.value.text,
      "is_admin": false
    };

    final resultUser = await supabase.insert(USERS, newUser);
    List<User> userCreateds = (resultUser as Iterable).map((u) => User.fromJson(u)).toList();
    registering.value = false;
    if (userCreateds.isEmpty) {
      Get.snackbar("Error", "No se llegó a crear el usuario. Revisar");
      return;
    }

    Get.snackbar("Éxito", "Se creó el usuario. Ingrese sesión.");
    Get.offAllNamed(Routes.LOGIN);
  }
  
}
