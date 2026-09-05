import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Main } from '../../services/main';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import * as Leaflet from 'leaflet';

export const getLayers = (): Leaflet.Layer[] => {
  return [
    new Leaflet.TileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    } as Leaflet.TileLayerOptions),
    ...getMarkers()
  ] as Leaflet.Layer[];
};

export const getMarkers = (): Leaflet.Marker[] => {
  return [
    new Leaflet.Marker(new Leaflet.LatLng(-12.0294764,-75.2369883), {
      icon: new Leaflet.Icon({
        iconSize: [50, 41],
        iconAnchor: [13, 41],
        iconUrl: 'assets/blue-marker.svg',
      }),
      title: 'Workspace'
    } as Leaflet.MarkerOptions),
    new Leaflet.Marker(new Leaflet.LatLng(-12.0287209,-75.2369883), {
      icon: new Leaflet.Icon({
        iconSize: [50, 41],
        iconAnchor: [13, 41],
        iconUrl: 'assets/red-marker.svg',
      }),
      title: 'Riva'
    } as Leaflet.MarkerOptions),
  ] as Leaflet.Marker[];
};

@Component({
  selector: 'app-management-route',
  imports: [
    CommonModule,
    FormsModule,
    LeafletModule
  ],
  templateUrl: './management-route.html',
  styleUrl: './management-route.css',
})
export class ManagementRoute implements OnInit {

  constructor(
    private Main: Main
  ) {

  }

  routes: any[] = [];

  options: Leaflet.MapOptions = {
    layers: getLayers(),
    zoom: 12,
    center: new Leaflet.LatLng(-12.0294764,-75.2369883)
  };

  

  ngOnInit(): void {
    this.getRoutes();
  }

  async getRoutes() {
    let resultRoutes: any = await this.Main
  }



}
