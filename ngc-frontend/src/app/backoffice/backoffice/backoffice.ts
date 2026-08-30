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
      
  }

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

    if (this.isMobile()) {

      this.closeMobileSidebar();

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

}
