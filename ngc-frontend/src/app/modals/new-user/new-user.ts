import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { DOCUMENT_TYPES, generateRandomString, messageAlert } from '../../constants';
import { Main } from '../../services/main';
import { Subject } from 'rxjs';

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
    private ChangeDetector: ChangeDetectorRef
  ) {}

  user!: any;
  document_types: any[] = DOCUMENT_TYPES;
  public onClose: Subject<any> = new Subject<any>();

  ngOnInit() {
    console.log(this.user);
    if (this.user) {
      this.user.editing = true;
    } {
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
      
      let resultUser: any = await this.Main.registerUser(newUser);
      console.log(resultUser);
      if (!resultUser) {
        messageAlert("Error", "Hubo un error al insertar el usuario", "error");
        return;
      }
      messageAlert("Éxito", "Se crearon correctamente el usuario", "success");
    } else {
      delete newUser.id;
      let resultUpdate: any = await this.Main.updateUser(this.user.id, newUser);
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
