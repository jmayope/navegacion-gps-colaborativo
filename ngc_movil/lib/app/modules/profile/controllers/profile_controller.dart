import 'package:flutter/cupertino.dart';
import 'package:get/get.dart';
import 'package:ngc_movil/app/data/constants.dart';
import 'package:ngc_movil/app/data/preference.service.dart';
import 'package:ngc_movil/app/data/supabase.service.dart';
import 'package:ngc_movil/app/routes/app_pages.dart';
import 'package:ngc_movil/app/theme/app_colors.dart';
import 'package:ngc_movil/models/index.dart';
import 'package:quiver/core.dart';

class ProfileController extends GetxController {
  //TODO: Implement ProfileController

  final notifications = false.obs;
  final shareLocation = false.obs;

  final userLoged = User().obs;
  final loading = true.obs;

  final supabase = Get.put(SupabaseService());
  final userContacts = <UserContact>[].obs;
  final addingContact = false.obs;
  final adding = false.obs;
  final contactFullName = TextEditingController();
  final contactPhone = TextEditingController();
  final relationships = <TableType>[].obs;
  
  final contactRelationship = TableType().obs;


  @override
  void onInit() {
    super.onInit();
    getInitialData();
  }

  Future<void> getInitialData() async {
    userLoged.value = await PreferenceService.getSession();
    final resultUserContacts = await supabase.select(USER_CONTACTS, filters: {"user_id": userLoged.value.id});
    userContacts.value = (resultUserContacts as Iterable).map((uc) => UserContact.fromJson(uc)).toList();
    relationships.value = [
      TableType(code: "0", name: "--SELECCIONE--"),
      TableType(code: "MADRE", name: "Madre"),
      TableType(code: "PADRE", name: "Padre"),
      TableType(code: "HERMANO", name: "Hermano"),
      TableType(code: "OTRO", name: "Otro"),
    ];
    contactRelationship.value = relationships.first;
    print(userContacts.length);
    notifications.value = userLoged.value.notifications ?? false;
    shareLocation.value = userLoged.value.shareLocation ?? false;
    loading.value = false;
  }

  Future<void> logout() async {
    PreferenceService.clearAll();
    Get.offAllNamed(Routes.LOGIN);
  }

  Future<void> addContact() async {
    if (adding.value) return;
    adding.value = true;
    print("Listo");
    if (contactRelationship.value.code == '0') {
      Get.snackbar("Validación", "Necesitas seleccionar el parentesco del Contacto");
      adding.value = false;
      return;
    }

    if (contactFullName.value.text.isEmpty || contactPhone.value.text.isEmpty) {
      Get.snackbar("Validación", "Necesitas Ingresar datos del Contacto");
      adding.value = false;
      return;
    }

    final newUserContact = {
      "user_id": userLoged.value.id,
      "full_name": contactFullName.value.text,
      "phone": contactPhone.value.text,
      "relationship": contactRelationship.value.code,
      "status": true
    };

    final resultNewUserContact = await supabase.insert(USER_CONTACTS, newUserContact);
    List<UserContact> userContactCreateds = (resultNewUserContact as Iterable).map((uc) => UserContact.fromJson(uc)).toList();
    adding.value = false;
    if (userContactCreateds.isEmpty) {
      Get.snackbar("Error", "Hubo un error al momento de crear el contacto");
      return;
    }

    Get.snackbar("Éxito", "Contacto de Emegencia Agregado!");
    userContacts.addAll(userContactCreateds);
    addingContact.value = false;
  }

  Future<void> updateUser() async {
    final userData = {
      "notifications": notifications.value,
      "share_location": shareLocation.value
    };

    final resultUserUpdated = await supabase.update(USERS, userLoged.value.id, userData);
    List<User> userUpdateds = (resultUserUpdated as Iterable).map((u) => User.fromJson(u)).toList();
    if (userUpdateds.isEmpty) {
      Get.snackbar("Error", "Hubo un error al momento de actualizar el usuario");
      return;
    }
    userLoged.value = userLoged.value.copyWith(notifications: Optional.of(notifications.value), shareLocation: Optional.of(shareLocation.value));
    bool userSaved = await PreferenceService.setSession(userLoged.value);
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {
    super.onClose();
  }

  
}
