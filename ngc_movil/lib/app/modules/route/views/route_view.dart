import 'package:flutter/material.dart';

import 'package:get/get.dart';

import '../controllers/route_controller.dart';

class RouteView extends GetView<RouteController> {
  const RouteView({super.key});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('RouteView'),
        centerTitle: true,
      ),
      body: const Center(
        child: Text(
          'RouteView is working',
          style: TextStyle(fontSize: 20),
        ),
      ),
    );
  }
}
