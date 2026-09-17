import 'package:flutter/material.dart';

import 'package:get/get.dart';

import '../controllers/check_controller.dart';

class CheckView extends GetView<CheckController> {
  const CheckView({super.key});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('CheckView'),
        centerTitle: true,
      ),
      body: const Center(
        child: Text(
          'CheckView is working',
          style: TextStyle(fontSize: 20),
        ),
      ),
    );
  }
}
