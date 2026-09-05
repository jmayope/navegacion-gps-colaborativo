import { Injectable } from '@angular/core';
import { API_URI, messageAlert, TOKEN_NAME } from '../constants';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Main {
  constructor(
    private Http: HttpClient
  ) {

  }

  uri: string = API_URI;

  // SESSION
  setSession(user: any) {
    sessionStorage.setItem(TOKEN_NAME, JSON.stringify(user));
    return true;
  }

  getSession() {
    return JSON.parse(sessionStorage.getItem(TOKEN_NAME) || '')
  }

  getToken() {
    return JSON.parse(sessionStorage.getItem(TOKEN_NAME) || '{}').token;
  }

  destroySession() {
    sessionStorage.removeItem(TOKEN_NAME);
    return true;
  }

  async login(body: any) {
    try {
      let result: any = await firstValueFrom(this.Http.post(`${this.uri}/auth/login`, body));
      return result || [];
    } catch (error: any) {
      messageAlert(null, `Error al intentar loguear en el sistema + ${error.message}`, 'error');
    }
  }

  async registerUser(body: any) {
    try {
      let result: any = await firstValueFrom(this.Http.post(`${this.uri}/auth/register`, body));
      return result || [];
    } catch (error: any) {
      messageAlert(null, `Error al registrar el usuario + ${error.message}`, 'error');
    }
  }

  async getRows(table: string) {
    try {
      let result: any = await firstValueFrom(this.Http.get(`${this.uri}/${table}`));
      return result || [];
    } catch (error: any) {
      messageAlert(null, `Error al listar usuarios + ${error.message}`, 'error');
    }
  }

  async updateRow(table: string, id: string, data: any) {
    try {
      let result: any = await firstValueFrom(this.Http.put(`${this.uri}/${table}/${id}`, data));
      return result || [];
    } catch (error: any) {
      messageAlert(null, `Error al actualizar el usuario con ID: ${id} + ${error.message}`, 'error');
    }
  }

  async deleteRow(table: string, id: string) {
    try {
      let result: any = await firstValueFrom(this.Http.delete(`${this.uri}/${table}/${id}`));
      return result || [];
    } catch (error: any) {
      messageAlert(null, `Error al actualizar el usuario con ID: ${id} + ${error.message}`, 'error');
    }
  }

}
