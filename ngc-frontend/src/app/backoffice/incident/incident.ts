import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

type Type = 'Accidente' | 'Robo' | 'Tráfico' | 'Cierre de vía' | 'Peligro';
type Severity = 'Crítica' | 'Alta' | 'Media' | 'Baja';
interface IncidentRecord { id: number; code: string; type: Type; severity: Severity; address: string; route: string; segment: string; reporter: string; description: string; date: string; elapsed: string; isPanic: boolean; isResolved: boolean; }

@Component({ selector: 'app-incident', imports: [CommonModule, FormsModule], templateUrl: './incident.html', styleUrl: './incident.css' })
export class Incident {
  readonly pageSize = 6;
  search = ''; typeFilter = ''; severityFilter = ''; statusFilter = ''; currentPage = 1;
  showForm = false; showDetail = false; selectedIncident: IncidentRecord | null = null;
  newIncident = this.empty();
  incidents: IncidentRecord[] = [
    { id: 248, code: '#INC-0248', type: 'Accidente', severity: 'Crítica', address: 'Av. Javier Prado Este 2450', route: 'R-001', segment: 'S-14', reporter: 'María Flores', description: 'Colisión entre dos vehículos. La vía presenta circulación restringida.', date: 'Hoy, 10:42', elapsed: 'Hace 8 min', isPanic: true, isResolved: false },
    { id: 247, code: '#INC-0247', type: 'Cierre de vía', severity: 'Alta', address: 'Av. Arequipa cuadra 18', route: 'R-014', segment: 'S-06', reporter: 'Carlos Ruiz', description: 'Desvío temporal por trabajos en la calzada.', date: 'Hoy, 09:58', elapsed: 'Hace 52 min', isPanic: false, isResolved: false },
    { id: 246, code: '#INC-0246', type: 'Tráfico', severity: 'Media', address: 'Panamericana Sur km 18', route: 'R-008', segment: 'S-22', reporter: 'Luis Paredes', description: 'Congestión vehicular en sentido norte-sur.', date: 'Hoy, 09:15', elapsed: 'Hace 1 h 35 min', isPanic: false, isResolved: false },
    { id: 245, code: '#INC-0245', type: 'Peligro', severity: 'Baja', address: 'Av. Brasil cuadra 12', route: 'R-003', segment: 'S-09', reporter: 'Andrea Silva', description: 'Objeto en la vía retirado por el equipo de apoyo.', date: 'Ayer, 18:30', elapsed: 'Resuelta a las 19:10', isPanic: false, isResolved: true },
    { id: 244, code: '#INC-0244', type: 'Robo', severity: 'Alta', address: 'Av. La Marina cuadra 25', route: 'R-011', segment: 'S-04', reporter: 'José Ramírez', description: 'Reporte de robo en las inmediaciones de la ruta.', date: 'Ayer, 16:48', elapsed: 'Hace 18 h', isPanic: false, isResolved: false },
    { id: 243, code: '#INC-0243', type: 'Peligro', severity: 'Media', address: 'Vía Expresa Paseo de la República', route: 'R-006', segment: 'S-17', reporter: 'Sofía Mendoza', description: 'Vehículo averiado atendido y retirado.', date: 'Ayer, 15:10', elapsed: 'Resuelta a las 15:32', isPanic: false, isResolved: true },
    { id: 242, code: '#INC-0242', type: 'Tráfico', severity: 'Media', address: 'Av. Benavides cuadra 31', route: 'R-009', segment: 'S-11', reporter: 'Diego León', description: 'Tráfico lento por alta demanda vehicular.', date: 'Ayer, 13:24', elapsed: 'Hace 21 h', isPanic: false, isResolved: false }
  ];
  get activeCount() { return this.incidents.filter(i => !i.isResolved).length; }
  get criticalCount() { return this.incidents.filter(i => i.severity === 'Crítica' && !i.isResolved).length; }
  get resolvedCount() { return this.incidents.filter(i => i.isResolved).length; }
  get filtered() { const q = this.search.trim().toLowerCase(); return this.incidents.filter(i => (!q || `${i.code} ${i.type} ${i.address} ${i.reporter}`.toLowerCase().includes(q)) && (!this.typeFilter || i.type === this.typeFilter) && (!this.severityFilter || i.severity === this.severityFilter) && (!this.statusFilter || (this.statusFilter === 'Activa' ? !i.isResolved : i.isResolved))); }
  get totalPages() { return Math.max(1, Math.ceil(this.filtered.length / this.pageSize)); }
  get visible() { return this.filtered.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize); }
  refresh() { this.currentPage = 1; }
  filter() { this.currentPage = 1; }
  clear() { this.search = ''; this.typeFilter = ''; this.severityFilter = ''; this.statusFilter = ''; this.filter(); }
  page(n: number) { this.currentPage = Math.min(Math.max(n, 1), this.totalPages); }
  detail(i: IncidentRecord) { this.selectedIncident = i; this.showDetail = true; }
  create() { this.newIncident = this.empty(); this.showForm = true; }
  close() { this.showForm = false; this.showDetail = false; this.selectedIncident = null; }
  resolve(i: IncidentRecord) { i.isResolved = true; i.isPanic = false; i.elapsed = 'Resuelta hace unos momentos'; this.close(); }
  save() { const id = Math.max(...this.incidents.map(i => i.id)) + 1; this.incidents.unshift({ ...this.newIncident, id, code: `#INC-${id}`, date: 'Ahora', elapsed: 'Recién reportada', isResolved: false }); this.close(); this.page(1); }
  icon(type: Type) { return ({ Accidente: 'fa-car-burst', Robo: 'fa-user-shield', Tráfico: 'fa-traffic-light', 'Cierre de vía': 'fa-road-barrier', Peligro: 'fa-triangle-exclamation' })[type]; }
  color(s: Severity) { return ({ Crítica: 'danger', Alta: 'warning', Media: 'info', Baja: 'secondary' })[s]; }
  private empty(): IncidentRecord { return { id: 0, code: '', type: 'Accidente', severity: 'Media', address: '', route: '', segment: '', reporter: 'Administrador', description: '', date: '', elapsed: '', isPanic: false, isResolved: false }; }
}
