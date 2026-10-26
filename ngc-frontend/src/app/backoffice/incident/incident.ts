import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

type Type = 'Accidente' | 'Robo' | 'Tráfico' | 'Cierre de vía' | 'Peligro';
type Severity = 'Crítica' | 'Alta' | 'Media' | 'Baja';
interface IncidentRecord { id: number; code: string; type: Type; severity: Severity; address: string; route: string; segment: string; reporter: string; description: string; date: string; elapsed: string; isPanic: boolean; isResolved: boolean; photoName?: string; }

@Component({ selector: 'app-incident', imports: [CommonModule, FormsModule], templateUrl: './incident.html', styleUrl: './incident.css' })
export class Incident {
  readonly pageSize = 6;
  search = ''; typeFilter = ''; severityFilter = ''; statusFilter = ''; currentPage = 1;
  showForm = false; showDetail = false; selectedIncident: IncidentRecord | null = null;
  formSubmitted = false;
  newIncident = this.empty();

  incidents: IncidentRecord[] = [];
  get activeCount() { return this.incidents.filter(i => !i.isResolved).length; }
  get criticalCount() { return this.incidents.filter(i => i.severity === 'Crítica' && !i.isResolved).length; }
  get resolvedCount() { return this.incidents.filter(i => i.isResolved).length; }
  get filtered() { const q = this.search.trim().toLowerCase(); return this.incidents.filter(i => (!q || `${i.code} ${i.type} ${i.address} ${i.reporter}`.toLowerCase().includes(q)) && (!this.typeFilter || i.type === this.typeFilter) && (!this.severityFilter || i.severity === this.severityFilter) && (!this.statusFilter || (this.statusFilter === 'Activa' ? !i.isResolved : i.isResolved))); }
  get totalPages() { return Math.max(1, Math.ceil(this.filtered.length / this.pageSize)); }
  get visible() { return this.filtered.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize); }
  filter() { this.currentPage = 1; }
  clear() { this.search = ''; this.typeFilter = ''; this.severityFilter = ''; this.statusFilter = ''; this.filter(); }
  page(n: number) { this.currentPage = Math.min(Math.max(n, 1), this.totalPages); }
  detail(i: IncidentRecord) { this.selectedIncident = i; this.showDetail = true; }
  create() { this.newIncident = this.empty(); this.formSubmitted = false; this.showForm = true; }
  close() { this.showForm = false; this.showDetail = false; this.selectedIncident = null; }
  resolve(i: IncidentRecord) { i.isResolved = true; i.isPanic = false; i.elapsed = 'Resuelta hace unos momentos'; this.close(); }
  remove(i: IncidentRecord) { if (window.confirm(`¿Eliminar la incidencia ${i.code}?`)) { this.incidents = this.incidents.filter(item => item.id !== i.id); this.close(); this.page(this.currentPage); } }
  attachPhoto(event: Event) { const input = event.target as HTMLInputElement; this.newIncident.photoName = input.files?.[0]?.name; }
  get formComplete() { return [this.newIncident.address, this.newIncident.route, this.newIncident.segment, this.newIncident.description].every(value => value.trim().length > 0); }
  save() { this.formSubmitted = true; if (!this.formComplete) return; const id = this.incidents.length ? Math.max(...this.incidents.map(i => i.id)) + 1 : 1; this.incidents.unshift({ ...this.newIncident, address: this.newIncident.address.trim(), route: this.newIncident.route.trim(), segment: this.newIncident.segment.trim(), description: this.newIncident.description.trim(), id, code: `#INC-${String(id).padStart(4, '0')}`, date: 'Ahora', elapsed: 'Recién reportada', isResolved: false }); this.close(); this.page(1); }
  icon(type: Type) { return ({ Accidente: 'fa-car-burst', Robo: 'fa-user-shield', Tráfico: 'fa-traffic-light', 'Cierre de vía': 'fa-road-barrier', Peligro: 'fa-triangle-exclamation' })[type]; }
  color(s: Severity) { return ({ Crítica: 'danger', Alta: 'warning', Media: 'info', Baja: 'secondary' })[s]; }
  private empty(): IncidentRecord { return { id: 0, code: '', type: 'Accidente', severity: 'Media', address: '', route: '', segment: '', reporter: 'Administrador', description: '', date: '', elapsed: '', isPanic: false, isResolved: false }; }
}
