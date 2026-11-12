import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { COLLECTIONS, DOCUMENT_TYPES, generateRandomString, messageAlert } from '../../constants';
import { Main } from '../../services/main';
import { firstValueFrom, Subject } from 'rxjs';
import { Supabase } from '../../services/supabase';

@Component({
  selector: 'app-new-user',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './new-user.html',
  styleUrl: './new-user.css',
})
export class NewUser implements OnInit {
  
  constructor(
    public modalRef: BsModalRef,
    private Main: Main,
    private ChangeDetector: ChangeDetectorRef,
    private Supabase: Supabase
  ) {}

  user!: any;
  //  = {
  //   email: '',
  //   full_name: ''
  // };
  document_types: any[] = DOCUMENT_TYPES;
  public onClose: Subject<any> = new Subject<any>();
  user_loged: any;

  ngOnInit() {
    this.user_loged = this.Main.getSession();
    console.log(this.user);
    if (this.user) {
      this.user.editing = true;
    } else {
      this.user = {};
    }
    this.ChangeDetector.detectChanges();
  }

  async save() {
    let newUser = structuredClone(this.user);

    if (!newUser.id) {
      newUser.password = generateRandomString(8);
      newUser.last_location_lat = 72.0333;
      newUser.last_location_lng = 72.0333;
      let resultUser: any;
      if (this.user_loged.usage_supabase) {
        resultUser = await firstValueFrom(this.Supabase.insert(COLLECTIONS.USERS, newUser));
      } else {
        resultUser = await this.Main.registerUser(newUser);
      }
      console.log(resultUser);
      if (!resultUser) {
        messageAlert("Error", "Hubo un error al insertar el usuario", "error");
        return;
      }
      messageAlert("Éxito", "Se crearon correctamente el usuario", "success");
    } else {
      let resultUpdate: any;
      if (this.user_loged.usage_supabase) {
        delete newUser.editing;
        resultUpdate = await firstValueFrom(this.Supabase.update(COLLECTIONS.USERS, newUser.id, newUser));
      } else {
        delete newUser.id;
        resultUpdate = await this.Main.updateRow(COLLECTIONS.USERS, this.user.id, newUser);
      }
      if (!resultUpdate) {
        messageAlert("Error", "Hubo un error al actualizar el usuario", "error");
        return;
      }
      messageAlert("Éxito", "Se actualizaron correctamente el usuario", "success");
    }
    this.onClose.next({
      registered: true
    });
    this.modalRef.hide();
  }


}
