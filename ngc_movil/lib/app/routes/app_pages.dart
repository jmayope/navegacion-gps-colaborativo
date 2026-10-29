import 'package:get/get.dart';

import '../modules/accompaniment/bindings/accompaniment_binding.dart';
import '../modules/accompaniment/views/accompaniment_view.dart';
import '../modules/active_trip/bindings/active_trip_binding.dart';
import '../modules/active_trip/views/active_trip_view.dart';
import '../modules/check/bindings/check_binding.dart';
import '../modules/check/views/check_view.dart';
import '../modules/history/bindings/history_binding.dart';
import '../modules/history/views/history_view.dart';
import '../modules/home/bindings/home_binding.dart';
import '../modules/home/views/home_view.dart';
import '../modules/login/bindings/login_binding.dart';
import '../modules/login/views/login_view.dart';
import '../modules/map/bindings/map_binding.dart';
import '../modules/map/views/map_view.dart';
import '../modules/profile/bindings/profile_binding.dart';
import '../modules/profile/views/profile_view.dart';
import '../modules/register/bindings/register_binding.dart';
import '../modules/register/views/register_view.dart';
import '../modules/route/bindings/route_binding.dart';
import '../modules/route/views/route_view.dart';
import '../modules/sos/bindings/sos_binding.dart';
import '../modules/sos/views/sos_view.dart';

part 'app_routes.dart';

class AppPages {
  AppPages._();

  static const INITIAL = Routes.LOGIN;

  static final routes = [
    GetPage(
      name: _Paths.HOME,
      page: () => const HomeView(),
      binding: HomeBinding(),
    ),
    GetPage(
      name: _Paths.LOGIN,
      page: () => const LoginView(),
      binding: LoginBinding(),
    ),
    GetPage(
      name: _Paths.ROUTE,
      page: () => const RouteView(),
      binding: RouteBinding(),
    ),
    GetPage(
      name: _Paths.MAP,
      page: () => const MapView(),
      binding: MapBinding(),
    ),
    GetPage(
      name: _Paths.CHECK,
      page: () => const CheckView(),
      binding: CheckBinding(),
    ),
    GetPage(
      name: _Paths.SOS,
      page: () => const SosView(),
      binding: SosBinding(),
    ),
    GetPage(
      name: _Paths.ACCOMPANIMENT,
      page: () => const AccompanimentView(),
      binding: AccompanimentBinding(),
    ),
    GetPage(
      name: _Paths.HISTORY,
      page: () => const HistoryView(),
      binding: HistoryBinding(),
    ),
    GetPage(
      name: _Paths.PROFILE,
      page: () => const ProfileView(),
      binding: ProfileBinding(),
    ),
    GetPage(
      name: _Paths.REGISTER,
      page: () => const RegisterView(),
      binding: RegisterBinding(),
    ),
    GetPage(
      name: _Paths.ACTIVE_TRIP,
      page: () => const ActiveTripView(),
      binding: ActiveTripBinding(),
    ),
  ];
}
