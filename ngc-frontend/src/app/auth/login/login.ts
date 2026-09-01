import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Main } from '../../services/main';
import { loadingAlert, messageAlert } from '../../constants';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {

  constructor(
    private Router: Router,
    private Main: Main
  ) {}

  credentials: any = {};
  logging: boolean = false;
  showPassword: boolean = false;
  ngOnInit(): void {
      
  }

  async login() {
    this.logging = true;
    loadingAlert("Verificando credenciales");
    console.log(this.credentials);
    let resultLogin: any = await this.Main.login(this.credentials);
    console.log(resultLogin);
    this.logging = false;
    
    if (!resultLogin.id) {
      Swal.close();
      messageAlert("Error", resultLogin.message, 'error');
      return;
    }
    if (resultLogin.is_admin) {
      let userLoged = structuredClone(resultLogin);
      delete userLoged.message;
      delete userLoged.success;
  
      let userSaved: boolean = this.Main.setSession(userLoged);
      Swal.close();
      this.Router.navigate(["backoffice/tablero"]);
    } else {
      messageAlert("Advertencia", "No tienes acceso a este servicio.", 'warning');
      return;
    }
  }

  goToRegister() {
    this.Router.navigate(["autenticacion/registro"]);
  }

  goToForgotPassword() {
    this.Router.navigate(["autenticacion/olvido-clave"]);
  }

  toggleShowPassword() {
    this.showPassword = !this.showPassword;
  }
}
