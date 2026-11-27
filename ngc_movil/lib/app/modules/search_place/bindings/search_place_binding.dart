import 'package:get/get.dart';

import '../controllers/search_place_controller.dart';

class SearchPlaceBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut<SearchPlaceController>(
      () => SearchPlaceController(),
    );
  }
}
