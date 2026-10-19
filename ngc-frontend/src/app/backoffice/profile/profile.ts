import { Component, OnInit } from '@angular/core';
import { Main } from '../../services/main';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-profile',
  imports: [],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  constructor(
    private Main: Main
  ) {

  }

  userLoged: any;
  ngOnInit(): void {
    this.userLoged = this.Main.getSession();
  }
  
}
