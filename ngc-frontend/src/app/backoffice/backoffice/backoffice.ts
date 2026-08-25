import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-backoffice',
  imports: [
    CommonModule,
    FormsModule,
    RouterOutlet
  ],
  templateUrl: './backoffice.html',
  styleUrl: './backoffice.css',
})
export class Backoffice {

}
