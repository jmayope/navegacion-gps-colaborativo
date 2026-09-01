// src/app/backoffice/backoffice.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

// Componentes
import { ManagementPersonComponent } from '../management-person/management-person.component';
import { UserModalComponent } from '../management-person/components/user-modal/user-modal.component';
import { UserStatsComponent } from '../management-person/components/user-stats/user-stats.component';
import { ConfirmDialogComponent } from '../shared/components/confirm-dialog/confirm-dialog.component';

// Servicios
import { UserService } from '../services/user.service';
import { ToastService } from '../services/toast.service';

// Módulo de rutas
import { BackofficeRoutingModule } from './backoffice-routing.module';

@NgModule({
  declarations: [
    ManagementPersonComponent,
    UserModalComponent,
    UserStatsComponent,
    ConfirmDialogComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    BackofficeRoutingModule
  ],
  providers: [
    UserService,
    ToastService
  ]
})
export class BackofficeModule { }