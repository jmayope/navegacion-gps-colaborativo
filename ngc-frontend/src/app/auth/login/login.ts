import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

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
    private Router: Router
  ) {}

  credentials: any = {};
  ngOnInit(): void {
      
  }

  async login() {
    console.log(this.credentials);
  }

  goToRegister() {
    this.Router.navigate(["autenticacion/registro"]);
  }
}
