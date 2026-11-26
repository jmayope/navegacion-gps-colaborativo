import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Main } from '../../services/main';
import { Supabase } from '../../services/supabase';
import { firstValueFrom } from 'rxjs';
import { COLLECTIONS, messageAlert } from '../../constants';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-chat-and-shared-content',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './chat-and-shared-content.html',
  styleUrl: './chat-and-shared-content.css',
})
export class ChatAndSharedContent implements OnInit {

  constructor(
    private Main: Main,
    private Supabase: Supabase,
    private ChangeDetector: ChangeDetectorRef
  ) {

  }

  user_loged: any;
  filter: any = {};
  users: any[] = [];
  searching: boolean = false;
  searched: boolean = false;
  routes: any[] = [];
  chat_selected: any;

  async ngOnInit() {
    this.user_loged = await this.Main.getSession();
    console.log(this.user_loged);
    this.getUsers();
  }

  async getUsers() {
    let result: any = await firstValueFrom(this.Supabase.select(COLLECTIONS.USERS));
    console.log(result);
    if (!result.success) {
      messageAlert("Error", "Hubo un error al listar los usuarios", "error");
      return;
    }
    this.users = result.data.filter((u: any) => u.id !== this.user_loged.id);
    this.ChangeDetector.detectChanges();
  }

  async searchChats() {
    if (this.searched) {
      this.searched = false;
      this.routes = [];
      this.filter.user_selected = undefined;
    } else {
      if (this.filter.user_selected) {
        this.searched = false;
        if (this.searching) return;
        this.searching = true;
        let result_owner: any = await firstValueFrom(this.Supabase.select(COLLECTIONS.CHATS, {filters: {user_id: this.filter.user_selected.id}}));
        if (!result_owner.success) {
          messageAlert("Error", "Hubo un error al listar los chats del usuario", "error");
          return;
        }
        
        let result_participant: any = await firstValueFrom(this.Supabase.select(COLLECTIONS.CHATS, {filters: {recipient_user_id: this.filter.user_selected.id}}));
        if (!result_participant.success) {
          messageAlert("Error", "Hubo un error al listar los chats del usuario", "error");
          return;
        }
        
        let total_chats: any[] = [...result_owner.data, ...result_participant.data];

        if (total_chats.length) {
          let route_ids: any[] = [...new Set(total_chats.map((r: any) => r.route_id))];
          console.log(route_ids);
          let result_routes: any = await firstValueFrom(this.Supabase.select(COLLECTIONS.ROUTES, {filters: {id: route_ids}}));
          route_ids.map((r: any) => {
            let new_route: any = result_routes.data.find((x: any) => r === x.id);
            this.routes.push(new_route);
          });
          console.log(this.routes);
          this.routes.map((r: any) => {
            r.chats = total_chats.filter((c: any) => c.route_id === r.id);
            if (r.chats.length) {
              r.last_message = r.chats[r.chats.length - 1];
              if (r.last_message) {
                r.message_dont_reads = r.chats.filter((c: any) => !c.is_read).length;
              }
            }
          });
          this.routes = this.routes;
          this.searching = false;
        } else {
          this.searching = false;
          this.routes = [];
        }
        this.searched = true;
        this.ChangeDetector.detectChanges();
      }
    }
  }

  async selectChat(chat: any) {
    this.chat_selected = chat;
    if (this.chat_selected) {
      
      let recipients: any = [...new Set(this.chat_selected.chats.map((c: any) => c.recipient_user_id))];
      console.log(recipients);
      let total_recipients: any = [this.filter.user_selected.id, ...recipients];
      console.log(total_recipients);
      let result_users: any = await firstValueFrom(this.Supabase.select(COLLECTIONS.USERS, {filters: {id: total_recipients}}));
      if (!result_users.success) {
        messageAlert("Error", "Error al obtener usuarios de la Conversación", "error");
        return;
      }
      this.chat_selected.users = result_users.data;
      this.chat_selected.chats.map((c: any) => {
        c.user = this.chat_selected.users.find((u: any) => u.id === c.user_id);
        c.recipient_user = this.chat_selected.users.find((u: any) => u.id === c.recipient_user_id);
      })
      if (this.chat_selected.users.length > 2) {
        this.chat_selected.user_labels = `${this.chat_selected.users[0].full_name}, ${this.chat_selected.users[1].full_name}, +${this.chat_selected.users.length - 2}`;
      } else {
        this.chat_selected.user_labels = this.chat_selected.users.map((u: any) => u.full_name).join(', ');
      }
      this.chat_selected.total_recipients = total_recipients;
      // , recipient_user_id: total_recipients, user_id: total_recipients
      this.ChangeDetector.detectChanges();
    }
  }
}
