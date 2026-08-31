import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Main } from '../../services/main';
import { loadingAlert, messageAlert } from '../../constants';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-register',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register implements OnInit {

  constructor(
    private Router: Router,
    private Main: Main
  ) {

  }

  documentTypes: any = [
    {id: 'DNI', name: "DNI"},
    {id: 'CE', name: "Carnet de Extranjeria"},
    {id: 'PAS', name: "Pasaporte"},
  ];

  newUser: any = {};

  registering: boolean = false;

  ngOnInit(): void {
      
  }

  async register() {
    this.registering = true;
    loadingAlert("Registrando el Usuario");
    console.log(this.newUser);
    let newUser = structuredClone(this.newUser);
    let resultRegister = await this.Main.registerUser(newUser);
    console.log(resultRegister);
    Swal.close();
    messageAlert("Éxito", "Se registró correctamente el usuario", 'success');
  }

  togglePassword() {
    this.newUser.showPassword = !this.newUser.showPassword;
  }

  goToBack() {
    this.Router.navigate(["autenticacion/iniciar-sesion"]);
  }

}
