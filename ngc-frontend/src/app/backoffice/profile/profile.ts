import { Component, OnInit } from '@angular/core';
import { Main } from '../../services/main';
import { firstValueFrom } from 'rxjs';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { Supabase } from '../../services/supabase';
import { COLLECTIONS, messageAlert } from '../../constants';

@Component({
  selector: 'app-profile',
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  constructor(
    private Main: Main,
    private Router: Router,
    private Supabase: Supabase
  ) {

  }

  user_loged: any;
  edit: boolean = false;
  ngOnInit(): void {
    this.user_loged = this.Main.getSession();
  }

  async logout() {
    Swal.fire({
      text: '¿Estás seguro de cerrar sesión?',
      icon: 'question',
      showCancelButton: true, 
      showConfirmButton: true,
      allowEscapeKey: false,
      allowOutsideClick: false
    }).then(async (choice) => {
      if (choice.isConfirmed) {
        await this.Main.destroySession();
        this.Router.navigate(["/autenticacion/iniciar-sesion"]);
      }
    }) 
  }

  async toggleEdit() {
    this.edit = !this.edit;
    if (!this.edit) {
      console.log("Guardando información del usuario");
      let data_to_update: any = structuredClone(this.user_loged);
      delete data_to_update.id;
      data_to_update.updated_at = (new Date()).toISOString();
      let result: any = await this.Supabase.update(COLLECTIONS.USERS, this.user_loged.id, data_to_update);
      if (!result.success) {
        messageAlert("Error", "Error mientras se actualiza la información del usuario", "error");
        return;
      }

      messageAlert("Éxito", "Se actualizaron todos los datos", "success");
    }
  }
  
}
