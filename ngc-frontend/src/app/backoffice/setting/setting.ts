import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Main } from '../../services/main';
import { firstValueFrom } from 'rxjs';
import { Supabase } from '../../services/supabase';
import { COLLECTIONS, messageAlert } from '../../constants';

@Component({
  selector: 'app-setting',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './setting.html',
  styleUrl: './setting.css',
})
export class Setting implements OnInit {

  constructor(
    private Main: Main,
    private ChangeDetector: ChangeDetectorRef,
    private Supabase: Supabase
  ) {

  }

  user_loged: any;

  tabs: any[] = [
    {id: 1,icon: 'fa-solid fa-sliders', name: 'General'},
    {id: 2,icon: 'fa-solid fa-bell', name: 'Notificaciones'},
    // {id: 3,icon: 'fa-solid fa-shield-halved', name: 'Seguridad'},
    {id: 4,icon: 'fa-solid fa-palette', name: 'Apariencia'},
    {id: 5,icon: 'fa-solid fa-plug', name: 'Integraciones'},
  ];

  timezones: any[] = [
    {id: 'PE', name: 'America/Lima  (GMT-5)'},
    {id: 'CO', name: 'America/Bogota  (GMT-5)'},
    {id: 'CL', name: 'America/Santiago  (GMT-4)'},
  ];

  languages: any[] = [
    {id: 'es', name: 'Español'},
    {id: 'en', name: 'English'},
    {id: 'pt', name: 'Portugues'},
  ]

  currencies: any[] = [
    {id: 'PEN', name: 'PEN — Sol peruano'},
    {id: 'USD', name: 'USD — Dólar estadounidense'}
  ];

  date_formats: any[] = [
    'DD/MM/AAAA', 'MM/DD/AAAA'
  ]

  // Notificaciones
  config_notifications: any[] = [
    {title: 'Notificaciones', description: 'Recibe una notificación push cuando se reporte una incidencia.', field: 'notifications'},
    {title: 'Compartir ubicación', description: 'Recibe una notificación push cuando se reporte una incidencia.', field: 'share_location'},
  ];

  tab_selected: any;

  can_edit: boolean = false;
  notification_edit: boolean = false;
  editing: boolean = false;


  async ngOnInit() {
    this.user_loged = await this.Main.getSession();
    this.user_loged.app_name = this.user_loged.app_name || 'Ruta Segura'
    this.selectTab(this.tabs[0]);
    this.ChangeDetector.detectChanges();
  }

  selectTab(tab: any) {
    this.tab_selected = tab;
  }

  async toggleEdit() {
    this.can_edit = !this.can_edit;
    if (!this.can_edit) {
      if (this.editing) return;
      this.editing = true;
      let update_user: any = {
        timezone: this.user_loged.timezone,
        language: this.user_loged.language,
        currency: this.user_loged.currency,
        date_format: this.user_loged.date_format,
        notifications: this.user_loged.notifications,
        share_location: this.user_loged.share_location
      };

      let result_main: any = await firstValueFrom(this.Supabase.update(COLLECTIONS.USERS, this.user_loged.id, update_user));
      if (!result_main.success) {
        messageAlert("Error", "Error al actualizar información adicional", "error");
        this.editing = false;
        return;
      }
      this.editing = false;
      messageAlert('Éxito', "Se guardaron los cambios", 'success');
    }
  }

}
