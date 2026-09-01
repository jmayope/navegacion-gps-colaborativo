// src/app/backoffice/backoffice-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ManagementPersonComponent } from '../management-person/management-person.component';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'users',
        component: ManagementPersonComponent,
        data: { title: 'Gestión de Personas' }
      },
      {
        path: 'users/:id',
        component: ManagementPersonComponent,
        data: { title: 'Detalle de Usuario' }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BackofficeRoutingModule { }