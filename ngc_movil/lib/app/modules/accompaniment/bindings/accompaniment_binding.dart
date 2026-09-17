import 'package:get/get.dart';

import '../controllers/accompaniment_controller.dart';

class AccompanimentBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut<AccompanimentController>(
      () => AccompanimentController(),
    );
  }
}
