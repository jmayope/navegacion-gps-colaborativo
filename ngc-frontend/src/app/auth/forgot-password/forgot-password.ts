import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.css',
})
export class ForgotPassword implements OnInit {

  constructor(
    private Router: Router
  ) {

  }

  resetPassword: any = {};

  ngOnInit(): void {
    
  }

  goToBack() {
    this.Router.navigate(["autenticacion/iniciar-sesion"]);
  }

  sendPassword() {
    console.log(this.resetPassword);
  }

}
