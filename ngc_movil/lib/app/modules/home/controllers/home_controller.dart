import 'package:get/get.dart';
import 'package:ngc_movil/app/data/constants.dart';
import 'package:ngc_movil/app/data/preference.service.dart';
import 'package:ngc_movil/app/data/supabase.service.dart';
import 'package:ngc_movil/app/routes/app_pages.dart';
import 'package:ngc_movil/models/index.dart';
import 'package:quiver/core.dart';

class HomeController extends GetxController {
  
  final userLoged = User().obs;
  final loading = true.obs;
  final routeMaps = <RouteMap>[].obs;
  final routeMapActive = RouteMap().obs;
  final existsRouteMap = false.obs;
  final supabase = Get.put(SupabaseService());
  final distanceTotal = num.parse('0').obs;
  final incidents = <Incident>[].obs;

  @override
  void onInit() {
    super.onInit();
    getInitialData();
  }

  Future<void> getInitialData() async {
    userLoged.value = await PreferenceService.getSession();
    final resultRoutes = await supabase.select(ROUTES, filters: {"user_id": userLoged.value.id});
    print(resultRoutes);
    routeMaps.value = (resultRoutes as Iterable).map((rm) => RouteMap.fromJson(rm)).toList();
    for (var i = 0; i < routeMaps.length; i++) {
      routeMaps[i] = routeMaps[i].copyWith(inProgress: Optional.of(routeMaps[i].status! == 'in_progress'));
    }
    routeMaps.sort((a, b) {
      final aInProgress = a.status == 'in_progress';
      final bInProgress = b.status == 'in_progress';

      if (aInProgress && !bInProgress) return -1;
      if (!aInProgress && bInProgress) return 1;
      return 0;
    });
    routeMapActive.value = routeMaps.singleWhere((r) => r.status == ROUTE_STATUS_IN_PROGRESS, orElse: () => RouteMap());
    existsRouteMap.value = routeMapActive.value.id != null;
    distanceTotal.value = routeMaps.fold(0, (m, i) => m + (i.estimatedDistance ?? 0));
    final resultIncidents = await supabase.select(INCIDENTS, filters: {"user_id": userLoged.value.id});
    incidents.value = (resultIncidents as Iterable).map((i) => Incident.fromJson(i)).toList();
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

  Future<void> goToProfile() async {
    Get.offAllNamed(Routes.PROFILE);
  }

  Future<void> goToRouteActive() async {
    Get.offAllNamed(Routes.ACTIVE_TRIP);
  }

  Future<void> logout() async {
    PreferenceService.clearAll();
    Get.offAllNamed(Routes.LOGIN);
  }

}
