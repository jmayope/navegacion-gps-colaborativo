import { CommonModule } from '@angular/common';
import { Component, HostListener, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterOutlet } from '@angular/router';
import { Main } from '../../services/main';

@Component({
  selector: 'app-backoffice',
  imports: [
    CommonModule,
    FormsModule,
    RouterOutlet
  ],
  templateUrl: './backoffice.html',
  styleUrl: './backoffice.css',
})
export class Backoffice implements OnInit {

  constructor(
    private Main: Main,
    private Router: Router
  ) {}

  ngOnInit(): void {
    this.user_loged = this.Main.getSession();
    let parts = this.user_loged.full_name.split(" ");
    this.user_loged.short_name = `${parts[2]} ${parts[0]}`;
    console.log(this.user_loged);
    this.activeModule = sessionStorage.getItem('menuSelected') || 'Inicio';
  }

  user_loged: any;
  // =========================================================
  // SIDEBAR
  // =========================================================

  sidebarCollapsed = false;
  mobileSidebarOpen = false;


  // =========================================================
  // MENÚ DE USUARIO
  // =========================================================

  userDropdownOpen = false;


  // =========================================================
  // MÓDULO ACTIVO
  // =========================================================

  activeModule = 'Inicio';

  modules: any[] = [
    { id: 1, code: 'Inicio', name: 'Inicio', icon: 'fa-solid fa-house', route: 'backoffice/tablero' },
    { id: 2, code: 'Usuarios', name: 'Usuarios', icon: 'fa-solid fa-users', route: 'backoffice/administracion-de-personas' },
    { id: 3, code: 'Rutas', badge: '7', name: 'Rutas', icon: 'fa-solid fa-route', route: 'backoffice/administracion-de-rutas' },
    { id: 4, code: 'Incidencias', badge: '3', name: 'Incidencias', icon: 'fa-solid fa-triangle-exclamation', route: 'backoffice/administracion-de-incidentes' },  
    { id: 5, code: 'Acompañamiento', name: 'Acompañamiento', icon: 'fa-solid fa-user-shield', route: 'backoffice/acompaniamiento' },
    { id: 6, code: 'Movimientos', name: 'Historial de Movimientos', icon: 'fa-solid fa-clock-rotate-left', route: 'backoffice/historial-de-movimientos' },
    { id: 7, code: 'Chat', name: 'Chats y Comparticiones', icon: 'fa-solid fa-comment-dots', route: 'backoffice/chat-y-comparticiones' },
    { id: 8, code: 'Reportes', name: 'Reportes', icon: 'fa-solid fa-file-lines', route: 'backoffice/reporteria' },
    { id: 7, code: 'Configuración', name: 'Configuración', icon: 'fa-solid fa-gear', route: 'backoffice/chat-y-comparticiones' },
  ];


  // =========================================================
  // RESPONSIVE
  // =========================================================

  isMobile(): boolean {
    return window.innerWidth < 992;
  }


  // =========================================================
  // SIDEBAR
  // =========================================================

  toggleSidebar(): void {

    if (this.isMobile()) {

      this.closeMobileSidebar();

    } else {

      this.sidebarCollapsed = !this.sidebarCollapsed;

    }

  }


  openMobileSidebar(): void {

    if (this.isMobile()) {

      this.mobileSidebarOpen = true;

    }

  }


  closeMobileSidebar(): void {

    this.mobileSidebarOpen = false;

  }


  // =========================================================
  // MENÚ DE USUARIO
  // =========================================================

  toggleUserMenu(event?: Event): void {

    event?.stopPropagation();

    this.userDropdownOpen =
      !this.userDropdownOpen;

  }


  closeUserMenu(): void {

    this.userDropdownOpen = false;

  }


  // =========================================================
  // NAVEGACIÓN
  // =========================================================

  selectModule(module: string): void {

    this.activeModule = module;
    sessionStorage.setItem('menuSelected', module);
    if (this.isMobile()) {
      this.closeMobileSidebar();
    }
    console.log(module);
    let moduleSelected: any = this.modules.find((m: any) => m.code === module);
    if (moduleSelected) {
      this.Router.navigate([moduleSelected.route]);
    }
  }


  // =========================================================
  // RESIZE
  // =========================================================

  @HostListener('window:resize')
  onResize(): void {

    if (!this.isMobile()) {

      this.mobileSidebarOpen = false;

    }

  }


  // =========================================================
  // CLICK FUERA DEL MENÚ
  // =========================================================

  @HostListener('document:click')
  onDocumentClick(): void {

    this.userDropdownOpen = false;

  }

  logout() {
    this.Main.destroySession();
    this.Router.navigate(["autenticacion/iniciar-sesion"]);
  }

  goToProfile() {
    this.activeModule = 'Inicio';    
    sessionStorage.setItem("menuSelected", this.activeModule);
    this.Router.navigate(["backoffice/perfil"]);
  }

  goToSetting() {
    this.activeModule = 'Inicio';    
    sessionStorage.setItem("menuSelected", this.activeModule);
    this.Router.navigate(["backoffice/configuracion"]);
  }

}
