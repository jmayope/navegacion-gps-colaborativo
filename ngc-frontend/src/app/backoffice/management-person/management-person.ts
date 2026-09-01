import { Component, OnInit, OnDestroy, ViewChild, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Subject, takeUntil, debounceTime, distinctUntilChanged, firstValueFrom } from 'rxjs';
import { Main } from '../../services/main';

@Component({
  selector: 'app-management-person',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],
  templateUrl: './management-person.html',
  styleUrl: './management-person.css',
})
export class ManagementPerson {

// Datos
  users: any[] = [];
  stats: any | null = null;
  loading: boolean = false;
  
  // Filtros
  filters: any = {
    search: '',
    status: 'all',
    verified: 'all',
    sortBy: 'created_at',
    sortOrder: 'DESC',
    page: 1,
    limit: 20
  };
  
  // Paginación
  pagination = {
    total: 0,
    totalPages: 0,
    page: 1,
    limit: 20
  };

  // Usuario seleccionado
  selectedUser: any | null = null;
  showUserModal: boolean = false;
  showConfirmDialog: boolean = false;
  userToDelete: string | null = null;
  isEditMode: boolean = false;

  // Subject para unsubscribe
  private destroy$ = new Subject<void>();
  
  // Subject para búsqueda con debounce
  private searchSubject = new Subject<string>();

  // @ViewChild(UserStatsComponent) statsComponent!: UserStatsComponent;
  // @ViewChild(UserModalComponent) userModal!: UserModalComponent;
  // @ViewChild(ConfirmDialogComponent) confirmDialog!: ConfirmDialogComponent;

  constructor(
    private Main: Main,
    private ChangeDetector: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadUsers();
    
    // Configurar búsqueda con debounce
    this.searchSubject.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(searchTerm => {
      this.filters.search = searchTerm;
      this.filters.page = 1;
      this.loadUsers();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * Cargar lista de usuarios
   */
  async loadUsers() {
    this.loading = true;
    let result: any = await this.Main.getUsers();
    this.users = result;
    this.loading = false;
    this.pagination.total = this.users.length;
    this.ChangeDetector.detectChanges();
  }

  toggleUser() {}

  exportUsers() {}


  // /**
  //  * Buscar usuarios
  //  */
  // onSearch(searchTerm: string): void {
  //   this.searchSubject.next(searchTerm);
  // }

  // /**
  //  * Cambiar filtro de estado
  //  */
  // onStatusChange(status: 'all' | 'active' | 'inactive'): void {
  //   this.filters.status = status;
  //   this.filters.page = 1;
  //   this.loadUsers();
  // }

  // /**
  //  * Cambiar filtro de verificación
  //  */
  // onVerifiedChange(verified: 'all' | 'verified' | 'unverified'): void {
  //   this.filters.verified = verified;
  //   this.filters.page = 1;
  //   this.loadUsers();
  // }

  // /**
  //  * Cambiar ordenamiento
  //  */
  // onSortChange(sortBy: 'created_at' | 'full_name' | 'last_activity_at'): void {
  //   this.filters.sortBy = sortBy;
  //   this.loadUsers();
  // }

  // /**
  //  * Cambiar página
  //  */
  // onPageChange(page: number): void {
  //   if (page < 1 || page > this.pagination.totalPages) return;
  //   this.filters.page = page;
  //   this.loadUsers();
  // }

  // /**
  //  * Abrir modal para crear usuario
  //  */
  // openCreateUserModal(): void {
  //   this.isEditMode = false;
  //   this.selectedUser = null;
  //   this.showUserModal = true;
  // }

  // /**
  //  * Abrir modal para editar usuario
  //  */
  // openEditUserModal(user: any): void {
  //   this.isEditMode = true;
  //   this.selectedUser = user;
  //   this.showUserModal = true;
  // }

  // /**
  //  * Ver detalles del usuario
  //  */
  // viewUserDetails(user: any): void {
  //   this.selectedUser = user;
  //   // Navegar a página de detalles o abrir modal con detalles
  //   // this.router.navigate(['/backoffice/users', user.id]);
  // }

  // /**
  //  * Cambiar estado del usuario (activar/desactivar)
  //  */
  // toggleUserStatus(user: any): void {
  //   const newStatus = !user.is_active;
  //   const action = newStatus ? 'activar' : 'desactivar';
    
  //   this.toastService.showConfirm(
  //     `¿${action} usuario?`,
  //     `¿Estás seguro de que quieres ${action} a ${user.full_name}?`,
  //     () => {
  //       this.userService.toggleUserStatus(user.id, newStatus)
  //         .pipe(takeUntil(this.destroy$))
  //         .subscribe({
  //           next: () => {
  //             this.toastService.success(`Usuario ${action} correctamente`);
  //             this.loadUsers();
  //             this.loadStats();
  //           },
  //           error: (error: any) => {
  //             console.error('Error al cambiar estado:', error);
  //             this.toastService.error('Error al cambiar estado del usuario');
  //           }
  //         });
  //     }
  //   );
  // }

  // /**
  //  * Verificar usuario
  //  */
  // verifyUser(user: any): void {
  //   this.userService.verifyUser(user.id)
  //     .pipe(takeUntil(this.destroy$))
  //     .subscribe({
  //       next: () => {
  //         this.toastService.success('Usuario verificado correctamente');
  //         this.loadUsers();
  //         this.loadStats();
  //       },
  //       error: (error: any) => {
  //         console.error('Error al verificar usuario:', error);
  //         this.toastService.error('Error al verificar el usuario');
  //       }
  //     });
  // }

  // /**
  //  * Abrir diálogo de confirmación para eliminar
  //  */
  // openDeleteConfirm(user: any): void {
  //   this.userToDelete = user.id;
  //   this.showConfirmDialog = true;
  // }

  // /**
  //  * Eliminar usuario
  //  */
  // deleteUser(): void {
  //   if (!this.userToDelete) return;

  //   this.userService.deleteUser(this.userToDelete)
  //     .pipe(takeUntil(this.destroy$))
  //     .subscribe({
  //       next: () => {
  //         this.toastService.success('Usuario eliminado correctamente');
  //         this.showConfirmDialog = false;
  //         this.userToDelete = null;
  //         this.loadUsers();
  //         this.loadStats();
  //       },
  //       error: (error: any) => {
  //         console.error('Error al eliminar usuario:', error);
  //         this.toastService.error('Error al eliminar el usuario');
  //         this.showConfirmDialog = false;
  //       }
  //     });
  // }

  // /**
  //  * Cancelar eliminación
  //  */
  // cancelDelete(): void {
  //   this.showConfirmDialog = false;
  //   this.userToDelete = null;
  // }

  // /**
  //  * Manejar guardado de usuario (crear/editar)
  //  */
  // onUserSaved(): void {
  //   this.showUserModal = false;
  //   this.loadUsers();
  //   this.loadStats();
  //   this.toastService.success(
  //     this.isEditMode ? 'Usuario actualizado correctamente' : 'Usuario creado correctamente'
  //   );
  // }

  // /**
  //  * Cerrar modal de usuario
  //  */
  // closeUserModal(): void {
  //   this.showUserModal = false;
  //   this.selectedUser = null;
  // }

  // /**
  //  * Exportar usuarios
  //  */
  // exportUsers(): void {
  //   const exportFilters = {
  //     search: this.filters.search,
  //     status: this.filters.status,
  //     verified: this.filters.verified
  //   };

  //   this.userService.exportUsers(exportFilters)
  //     .pipe(takeUntil(this.destroy$))
  //     .subscribe({
  //       next: (blob) => {
  //         const url = window.URL.createObjectURL(blob);
  //         const a = document.createElement('a');
  //         a.href = url;
  //         a.download = `usuarios_${new Date().toISOString().split('T')[0]}.csv`;
  //         a.click();
  //         window.URL.revokeObjectURL(url);
  //         this.toastService.success('Exportación completada');
  //       },
  //       error: (error: any) => {
  //         console.error('Error al exportar:', error);
  //         this.toastService.error('Error al exportar usuarios');
  //       }
  //     });
  // }

  // /**
  //  * Obtener clase de badge para estado
  //  */
  // getStatusBadgeClass(isActive: boolean): string {
  //   return isActive ? 'badge-active' : 'badge-inactive';
  // }

  // /**
  //  * Obtener clase de badge para verificación
  //  */
  // getVerificationBadgeClass(isVerified: boolean): string {
  //   return isVerified ? 'badge-verified' : 'badge-unverified';
  // }

  // /**
  //  * Obtener texto de estado
  //  */
  // getStatusText(isActive: boolean): string {
  //   return isActive ? 'Activo' : 'Inactivo';
  // }

  // /**
  //  * Obtener texto de verificación
  //  */
  // getVerificationText(isVerified: boolean): string {
  //   return isVerified ? 'Verificado' : 'Pendiente';
  // }

  // /**
  //  * Formatear fecha de última actividad
  //  */
  // formatLastActivity(date: string | null): string {
  //   if (!date) return 'Nunca';
  //   const now = new Date();
  //   const lastActivity = new Date(date);
  //   const diffMs = now.getTime() - lastActivity.getTime();
  //   const diffMins = Math.floor(diffMs / 60000);
  //   const diffHours = Math.floor(diffMs / 3600000);
  //   const diffDays = Math.floor(diffMs / 86400000);

  //   if (diffMins < 1) return 'Hace un momento';
  //   if (diffMins < 60) return `Hace ${diffMins} min`;
  //   if (diffHours < 24) return `Hace ${diffHours} h`;
  //   if (diffDays < 7) return `Hace ${diffDays} días`;
  //   return lastActivity.toLocaleDateString();
  // }


}
