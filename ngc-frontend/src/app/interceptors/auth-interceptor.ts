import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { Main } from '../services/main';
import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const mainService = inject(Main);
  // Obtener el token del localStorage
  const token = mainService.getToken();
  
  // Si existe el token, clonar la petición y añadir el header de autorización
  const publicEndpoints = ['/login', '/register'];
  const isPublicEndpoint = publicEndpoints.some(url => req.url.includes(url));
  if (token && !isPublicEndpoint) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  
  // Continuar con la petición y manejar errores
  return next(req).pipe(
    catchError((error) => {
      // Si recibimos un 401, redirigir al login
      if (error.status === 401) {
        localStorage.removeItem('token');
        router.navigate(['/login']);
      }
      
      // Si recibimos un 403, el usuario no tiene permisos
      if (error.status === 403) {
        console.error('No tienes permisos para acceder a este recurso');
      }
      
      return throwError(() => error);
    })
  );
};