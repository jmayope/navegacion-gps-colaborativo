import 'package:get/get.dart';

import '../controllers/sos_controller.dart';

class SosBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut<SosController>(
      () => SosController(),
    );
  }
}
