import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Main } from '../../services/main';
import { COLLECTIONS, messageAlert } from '../../constants';

type Type = 'Accidente' | 'Robo' | 'Tráfico' | 'Cierre de vía' | 'Peligro';
type Severity = 'Crítica' | 'Alta' | 'Media' | 'Baja';

@Component({ selector: 'app-incident', imports: [CommonModule, FormsModule], templateUrl: './incident.html', styleUrl: './incident.css' })
export class Incident implements OnInit {

  constructor(
    private Main: Main,
    private ChangeDetector: ChangeDetectorRef
  ) {

  }

  user_loged: any;

  ngOnInit() {
    this.getIncidents();
  }

  async getIncidents() {
    let result_incidents: any = await this.Main.getRows(COLLECTIONS.INCIDENTS);
    console.log(result_incidents);
    this.incidents = result_incidents;
    this.getRoutesInfo();
    
  }

  async getRoutesInfo() {
    let result_routes: any = await this.Main.getRows(COLLECTIONS.ROUTES);
    let result_segments: any = await this.Main.getRows(COLLECTIONS.SEGMENTS);
    let result_users: any = await this.Main.getRows(COLLECTIONS.USERS);

    this.incidents.map((i: any) => {
      i.route = result_routes.find((r: any) => r.id === i.route_id);
      if (i.segment_id) {
        i.segment = result_segments.find((s: any) => s.id === i.segment_id);
      }
      if (i.route) {
        i.route.segments = result_segments.filter((s: any) => s.route_id === i.route.id);
      }
      i.user = result_users.find((u: any) => u.id === i.user_id);
    });
    this.ChangeDetector.detectChanges();
  }

  readonly pageSize = 6;
  search = ''; typeFilter = ''; severityFilter = ''; statusFilter = ''; currentPage = 1;
  showForm = false; showDetail = false; selectedIncident: any | null = null;
  newIncident = this.empty();
  incidents: any[] = [];
  get activeCount() { 
    return this.incidents.filter(i => !i.isResolved).length; 
  }

  get criticalCount() { 
    return this.incidents.filter(i => i.severity === 'high' && !i.isResolved).length; 
  }

  get resolvedCount() { 
    return this.incidents.filter(i => i.isResolved).length; 
  }

  get filtered() { 
    const q = this.search.trim().toLowerCase(); return this.incidents.filter(i => (!q || `${i.code} ${i.type} ${i.address} ${i.reporter}`.toLowerCase().includes(q)) && (!this.typeFilter || i.type === this.typeFilter) && (!this.severityFilter || i.severity === this.severityFilter) && (!this.statusFilter || (this.statusFilter === 'Activa' ? !i.isResolved : i.isResolved))); 
  }

  get totalPages() { 
    return Math.max(1, Math.ceil(this.filtered.length / this.pageSize)); 
  }

  get visible() { 
    return this.filtered.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize); 
  }

  refresh() { 
    this.currentPage = 1; 
  }

  filter() { 
    this.currentPage = 1; 
  }

  clear() { 
    this.search = ''; this.typeFilter = ''; this.severityFilter = ''; this.statusFilter = ''; this.filter(); 
  }

  page(n: number) { 
    this.currentPage = Math.min(Math.max(n, 1), this.totalPages); 
  }

  detail(i: any) { 
    this.selectedIncident = i; this.showDetail = true; 
  }

  create() { 
    this.newIncident = this.empty(); this.showForm = true; 
  }

  close() { 
    this.showForm = false; this.showDetail = false; this.selectedIncident = null; 
  }

  async resolve(i: any) { 
    let data: any = structuredClone(i);
    data.is_resolved = true; 
    data.is_panic = false; 
    data.resolved_at = new Date();
    data.description = `Re | RESUELTO: ${data.description}`; 
    delete data.id;
    let result_incident: any = await this.Main.updateRow(COLLECTIONS.INCIDENTS, i.id, data);
    if (!result_incident) {
      messageAlert("Error", "Hubo un error al momento de resolver el incidente", "error");
      return;
    }
    messageAlert("Éxito", "Se resolvio el inconveniente", "success");
    this.getIncidents();
    this.close(); 
  }

  save() { 
    const id = Math.max(...this.incidents.map(i => i.id)) + 1; this.incidents.unshift({ ...this.newIncident, id, code: `#INC-${id}`, date: 'Ahora', elapsed: 'Recién reportada', isResolved: false }); this.close(); this.page(1); 
  }

  icon(type: Type) { 
    return ({ Accidente: 'fa-car-burst', Robo: 'fa-user-shield', Tráfico: 'fa-traffic-light', 'Cierre de vía': 'fa-road-barrier', Peligro: 'fa-triangle-exclamation' })[type]; 
  }

  color(s: Severity) { 
    return ({ Crítica: 'danger', Alta: 'warning', Media: 'info', Baja: 'secondary' })[s]; 
  }

  private empty(): any { return { id: 0, code: '', type: 'Accidente', severity: 'Media', address: '', route: '', segment: '', reporter: 'Administrador', description: '', date: '', elapsed: '', isPanic: false, isResolved: false }; }
}
