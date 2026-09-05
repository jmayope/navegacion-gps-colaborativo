import Swal from "sweetalert2";

export const API_URI = "http://localhost:8080/api";
export const TOKEN_NAME = 'NGS-TOKEN';

export const COLLECTIONS = {
  USERS: 'users',
  ROUTES: 'routes'
};


export function generateRandomString(length: number) {
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()';
  let result = '';
  for (let i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * characters.length));
  }
  return result;
}

export const DOCUMENT_TYPES = [
  {id: 'DNI', name: "DNI"},
  {id: 'CE', name: "Carnet de Extranjeria"},
  {id: 'PAS', name: "Pasaporte"},
];

export const STATUS_RECORDS = [
  {id: true, name: "Activo" },
  {id: false, name: "Inactivo" },
];

export const VERIFIEDS_RECORDS = [
  {id: true, name: "Verificado" },
  {id: false, name: "No Verificado" },
];

export function loadingAlert(message: string, timerInSeconds?: number) {
  if (timerInSeconds) {
    Swal.fire({
      html: `
        <div class="spinner-grow text-primary" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
        <div class="spinner-grow text-secondary" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
        <div class="spinner-grow text-success" role="status">
          <span class="visually-hidden">Loading...</span>
        </div><br>${message}
      `,
      timer: timerInSeconds * 1000,
      allowEscapeKey: false,
      allowOutsideClick: false,
      showConfirmButton: false,
      showCancelButton: false
    });
  } else {
    Swal.fire({
      html: `
        <div class="spinner-grow text-primary" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
        <div class="spinner-grow text-secondary" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
        <div class="spinner-grow text-success" role="status">
          <span class="visually-hidden">Loading...</span>
        </div><br>${message}
      `,
      allowEscapeKey: false,
      allowOutsideClick: false,
      showConfirmButton: false,
      showCancelButton: false
    });
  }
}

export function messageAlert(title: any, message: string, icon: string) {
  let icons: any = {
    'success': 'success',
    'error': 'error',
    'warning': 'warning',
    'info': 'info',
  }
  Swal.fire({
    text: message,
    icon: icons[icon]
  });
}