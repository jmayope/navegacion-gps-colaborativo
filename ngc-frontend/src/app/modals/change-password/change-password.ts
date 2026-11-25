import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Main } from '../../services/main';
import { Supabase } from '../../services/supabase';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { COLLECTIONS, messageAlert } from '../../constants';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-change-password',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './change-password.html',
  styleUrl: './change-password.css',
})
export class ChangePassword implements OnInit {
  constructor(
    public modalRef: BsModalRef,
    private Main: Main,
    private Supabase: Supabase,
    private ChangeDetector: ChangeDetectorRef,
  ) {}

  user_loged: any;
  new_credentials: any = {};
  changing: boolean = false;
  async ngOnInit() {
    this.user_loged = await this.Main.getSession();
  }

  toggleShowPassword() {
    this.new_credentials.show_password = !this.new_credentials.show_password;
  }

  async save() {
    if (this.changing) return;
    this.changing = true;
    if (this.new_credentials.new_password !== this.new_credentials.confirm_password) {
      messageAlert("Validación", "Las contraseñas deben coincidir.", "warning");
      return;
    }  

    let change_password: any = {
      password: this.new_credentials.new_password
    };

    let result_change_password: any = await firstValueFrom(this.Supabase.update(COLLECTIONS.USERS, this.user_loged.id, change_password));
    if (!result_change_password.success) {
      this.changing = false;
      messageAlert("Error", "Error al actualizar la contraseña", "error");
      return;
    }

    messageAlert("Éxito", "Se modificó la contraseña", "success");
    this.modalRef.hide();
  }

}
