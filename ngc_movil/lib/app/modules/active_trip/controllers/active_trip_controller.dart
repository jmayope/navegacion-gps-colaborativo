import 'package:flutter/material.dart';
import 'package:get/get.dart';

class Companion {
  final String name;
  final String phone;
  const Companion(this.name, this.phone);
 
  String get initials {
    final parts = name.trim().split(RegExp(r'\s+'));
    if (parts.length == 1) return parts.first.substring(0, 1).toUpperCase();
    return (parts.first[0] + parts.last[0]).toUpperCase();
  }
}


class ActiveTripController extends GetxController {
  //TODO: Implement ActiveTripController

  final companions = <Companion>[].obs;
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

  // void _openCompanionsSheet() {
  //   showModalBottomSheet(
  //     context: Get.context!,
  //     isScrollControlled: true,
  //     backgroundColor: Colors.transparent,
  //     builder: (_) => _CompanionsSheet(
  //       companions: controller.companions,
  //       onChanged: () => {},
  //     ),
  //   );
  // }
}
