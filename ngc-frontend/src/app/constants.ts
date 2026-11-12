import Swal from "sweetalert2";

export const API_URI = "http://localhost:8080/api";
export const TOKEN_NAME = 'NGS-TOKEN';

export const SUPABASE_URL = "https://vkandgvzpxpyvijwwaaj.supabase.co/rest/v1/";
export const SUPABASE_API_KEY = "sb_publishable_657f3Z1zxNcI_hAOr64mcA_LCaeCrWl";

export const COLLECTIONS = {
  USERS: 'users',
  USER_SESSIONS: 'user_sessions',
  CHATS: 'chats',
  ROUTES: 'routes',
  INCIDENTS: 'incidents',
  SEGMENTS: 'segments',
  SEGMENT_DETAILS: 'segment_details',
  ROUTE_QUERIES: 'route_queries',
  MOOD_STATES: 'mood_states',
  MOVEMENT_HISTORY: 'movement_history'
};

export const STATUS_COMPLETED = 'completed';
export const STATUS_IN_PROGRESS = 'in_progress';
export const STATUS_PLANNING = 'planning';

export function getStatusInfo(status: string) {
  switch (status) {
    case STATUS_COMPLETED:
      return {name: 'Completado', color: 'success'};
    case STATUS_IN_PROGRESS:
      return {name: 'En progreso', color: 'warning'};
    case STATUS_PLANNING:
      return {name: 'Planificado', color: 'primary'};
    default:
      return {name: 'Sin estado', color: 'dark'};
  }
}

export const NUM_ROWS = [5,10,20,50,100];

export function buildPagination(total: number, rows: number) {
    let pages = [];
    for (let p = 1; p <= Math.ceil(total / rows); p++) {
      pages.push(p);
    }
    return pages;
}

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

export const ROUTE_STATUS = [
  { id: 'planning', name: 'Planificación', }, 
  { id: 'in_progress', name: 'En progreso', }, 
  { id: 'completed', name: 'Completado', }, 
  { id: 'cancelled', name: 'Cancelado', }
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