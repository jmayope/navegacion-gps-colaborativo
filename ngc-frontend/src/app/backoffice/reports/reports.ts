import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { Main } from '../../services/main';
import { Supabase } from '../../services/supabase';
import moment from 'moment';

@Component({
  selector: 'app-reports',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './reports.html',
  styleUrl: './reports.css',
})
export class Reports implements OnInit {

  constructor(
    private Main: Main,
    private Supabase: Supabase
  ) {

  }

  ngOnInit(): void {
    this.getFunction();
  }

  async getFunction() {
    let result: any = await firstValueFrom(this.Supabase.rpc('fn_get_routes', {p_user_id: null, p_search: null, p_date_init: '2026-01-01', p_date_end: '2026-12-31'}));
    console.log(result);
  }

}
