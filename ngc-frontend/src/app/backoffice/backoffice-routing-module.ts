import { RouterModule, Routes } from "@angular/router";
import { Backoffice } from "./backoffice/backoffice";
import { Dashboard } from "./dashboard/dashboard";
import { ManagementPerson } from "./management-person/management-person";
import { ManagementRoute } from "./management-route/management-route";
import { Incident } from "./incident/incident";
import { Profile } from "./profile/profile";
import { Reports } from "./reports/reports";
import { NgModule } from "@angular/core";

export const routes: Routes = [
  {
    path: '',
    component: Backoffice,
    children: [
      {
          path: 'tablero',
          component: Dashboard,
          data: { title: 'Tablero' }
      },
      {
          path: 'administracion-de-personas',
          component: ManagementPerson,
          data: { title: 'Administración de Personas' }
      },
      {
          path: 'administracion-de-rutas',
          component: ManagementRoute,
          data: { title: 'Administración de Rutas' }
      },
      {
          path: 'administracion-de-incidentes',
          component: Incident,
          data: { title: 'Administración de Incidentes' }
      },
      {
          path: 'perfil',
          component: Profile,
          data: { title: 'Perfil de Usuario' }
      },
      {
          path: 'reporteria',
          component: Reports,
          data: { title: 'Reporteria' }
      },
    ]
  }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class BackofficeRoutingModule {}