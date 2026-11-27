
import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:flutter_map_location_marker/flutter_map_location_marker.dart';
import 'package:get/get.dart';
import 'package:latlong2/latlong.dart';
import 'package:ngc_movil/app/routes/app_pages.dart';

import '../controllers/search_place_controller.dart';

class SearchPlaceView extends GetView<SearchPlaceController> {
  const SearchPlaceView({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          onPressed: () {
            Get.offAllNamed(Routes.ROUTE);
          },
          icon: const Icon(Icons.home),
        ),
        title: const Text('Busque el lugar'),
        centerTitle: true,
      ),

      body: Stack(
        children: [
          FlutterMap(
            mapController: controller.mapController.value,

            options: const MapOptions(
              initialCenter: LatLng(-12.0464, -77.0428),
              initialZoom: 13,
              minZoom: 3,
              maxZoom: 19,
            ),

            children: [
              // MAPA BASE
              TileLayer(
                urlTemplate:
                    'https://tile.openstreetmap.org/{z}/{x}/{y}.png',

                userAgentPackageName:
                    'com.ngcapp.utpdevs.ngc_movil',

                // Identificación explícita de la aplicación.
                tileProvider: NetworkTileProvider(
                  headers: {
                    'User-Agent':
                        'NGC-Movil/1.0 (com.ngcapp.utpdevs.ngc_movil)',
                  },
                ),

                maxNativeZoom: 19,
              ),

              // UBICACIÓN ACTUAL
              CurrentLocationLayer(
                style: LocationMarkerStyle(
                  marker: DefaultLocationMarker(
                    child: const Icon(
                      Icons.location_pin,
                      color: Colors.white,
                    ),
                  ),
                  markerSize: const Size(35, 35),
                  markerDirection: MarkerDirection.heading,
                ),
              ),

              // ATRIBUCIÓN OSM
              RichAttributionWidget(
                attributions: [
                  TextSourceAttribution(
                    '© OpenStreetMap contributors',
                    onTap: () {},
                  ),
                ],
              ),
            ],
          )
        ],
      ),
    );
  }
}
