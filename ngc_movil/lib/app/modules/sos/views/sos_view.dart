import 'package:flutter/material.dart';

import 'package:get/get.dart';

import '../controllers/sos_controller.dart';

class SosView extends GetView<SosController> {
  const SosView({super.key});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('SosView'),
        centerTitle: true,
      ),
      body: const Center(
        child: Text(
          'SosView is working',
          style: TextStyle(fontSize: 20),
        ),
      ),
    );
  }
}
