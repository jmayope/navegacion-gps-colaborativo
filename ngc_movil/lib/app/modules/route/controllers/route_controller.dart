import 'dart:math';

import 'package:flutter/animation.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:ngc_movil/app/data/constants.dart';
import 'package:ngc_movil/app/data/preference.service.dart';
import 'package:ngc_movil/app/data/supabase.service.dart';
import 'package:ngc_movil/app/routes/app_pages.dart';
import 'package:ngc_movil/models/index.dart';

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
  final routeMaps = <RouteMap>[].obs;
  final newRoute = RouteMap().obs;
  final selectedRoute = 999.obs;

  final origin = TextEditingController();
  final destiny = TextEditingController();

  final userLoged = User().obs;
  final loading = true.obs;
  final saving = false.obs;
  
  final isFavorite = false.obs;

  final originLat = Random().nextDouble().obs;
  final originLng = Random().nextDouble().obs;

  final destinationLat = Random().nextDouble().obs;
  final destinationLng = Random().nextDouble().obs;
  final estimatedDistance = Random().nextDouble().obs;
  final estimatedDuration = Random().nextInt(10).obs;

  final supabase = Get.put(SupabaseService());

  @override
  void onInit() {
    super.onInit();
    getInitialData();
  }

  Future<void> getInitialData() async {
    userLoged.value = await PreferenceService.getSession();
    final resultRouteMaps = await supabase.select(ROUTES, filters: {"user_id": userLoged.value.id});
    routeMaps.value = (resultRouteMaps as Iterable).map((rm) => RouteMap.fromJson(rm)).toList();
    for (var i = 0; i < routeMaps.length; i++) {
      RouteMap currentRoute = routeMaps[i];
      routes.add(RouteData('${currentRoute.originName} a ${currentRoute.destinationName}', '${currentRoute.estimatedDistance} km - ${currentRoute.estimatedDuration} h', currentRoute.isFavorite ?? false ? 'Si' : 'No', getRandomColor()));
    }
    
    loading.value = false;
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {
    super.onClose();
  }

  Future<void> save() async {
    if (saving.value) return;
    saving.value = true;

    if (origin.value.text.isEmpty || destiny.value.text.isEmpty) {
      Get.snackbar("Validación", "Necesita ingresar el Origen y el Destino.");
      saving.value = false;
      return;
    }

    // COMPLETAR LA RUTA ACTUAL SI EXISTIERA

    final routeInProgress = await supabase.select(ROUTES, filters: {"status": 'in_progress', "user_id": userLoged.value.id});
    
    List<RouteMap> routesInProgress = (routeInProgress as Iterable).map((r) => RouteMap.fromJson(r)).toList();
    if (routesInProgress.isNotEmpty) {
      for (var i = 0; i < routesInProgress.length; i++) {
        RouteMap currentRoute = routesInProgress[i];
        final routeUpdated = await supabase.update(ROUTES, currentRoute.id, {"status": "completed"});
        print(routeUpdated);
      }
    }

    final newRoute = {
      "user_id": userLoged.value.id,
      "origin_name": origin.value.text,
      "origin_lat": originLat.value,
      "origin_lng": originLng.value,
      "origin_address": origin.value.text,
      "destination_name": destiny.value.text,
      "destination_lat": destinationLat.value,
      "destination_lng": destinationLng.value,
      "destination_address": destiny.value.text,
      "estimated_distance": estimatedDistance.value,
      "estimated_duration": estimatedDuration.value,
      "is_favorite": isFavorite.value,
      "status": 'in_progress',
      "started_at": DateTime.now().toIso8601String()
    };

    final resultRoute = await supabase.insert(ROUTES, newRoute);
    saving.value = false;
    if (resultRoute.isEmpty) {
      Get.snackbar("Error", "Hubo un error al momento de crear la ruta");
      return;
    }
    Get.snackbar("Éxito", "Se creo la ruta correctamente");
    Get.offAllNamed(Routes.HOME);
  }

  Future<void> selectSuggestRoute(int index) async {
    RouteMap routeSelected = routeMaps[index];
    originLat.value = routeSelected.originLat!;
    originLng.value = routeSelected.originLng!;
    destinationLat.value = routeSelected.destinationLat!;
    destinationLng.value = routeSelected.destinationLng!;
    estimatedDistance.value = routeSelected.estimatedDistance!;
    estimatedDuration.value = routeSelected.estimatedDuration!;

    origin.value = TextEditingValue(text: routeSelected.originName!);
    destiny.value = TextEditingValue(text: routeSelected.destinationName!);
    
  }


}
