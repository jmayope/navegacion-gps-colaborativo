import 'package:flutter/material.dart';

import 'package:get/get.dart';

import '../controllers/accompaniment_controller.dart';

class AccompanimentView extends GetView<AccompanimentController> {
  const AccompanimentView({super.key});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('AccompanimentView'),
        centerTitle: true,
      ),
      body: const Center(
        child: Text(
          'AccompanimentView is working',
          style: TextStyle(fontSize: 20),
        ),
      ),
    );
  }
}
