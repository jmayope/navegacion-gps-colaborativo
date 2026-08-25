import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

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
    private Router: Router
  ) {

  }

  documentTypes: any = [
    {id: 'DNI', name: "DNI"},
    {id: 'CE', name: "Carnet de Extranjeria"},
    {id: 'PAS', name: "Pasaporte"},
  ];

  newUser: any = {};

  ngOnInit(): void {
      
  }

  async register() {
    console.log(this.newUser);
  }

  togglePassword() {
    this.newUser.showPassword = !this.newUser.showPassword;
  }

  goToBack() {
    this.Router.navigate(["autenticacion/iniciar-sesion"]);
  }

}
