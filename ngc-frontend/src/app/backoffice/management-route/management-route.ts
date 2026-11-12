import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Main } from '../../services/main';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import * as L from 'leaflet';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { buildPagination, COLLECTIONS, getStatusInfo, loadingAlert, messageAlert, NUM_ROWS, ROUTE_STATUS } from '../../constants';
import { Supabase } from '../../services/supabase';
import { firstValueFrom } from 'rxjs';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import Swal from 'sweetalert2';

// export const getLayers = (): Leaflet.Layer[] => {
//   return [
//     new Leaflet.TileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
//       attribution: '&copy; OpenStreetMap contributors'
//     } as Leaflet.TileLayerOptions),
//     // ...getMarkers()
//   ] as Leaflet.Layer[];
// };

// export const getMarkers = (): Leaflet.Marker[] => {
//   return [
//     new Leaflet.Marker(new Leaflet.LatLng(-12.0294764,-75.2369883), {
//       icon: new Leaflet.Icon({
//         iconSize: [50, 41],
//         iconAnchor: [13, 41],
//         iconUrl: 'assets/blue-marker.svg',
//       }),
//       title: 'Workspace'
//     } as Leaflet.MarkerOptions),
//     new Leaflet.Marker(new Leaflet.LatLng(-12.0287209,-75.2369883), {
//       icon: new Leaflet.Icon({
//         iconSize: [50, 41],
//         iconAnchor: [13, 41],
//         iconUrl: 'assets/red-marker.svg',
//       }),
//       title: 'Riva'
//     } as Leaflet.MarkerOptions),
//   ] as Leaflet.Marker[];
// };

@Component({
  selector: 'app-management-route',
  imports: [
    CommonModule,
    FormsModule,
    LeafletModule,
    BsDropdownModule
  ],
  templateUrl: './management-route.html',
  styleUrl: './management-route.css',
})
export class ManagementRoute implements OnInit {

  constructor(
    private Main: Main,
    private ModalService: BsModalService,
    private ChangeDetector: ChangeDetectorRef,
    private Supabase: Supabase
  ) {

  }


  routes: any[] = [];

  // options: Leaflet.MapOptions = {
  //   layers: getLayers(),
  //   zoom: 16,
  //   center: new Leaflet.LatLng(-12.0294764,-75.2369883)
  // };

  pagination: any = {
    offset: 0,
    limit: 10,
    num_rows: 10,
    current_page: 1,
    total: 0
  };
  num_rows: number[] = NUM_ROWS;

  filters: any = {};
  route_status: any[] = ROUTE_STATUS;
  modalRef?: BsModalRef;
  user_loged: any;
  pages: number[] = [];

  map: any;
  currentCoords:[number, number] = [0,0];
  user_marker: L.Marker<any> | undefined;
  map_zoom: number = 14;

  route_icon = L.icon({
    iconUrl: 'images/pin.png',
    iconSize: [35,35]
  });

  user_icon = L.icon({
    iconUrl: 'images/user_pin.png',
    iconSize: [35,41]
  });
  click_markers: L.Marker<any>[] = [];
  markers_info: any[] = [];

  route_layer: L.GeoJSON | null = null;

  ngOnInit(): void {
    this.user_loged = this.Main.getSession();
    this.loadRoutes();
  }

  changeNumRows() {
    this.pagination.current_page = 1;
    this.loadRoutes();
  }

  async loadRoutes() {
    let result_routes: any;
    if (this.user_loged.usage_supabase) {
      result_routes = await firstValueFrom(this.Supabase.select(COLLECTIONS.ROUTES, {range: {offset: (this.pagination.current_page - 1) * this.pagination.num_rows, limit: (this.pagination.current_page * this.pagination.num_rows)}}, true));
      console.log(result_routes);
      if (result_routes.success) {
        this.pagination.total = result_routes.total;
        this.routes = result_routes.data;
        if (this.pagination.total > 0) {
          this.pages = buildPagination(this.pagination.total, this.pagination.num_rows);
          this.getAdditionalInfo();
        }
      } else {
        // Si falla, mostramos el error amigable
        messageAlert("Error", result_routes.error.join(' | '), "error");
        
        // Si hay errores de validación de la BD (ej. email duplicado)
        if (result_routes.validation.length > 0) {
          console.warn('Validaciones de Supabase:', result_routes.validation);
        }
      }
    } else {
      result_routes = await this.Main.getRows(COLLECTIONS.ROUTES);
      this.routes = result_routes;
      this.getAdditionalInfo();
    }

    console.log(result_routes);
    // let resultRoutesByText: any;
    // if (this.user_loged.usage_supabase) {
    //   resultRoutesByText = await firstValueFrom(this.Supabase.select(COLLECTIONS.ROUTES, {filters: {origin_name: {op: 'ilike', value: '%casa%' }}}));
    // } else {
    //   resultRoutesByText = await this.Main.getByText(COLLECTIONS.ROUTES, 'casa');
    // }
    // console.log(resultRoutesByText);
    
    
  }

  async getAdditionalInfo() {
    if (this.user_loged.usage_supabase) {
      console.log([...new Set(this.routes.map((r: any) => r.user_id))]);
      let result_users: any = await firstValueFrom(this.Supabase.select(COLLECTIONS.USERS, {filters: {id: [...new Set(this.routes.map((r: any) => r.user_id))]}}))
      if (result_users.success) {
        this.routes.map((r: any) => {
          let statusData: any = getStatusInfo(r.status);
          r.status_name = statusData.name;
          r.status_color = statusData.color;
          r.user = result_users.data.find((u: any) => u.id === r.user_id);
        })
      } else {
        // Si falla, mostramos el error amigable
        messageAlert("Error", result_users.error.join(' | '), "error");
        
        // Si hay errores de validación de la BD (ej. email duplicado)
        if (result_users.validation.length > 0) {
          console.warn('Validaciones de Supabase:', result_users.validation);
        }
      }
    } else {
      // SIN HACER NADA AUN

    }
    this.initMap();
  }
  
  initMap() {
    if (this.map) {
      console.log("El mapa ya está inicializado. Evitando doble creación.");
      return; // Evita crear el mapa si ya existe
    }
    
    this.map = L.map('map').setView(this.currentCoords, this.map_zoom);
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(this.map);
    this.getLocation();
  }

  async addMarkerAt(lat: number, lng: number) {
    const newMarker = L.marker([lat, lng], {
      icon: this.route_icon,
      draggable: true
    })
      .addTo(this.map)
      .bindPopup(`Punto: ${lat.toFixed(2)}, ${lng.toFixed(2)}`);

    // Pushear el marker de inmediato
    this.click_markers.push(newMarker);
    
    // 👇 Hacemos la petición a Nominatim SIN await (en segundo plano)
    fetch(`https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${lat}&lon=${lng}&zoom=18&addressdetails=1`)
      .then(response => response.json())
      .then(data => {
        const streetName = data.address?.road || data.display_name || "Dirección no encontrada";
        let route_point = {
          name: `punto ${lat} - ${lng}`,
          latitude: lat,
          longitude: lng,
          direction: `${streetName} - ${this.markers_info.length + 1}`,
          address: `${streetName} Lat: ${lat} - Lng:${lng}`
        };

        this.markers_info.push(route_point);
        this.ChangeDetector.detectChanges();
      })
      .catch(error => {
        console.error("Error en geocodificación inversa:", error);
        // Aunque falle la dirección, agregamos el punto a la info
        this.markers_info.push({
          name: `punto ${lat} - ${lng}`,
          latitude: lat,
          longitude: lng,
          direction: `Dirección desconocida - ${this.markers_info.length + 1}`,
          address: `Lat: ${lat} - Lng:${lng}`
        });
        newMarker.setPopupContent(`Dirección no encontrada<br>${lat.toFixed(5)}, ${lng.toFixed(5)}`);
        this.ChangeDetector.detectChanges();
      });
  }

  async getAdditionalData() {
    loadingAlert("Calculando...");
    const { distance, duration, milestones } = await this.getRoadTotalEstimate();
    console.log(`Distancia: ${(distance / 1000).toFixed(2)} km`);
    console.log(milestones);
    this.markers_info.filter((i, k) => k > 0).map((i, k) => {
      let milestone_found = milestones[k];
      console.log(i);
      console.log(milestone_found);
      i.distance = milestone_found.distance;
      i.estimated_time_minutes = milestone_found.duration;
    });
    // this.new_route.distance_km = parseFloat((distance / 1000).toFixed(2));
    // this.new_route.estimated_duration_hours = parseFloat((duration / 3600).toFixed(2));
    console.log(parseFloat((distance / 1000).toFixed(2)));
    console.log(parseFloat((duration / 3600).toFixed(2)));
    console.log(this.markers_info);
    Swal.close();
    this.ChangeDetector.detectChanges();
  }

  async getRoadTotalEstimate(): Promise<{ distance: number; duration: number, milestones: any[], geometry: any }> {
    if (this.click_markers.length < 2) {
      console.warn("Se necesitan al menos 2 markers para calcular la ruta.");
      return { distance: 0, duration: 0, milestones: [], geometry: null };
    }

    const coords = this.click_markers
      .map(m => {
        const ll = m.getLatLng();
        return `${ll.lng},${ll.lat}`;
      })
      .join(';');

    const url = `https://router.project-osrm.org/route/v1/driving/${coords}?overview=full&geometries=geojson`;

    try {
      console.log("Llamando a OSRM:", url);
      const res = await fetch(url);
      
      if (!res.ok) {
        throw new Error(`Error HTTP: ${res.status} ${res.statusText}`);
      }
      
      const data = await res.json();
      console.log("Respuesta de OSRM:", data);
      
      if (data.code !== 'Ok' || !data.routes?.length) {
        throw new Error(`Error de OSRM: ${data.message || 'No routes found'}`);
      }
      
      const route = data.routes[0];
      return {
        distance: route.distance,
        duration: route.duration,
        milestones: route.legs,
        geometry: route.geometry
      };
    } catch (err: any) {
      console.error('Error OSRM:', err);
      messageAlert("Aviso", "No se pudo calcular la estimación: " + err.message, "warning");
      return { distance: 0, duration: 0, milestones: [], geometry: null };
    }
  }

  getLocation() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((position) => {
        let coords: [number, number] = [position.coords.latitude, position.coords.longitude];
        this.currentCoords = coords;
        if (this.user_marker) {
          this.user_marker.setLatLng(coords); 
        } else {
          this.user_marker = L.marker(coords, {
            icon: this.user_icon,
            draggable: true
          }).addTo(this.map).bindPopup("Estas aqui").openPopup();
        }

        this.user_marker.on("dragend", (event) => {
          let marker = event.target;
          let position = marker.getLatLng();
          marker.setLatLng(position).openPopup();
          this.map.setView(position, this.map_zoom);
          console.log(`Marker movido: posicion: ${position.lat} ${position.lng}`)
          this.ChangeDetector.detectChanges();  
        });

        this.map.setView(coords, this.map_zoom);
        this.ChangeDetector.detectChanges();
      }, () => {
        messageAlert("Aviso", "No se pudo obtener la Geolocalización", "warning");  
      })
    } else {
      messageAlert("Aviso", "Geolocalización no soportada para este navegador", "warning");
    }
  }

  clearMarkers() {
    this.click_markers.forEach(marker => {
      this.map.removeLayer(marker);
    });

    // 👇 Eliminar la capa de la ruta si existe
    if (this.route_layer) {
      this.map.removeLayer(this.route_layer);
      this.route_layer = null;
    }

    this.click_markers = [];
    this.markers_info = [];
    
    this.ChangeDetector.detectChanges();
  }

  async seeInTheMap(route: any) {
    console.log("Ejecutando seeInTheMap...");
    
    if (!this.map) {
      console.error("El mapa no está inicializado aún.");
      return;
    }

    this.clearMarkers(); 
    
    await this.addMarkerAt(route.origin_lat, route.origin_lng);
    await this.addMarkerAt(route.destination_lat, route.destination_lng);

    console.log("Markers agregados. Iniciando cálculo de ruta...");
    await this.drawRouteBetweenMarkers(); // 👈 Agregar await aquí por seguridad
  }

  async drawRouteBetweenMarkers() {
    console.log("Iniciando dibujo de ruta...");
    
    const { distance, duration, milestones, geometry } = await this.getRoadTotalEstimate();
    
    if (!geometry) {
      console.error("No hay geometría para dibujar.");
      return;
    }

    // Eliminar ruta anterior si existe
    if (this.route_layer) {
      this.map.removeLayer(this.route_layer);
      this.route_layer = null;
    }

    try {
      const geoJsonFeature = {
        type: "Feature",
        properties: {},
        geometry: geometry
      };

      // Dibujar la ruta
      this.route_layer = L.geoJSON(geoJsonFeature as any, {
        style: {
          color: '#0055ff', // Azul brillante
          weight: 7,
          opacity: 1,
          lineCap: 'round',
          lineJoin: 'round'
        }
      }).addTo(this.map);

      // Forzar a que la línea esté por encima de todo
      this.route_layer.bringToFront();

      // Arreglar el tamaño del mapa
      this.map.invalidateSize();
      
      // Esperar un momento a que Angular estabilice el DOM y hacer zoom
      setTimeout(() => {
        if (this.route_layer) {
          // this.map.fitBounds(this.route_layer.getBounds(), { padding: [50, 50] });
          this.map.setView([-12.054105, -75.271505], 16);
          console.log("Zoom ajustado a la ruta.");
        }
      }, 200);
      
      console.log("¡Ruta dibujada con éxito en el mapa!");
    } catch (error) {
      console.error("Error al dibujar la ruta en Leaflet:", error);
    }
    
    this.ChangeDetector.detectChanges();
  }


  changePage(page: number) {
    this.pagination.current_page = page;
    this.loadRoutes();
  }

  // getStatusName(status: string) {
  //   return getStatusName(status);
  // }

  
}
