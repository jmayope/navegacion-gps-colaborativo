import 'package:flutter_map/flutter_map.dart';
import 'package:get/get.dart';
import 'package:latlong2/latlong.dart';
import 'package:location/location.dart';


class SearchPlaceController extends GetxController {
  final mapController = MapController().obs;

  final location = Location();
  final userLocation = LatLng(0, 0).obs;
  final isLoading = true.obs;
  @override
  void onInit() {
    super.onInit();
    initializeLocation();
  }

  Future<void> initializeLocation() async {
    if (!await checkRequestPermissions()) return;
    getInitialData();
  }

  Future<bool> checkRequestPermissions() async {
    bool serviceEnabled = await location.serviceEnabled();
    if (!serviceEnabled) {
      serviceEnabled = await location.requestService();
      if (!serviceEnabled) {
        return false;
      }
    }

    PermissionStatus permissionGranted = await location.hasPermission();
    if (permissionGranted == PermissionStatus.denied) {
      permissionGranted = await location.requestPermission();
      if (permissionGranted != PermissionStatus.granted) return false;
    }
    return true;
  }

  Future<void> getInitialData() async {
    await location.getLocation().then((location) {
      userLocation.value = LatLng(location.latitude, location.longitude);
    });
    isLoading.value = false;
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
