import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Subject, takeUntil, debounceTime, distinctUntilChanged } from 'rxjs';

@Component({
  selector: 'app-management-person',
  imports: [],
  templateUrl: './management-person.html',
  styleUrl: './management-person.css',
})
export class ManagementPerson {

// Datos
  users: User[] = [];
  stats: UserStats | null = null;
  loading: boolean = false;
  
  // Filtros
  filters: UserFilters = {
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
  selectedUser: User | null = null;
  showUserModal: boolean = false;
  showConfirmDialog: boolean = false;
  userToDelete: string | null = null;
  isEditMode: boolean = false;

  // Subject para unsubscribe
  private destroy$ = new Subject<void>();
  
  // Subject para búsqueda con debounce
  private searchSubject = new Subject<string>();

  @ViewChild(UserStatsComponent) statsComponent!: UserStatsComponent;
  @ViewChild(UserModalComponent) userModal!: UserModalComponent;
  @ViewChild(ConfirmDialogComponent) confirmDialog!: ConfirmDialogComponent;

  constructor(
    private userService: UserService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
    this.loadStats();
    
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
  loadUsers(): void {
    this.loading = true;
    this.userService.getUsers(this.filters)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: PaginatedResponse<User>) => {
          this.users = response.data;
          this.pagination = response.pagination;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error al cargar usuarios:', error);
          this.toastService.error('Error al cargar la lista de usuarios');
          this.loading = false;
        }
      });
  }

  /**
   * Cargar estadísticas del dashboard
   */
  loadStats(): void {
    this.userService.getDashboardStats()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.stats = response.stats;
        },
        error: (error) => {
          console.error('Error al cargar estadísticas:', error);
        }
      });
  }

  /**
   * Buscar usuarios
   */
  onSearch(searchTerm: string): void {
    this.searchSubject.next(searchTerm);
  }

  /**
   * Cambiar filtro de estado
   */
  onStatusChange(status: 'all' | 'active' | 'inactive'): void {
    this.filters.status = status;
    this.filters.page = 1;
    this.loadUsers();
  }

  /**
   * Cambiar filtro de verificación
   */
  onVerifiedChange(verified: 'all' | 'verified' | 'unverified'): void {
    this.filters.verified = verified;
    this.filters.page = 1;
    this.loadUsers();
  }

  /**
   * Cambiar ordenamiento
   */
  onSortChange(sortBy: 'created_at' | 'full_name' | 'last_activity_at'): void {
    this.filters.sortBy = sortBy;
    this.loadUsers();
  }

  /**
   * Cambiar página
   */
  onPageChange(page: number): void {
    if (page < 1 || page > this.pagination.totalPages) return;
    this.filters.page = page;
    this.loadUsers();
  }

  /**
   * Abrir modal para crear usuario
   */
  openCreateUserModal(): void {
    this.isEditMode = false;
    this.selectedUser = null;
    this.showUserModal = true;
  }

  /**
   * Abrir modal para editar usuario
   */
  openEditUserModal(user: User): void {
    this.isEditMode = true;
    this.selectedUser = user;
    this.showUserModal = true;
  }

  /**
   * Ver detalles del usuario
   */
  viewUserDetails(user: User): void {
    this.selectedUser = user;
    // Navegar a página de detalles o abrir modal con detalles
    // this.router.navigate(['/backoffice/users', user.id]);
  }

  /**
   * Cambiar estado del usuario (activar/desactivar)
   */
  toggleUserStatus(user: User): void {
    const newStatus = !user.is_active;
    const action = newStatus ? 'activar' : 'desactivar';
    
    this.toastService.showConfirm(
      `¿${action} usuario?`,
      `¿Estás seguro de que quieres ${action} a ${user.full_name}?`,
      () => {
        this.userService.toggleUserStatus(user.id, newStatus)
          .pipe(takeUntil(this.destroy$))
          .subscribe({
            next: () => {
              this.toastService.success(`Usuario ${action} correctamente`);
              this.loadUsers();
              this.loadStats();
            },
            error: (error) => {
              console.error('Error al cambiar estado:', error);
              this.toastService.error('Error al cambiar estado del usuario');
            }
          });
      }
    );
  }

  /**
   * Verificar usuario
   */
  verifyUser(user: User): void {
    this.userService.verifyUser(user.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.toastService.success('Usuario verificado correctamente');
          this.loadUsers();
          this.loadStats();
        },
        error: (error) => {
          console.error('Error al verificar usuario:', error);
          this.toastService.error('Error al verificar el usuario');
        }
      });
  }

  /**
   * Abrir diálogo de confirmación para eliminar
   */
  openDeleteConfirm(user: User): void {
    this.userToDelete = user.id;
    this.showConfirmDialog = true;
  }

  /**
   * Eliminar usuario
   */
  deleteUser(): void {
    if (!this.userToDelete) return;

    this.userService.deleteUser(this.userToDelete)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.toastService.success('Usuario eliminado correctamente');
          this.showConfirmDialog = false;
          this.userToDelete = null;
          this.loadUsers();
          this.loadStats();
        },
        error: (error) => {
          console.error('Error al eliminar usuario:', error);
          this.toastService.error('Error al eliminar el usuario');
          this.showConfirmDialog = false;
        }
      });
  }

  /**
   * Cancelar eliminación
   */
  cancelDelete(): void {
    this.showConfirmDialog = false;
    this.userToDelete = null;
  }

  /**
   * Manejar guardado de usuario (crear/editar)
   */
  onUserSaved(): void {
    this.showUserModal = false;
    this.loadUsers();
    this.loadStats();
    this.toastService.success(
      this.isEditMode ? 'Usuario actualizado correctamente' : 'Usuario creado correctamente'
    );
  }

  /**
   * Cerrar modal de usuario
   */
  closeUserModal(): void {
    this.showUserModal = false;
    this.selectedUser = null;
  }

  /**
   * Exportar usuarios
   */
  exportUsers(): void {
    const exportFilters = {
      search: this.filters.search,
      status: this.filters.status,
      verified: this.filters.verified
    };

    this.userService.exportUsers(exportFilters)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `usuarios_${new Date().toISOString().split('T')[0]}.csv`;
          a.click();
          window.URL.revokeObjectURL(url);
          this.toastService.success('Exportación completada');
        },
        error: (error) => {
          console.error('Error al exportar:', error);
          this.toastService.error('Error al exportar usuarios');
        }
      });
  }

  /**
   * Obtener clase de badge para estado
   */
  getStatusBadgeClass(isActive: boolean): string {
    return isActive ? 'badge-active' : 'badge-inactive';
  }

  /**
   * Obtener clase de badge para verificación
   */
  getVerificationBadgeClass(isVerified: boolean): string {
    return isVerified ? 'badge-verified' : 'badge-unverified';
  }

  /**
   * Obtener texto de estado
   */
  getStatusText(isActive: boolean): string {
    return isActive ? 'Activo' : 'Inactivo';
  }

  /**
   * Obtener texto de verificación
   */
  getVerificationText(isVerified: boolean): string {
    return isVerified ? 'Verificado' : 'Pendiente';
  }

  /**
   * Formatear fecha de última actividad
   */
  formatLastActivity(date: string | null): string {
    if (!date) return 'Nunca';
    const now = new Date();
    const lastActivity = new Date(date);
    const diffMs = now.getTime() - lastActivity.getTime();
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffMins < 1) return 'Hace un momento';
    if (diffMins < 60) return `Hace ${diffMins} min`;
    if (diffHours < 24) return `Hace ${diffHours} h`;
    if (diffDays < 7) return `Hace ${diffDays} días`;
    return lastActivity.toLocaleDateString();
  }


}
