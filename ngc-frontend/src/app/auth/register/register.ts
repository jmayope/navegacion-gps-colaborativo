import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

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

  constructor() {

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

}
