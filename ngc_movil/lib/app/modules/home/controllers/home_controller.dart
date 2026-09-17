import 'package:get/get.dart';
import 'package:ngc_movil/app/routes/app_pages.dart';

class HomeController extends GetxController {
  
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

  Future<void> goToProfile() async {
    Get.offAllNamed(Routes.PROFILE);
  }

  Future<void> goToRouteActive() async {
    Get.offAllNamed(Routes.ROUTE);
  }

  Future<void> logout() async {
    Get.offAllNamed(Routes.LOGIN);
  }

}
