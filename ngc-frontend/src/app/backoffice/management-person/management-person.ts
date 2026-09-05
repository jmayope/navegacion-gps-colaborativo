import { Component, OnInit, OnDestroy, ViewChild, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Subject, takeUntil, debounceTime, distinctUntilChanged, firstValueFrom } from 'rxjs';
import { Main } from '../../services/main';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { NewUser } from '../../modals/new-user/new-user';
import Swal from 'sweetalert2';
import { FilterPipe } from '../../pipes/filter-pipe';
import { COLLECTIONS, STATUS_RECORDS, VERIFIEDS_RECORDS } from '../../constants';
import { SortPipe } from '../../pipes/sort-pipe';

@Component({
  selector: 'app-management-person',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    FilterPipe,
    SortPipe
  ],
  templateUrl: './management-person.html',
  styleUrl: './management-person.css',
})
export class ManagementPerson {

// Datos
  users: any[] = [];
  stats: any | null = null;
  loading: boolean = false;
  modalRef?: BsModalRef;
  
  // Filtros
  filters: any = {    
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

  status_records: any[] = STATUS_RECORDS;
  verifieds_records: any[] = VERIFIEDS_RECORDS;
  fields_sorts: any[] = [
    {id: 1, field: 'full_name', order: 'asc', name: 'Nombre A-Z' },
    {id: 2, field: 'full_name', order: 'desc', name: 'Nombre Z-A' },
    {id: 3, field: 'last_activity_at', order: 'desc', name: 'Última actividad'}
  ];

  field: any;
  order: any;

  
  constructor(
    private Main: Main,
    private ChangeDetector: ChangeDetectorRef,
    private ModalService: BsModalService
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
    let result: any = await this.Main.getRows(COLLECTIONS.USERS);
    this.users = result;
    this.loading = false;
    this.pagination.total = this.users.length;
    this.ChangeDetector.detectChanges();
  }

  toggleUser(user?: any) {
    console.log(user);
    this.modalRef = this.ModalService.show(NewUser, {class: 'modal-sm'});
    
    if (this.modalRef && this.modalRef.content) {
      this.modalRef.content.user = structuredClone(user);
    }

    this.modalRef.content.onClose.subscribe((data: any) => {
      if (data.registered) {
        this.loadUsers();
      }
    })
  }

  deleteUser(user: any) {
    Swal.fire({
      icon: 'question',
      text: `¿Estás seguro de eliminar el Usuario con ID: ${user.id}?`,
      allowEscapeKey: false,
      allowOutsideClick: false,
      showCancelButton: true,
      showConfirmButton: true
    }).then(async (choice) => {
      if (choice.isConfirmed) {
        let resultDelete: any = await this.Main.deleteRow(COLLECTIONS.USERS, user.id);
        console.log(resultDelete);
        this.loadUsers();
      }
    })
  }

  exportUsers() {}
}
