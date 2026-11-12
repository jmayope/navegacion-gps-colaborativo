import { Injectable } from '@angular/core';
import { SUPABASE_API_KEY, SUPABASE_URL } from '../constants';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';

export interface ApiResponse<T = any> {
  success: boolean;
  data: T | T[] | null;
  validation: any[];
  error: string[];
  total?: number;
}

@Injectable({
  providedIn: 'root',
})
export class Supabase {
  private readonly SUPABASE_URL = SUPABASE_URL;
  private readonly SUPABASE_API_KEY = SUPABASE_API_KEY;
  
  private readonly headers = new HttpHeaders({
    'apikey': this.SUPABASE_API_KEY,
    'Authorization': `Bearer ${this.SUPABASE_API_KEY}`,
    'Content-Type': 'application/json',
    'Prefer': 'return=representation'
  });

  constructor(private http: HttpClient) {}

  select<T = any>(table: string, queryParams: any = {}, observeFullResponse = false): Observable<ApiResponse<T>> {
    let url = `${this.SUPABASE_URL}${table}`;
    let params = new HttpParams();
    let headers = this.headers;
    
    if (queryParams.select) {
      params = params.set('select', queryParams.select);
    }
    
    if (queryParams.filters) {
      Object.keys(queryParams.filters).forEach(key => {
        const filter = queryParams.filters![key];
        if (typeof filter === 'object' && filter !== null && 'op' in filter) {
          let val = filter.value;
          if (typeof val === 'string') val = val.replace(/%/g, '*');
          params = params.set(key, `${filter.op}.${val}`);
        } else if (Array.isArray(filter)) {
          params = params.set(key, `in.(${filter.join(',')})`);
        } else {
          params = params.set(key, `eq.${filter}`);
        }
      });
    }
    
    if (queryParams.order) {
      const direction = queryParams.order.direction || 'desc';
      params = params.set('order', `${queryParams.order.column}.${direction}`);
    }

    if (queryParams.range) {
      const { offset, limit } = queryParams.range;
      headers = headers.set('Range', `${offset}-${offset + limit - 1}`);
      headers = headers.set('Prefer', 'count=exact');
    }

    if (observeFullResponse) {
      return this.http.get<T>(url, { headers, params, observe: 'response' }).pipe(
        map((response: HttpResponse<T>) => {
          // 👇 Extraemos el total del header Content-Range (Ej: "0-9/150")
          let total: number | undefined = undefined;
          const contentRange = response.headers.get('Content-Range');
          
          if (contentRange) {
            const parts = contentRange.split('/');
            if (parts.length === 2 && parts[1] !== '*') {
              total = parseInt(parts[1], 10);
            }
          }

          return {
            success: true,
            data: response.body,
            validation: [],
            error: [],
            total: total // 👈 Lo incluimos en la respuesta
          } as ApiResponse<T>;
        }),
        catchError(error => of(this.handleError<T>(error, table, 'SELECT')))
      );
    }

    // 2. Petición normal (devuelve solo el body)
    return this.http.get<T>(url, { headers, params }).pipe(
      map((response: T) => {
        return {
          success: true,
          data: response,
          validation: [],
          error: []
        } as ApiResponse<T>;
      }),
      catchError(error => of(this.handleError<T>(error, table, 'SELECT')))
    );
  }

  insert<T = any>(table: string, data: any): Observable<ApiResponse<T>> {
    const headers = this.headers.set('Prefer', 'return=representation');
    
    return this.http.post<T>(`${this.SUPABASE_URL}${table}`, data, { headers }).pipe(
      map(response => ({
        success: true,
        data: response,
        validation: [],
        error: []
      } as ApiResponse<T>)), // 👈 Casteo agregado
      catchError(error => of(this.handleError<T>(error, table, 'INSERT')))
    );
  }

  update<T = any>(table: string, id: any, data: any, idColumn: string = 'id'): Observable<ApiResponse<T>> {
    const headers = this.headers.set('Prefer', 'return=representation');
    const params = new HttpParams().set(idColumn, `eq.${id}`);
    
    return this.http.patch<T>(`${this.SUPABASE_URL}${table}`, data, { headers, params }).pipe(
      map(response => ({
        success: true,
        data: response,
        validation: [],
        error: []
      } as ApiResponse<T>)), // 👈 Casteo agregado
      catchError(error => of(this.handleError<T>(error, table, 'UPDATE')))
    );
  }

  delete<T = any>(table: string, id: any, idColumn: string = 'id'): Observable<ApiResponse<T>> {
    const params = new HttpParams().set(idColumn, `eq.${id}`);
    
    return this.http.delete<T>(`${this.SUPABASE_URL}${table}`, { headers: this.headers, params }).pipe(
      map(response => ({
        success: true,
        data: response,
        validation: [],
        error: []
      } as ApiResponse<T>)), // 👈 Casteo agregado
      catchError(error => of(this.handleError<T>(error, table, 'DELETE')))
    );
  }

  rpc<T = any>(
    functionName: string,
    params: Record<string, any> = {},
    options: { get?: boolean } = {}
  ): Observable<ApiResponse<T>> {
    const url = `${this.SUPABASE_URL}rpc/${functionName}`;

    if (options.get) {
      let httpParams = new HttpParams();
      Object.keys(params).forEach(key => {
        const value = params[key];
        if (value !== undefined && value !== null) {
          httpParams = httpParams.set(key, typeof value === 'object' ? JSON.stringify(value) : String(value));
        }
      });

      return this.http.get<T>(url, { headers: this.headers, params: httpParams }).pipe(
        map(response => ({
          success: true,
          data: response,
          validation: [],
          error: []
        } as ApiResponse<T>)), // 👈 Casteo agregado
        catchError(error => of(this.handleError<T>(error, functionName, 'RPC')))
      );
    }

    return this.http.post<T>(url, params, { headers: this.headers }).pipe(
      map(response => ({
        success: true,
        data: response,
        validation: [],
        error: []
      } as ApiResponse<T>)), // 👈 Casteo agregado
      catchError(error => of(this.handleError<T>(error, functionName, 'RPC')))
    );
  }

  // 👇 Se volvió genérico
  private handleError<T>(error: any, context: string, method: string): ApiResponse<T> {
    console.error(`Error en ${method} (${context}):`, error);
    
    const validation: any[] = [];
    const errorList: string[] = [];
    const errorBody = error.error || {};
    
    if (errorBody.code === '23505') {
      validation.push({ type: 'unique_violation', message: errorBody.message });
      errorList.push('Ya existe un registro con esos datos (violación de unicidad).');
    } else if (errorBody.code === '23503') {
      validation.push({ type: 'foreign_key_violation', message: errorBody.message });
      errorList.push('El registro está relacionado con otra tabla y no se puede procesar (violación de llave foránea).');
    } else if (errorBody.details) {
      validation.push({ type: errorBody.code, message: errorBody.details });
      errorList.push(errorBody.message || 'Error de validación en la base de datos.');
    } else if (error.status) {
      errorList.push(`Error ${error.status}: ${error.statusText || 'Fallo en la petición'}`);
    } else {
      errorList.push(error.message || 'Error desconocido en la comunicación con Supabase.');
    }

    return {
      success: false,
      data: null,
      validation: validation,
      error: errorList
    };
  }
}