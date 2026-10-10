import 'package:flutter/animation.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class RouteData {
  final String name;
  final String info;
  final String traffic;
  final Color color;
 
  const RouteData(this.name, this.info, this.traffic, this.color);
}

class RouteController extends GetxController {
  //TODO: Implement RouteController

  final routes = <RouteData>[].obs;
  final selectedRoute = 0.obs;

  final origin = TextEditingController();
  final destiny = TextEditingController();

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

  
}
