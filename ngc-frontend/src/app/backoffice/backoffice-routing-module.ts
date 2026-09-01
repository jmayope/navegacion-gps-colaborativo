import { RouterModule, Routes } from "@angular/router";
import { Backoffice } from "./backoffice/backoffice";
import { Dashboard } from "./dashboard/dashboard";
import { ManagementPerson } from "./management-person/management-person";
import { ManagementRoute } from "./management-route/management-route";
import { Incident } from "./incident/incident";
import { Profile } from "./profile/profile";
import { Reports } from "./reports/reports";
import { NgModule } from "@angular/core";
import { Accompaniment } from "./accompaniment/accompaniment";
import { MovementsHistory } from "./movements-history/movements-history";
import { ChatAndSharedContent } from "./chat-and-shared-content/chat-and-shared-content";
import { Setting } from "./setting/setting";

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
          path: 'acompaniamiento',
          component: Accompaniment,
          data: { title: 'Acompañamiento' }
      },
      {
          path: 'historial-de-movimientos',
          component: MovementsHistory,
          data: { title: 'Historial de Movimientos' }
      },
      {
          path: 'chat-y-comparticiones',
          component: ChatAndSharedContent,
          data: { title: 'Chats y Comparticiones' }
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
      {
          path: 'configuracion',
          component: Setting,
          data: { title: 'Configuración' }
      },
    ]
  }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class BackofficeRoutingModule {}