import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Main } from '../../services/main';
import { COLLECTIONS, loadingAlert, messageAlert } from '../../constants';
import Swal from 'sweetalert2';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-login',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {

  constructor(
    private Main: Main,
    private Router: Router,
    private ChangeDetector: ChangeDetectorRef
  ) { }

  credentials: any = {};
  show_password: boolean = false;
  remember: boolean = false;
  logging: boolean = false;

  // ---- Información del servicio (panel derecho) ----
  service_features: any[] = [
    {
      icon: 'fa-location-dot',
      title: 'Seguimiento en tiempo real',
      text: 'Ubica a tus usuarios en el mapa mientras recorren su ruta.'
    },
    {
      icon: 'fa-bell',
      title: 'Alertas instantáneas',
      text: 'Recibe un aviso al momento de cualquier incidencia o desvío de ruta.'
    },
    {
      icon: 'fa-user-shield',
      title: 'Acompañamiento virtual 24/7',
      text: 'Un agente puede acompañar al usuario durante todo su trayecto.'
    }
  ];

  service_stats: any[] = [
    { value: '32K+', label: 'Usuarios protegidos' },
    { value: '99.8%', label: 'Disponibilidad' },
    { value: '24/7', label: 'Monitoreo activo' }
  ];

  // ---- Ventajas ----
  advantages: any[] = [
    {
      icon: 'fa-satellite-dish',
      color: 'primary',
      title: 'Monitoreo en vivo',
      text: 'Visualiza la ubicación, la velocidad y el estado de cada usuario en un solo mapa.'
    },
    {
      icon: 'fa-bolt',
      color: 'warning',
      title: 'Respuesta inmediata',
      text: 'Las alertas llegan al instante para que tu equipo actúe antes de que escale el problema.'
    },
    {
      icon: 'fa-share-nodes',
      color: 'success',
      title: 'Rutas compartidas',
      text: 'Comparte el recorrido con familiares o contactos de confianza mediante un enlace seguro.'
    },
    {
      icon: 'fa-clock-rotate-left',
      color: 'info',
      title: 'Historial completo',
      text: 'Consulta cada movimiento, parada e incidencia registrada para auditorías y reportes.'
    },
    {
      icon: 'fa-comments',
      color: 'danger',
      title: 'Comunicación directa',
      text: 'Chat y llamadas con el usuario o su grupo de seguimiento desde la misma plataforma.'
    },
    {
      icon: 'fa-lock',
      color: 'secondary',
      title: 'Privacidad y control',
      text: 'El usuario decide qué comparte y con quién; tú administras los permisos por rol.'
    }
  ];

  // ---- Casos de éxito (contenido de ejemplo: reemplazar por casos reales) ----
  success_cases: any[] = [
    {
      sector: 'Transporte interurbano',
      company: 'Transportes Andes',
      metric: '-42%',
      metric_label: 'en el tiempo de respuesta ante incidentes',
      quote: 'Antes dependíamos de llamadas para saber dónde estaba cada unidad. Hoy vemos todo en tiempo real y reaccionamos en minutos.',
      author: 'Marcos Villanueva',
      role: 'Jefe de operaciones'
    },
    {
      sector: 'Rutas escolares',
      company: 'Colegio San Martín',
      metric: '100%',
      metric_label: 'de las familias con seguimiento en vivo',
      quote: 'Los padres ya no llaman para preguntar si el bus llegó. Ven el recorrido desde su teléfono y están tranquilos.',
      author: 'Lucía Paredes',
      role: 'Coordinadora de movilidad'
    },
    {
      sector: 'Traslado de personal',
      company: 'Minera Horizonte',
      metric: '0',
      metric_label: 'incidencias sin atender en 6 meses',
      quote: 'El acompañamiento virtual nos da la seguridad de que nuestro personal nunca viaja solo, incluso en zonas remotas.',
      author: 'Carlos Ramírez',
      role: 'Gerente de seguridad'
    }
  ];

  current_year: number = new Date().getFullYear();

  ngOnInit(): void {
    let remembered_email = localStorage.getItem('remember_email');
    if (remembered_email) {
      this.credentials.email = remembered_email;
      this.remember = true;
    }
  }

  async login() {
    if (this.logging) {
      return;
    }

    let email = (this.credentials.email || '').trim().toLowerCase();
    let password = this.credentials.password || '';

    if (!email || !password) {
      messageAlert("Error", "Ingresa tu correo y tu contraseña", "error");
      return;
    }

    this.logging = true;

    try {
      let users: any = await this.Main.getRows(COLLECTIONS.USERS);
      let result_users = users.filter((u: any) => u.email === email);
      let user = (result_users && result_users.length) ? result_users[0] : null;

      // Mismo mensaje si el usuario no existe o la contraseña no coincide
      if (!user || !(await this.verifyPassword(password, user.password))) {
        messageAlert("Error", "Correo o contraseña incorrectos", "error");
        return;
      }

      if (!user.is_active) {
        messageAlert("Cuenta inactiva", "Tu cuenta está desactivada. Contacta al administrador.", "warning");
        return;
      }

      if (this.remember) {
        localStorage.setItem('remember_email', email);
      } else {
        localStorage.removeItem('remember_email');
      }

      sessionStorage.setItem('session_user', JSON.stringify({
        id: user.id,
        email: user.email,
        first_name: user.first_name,
        last_name: user.last_name,
        role: user.role,
        company_id: user.company_id
      }));

      if (user.is_admin) {
        let userLoged = structuredClone(user);
        delete userLoged.message;
        delete userLoged.success;
    
        let userSaved: boolean = this.Main.setSession(userLoged);
        Swal.close();
        this.Router.navigate(["backoffice/tablero"]);
      } else {
        messageAlert("Advertencia", "No tienes acceso a este servicio.", 'warning');
        return;
      }
    } catch (error) {
      console.error('Error en login:', error);
      messageAlert("Error", "No se pudo iniciar sesión. Inténtalo nuevamente.", "error");
    } finally {
      this.logging = false;
      this.ChangeDetector.detectChanges();
    }
  }

  togglePassword() {
    this.show_password = !this.show_password;
  }

  // Compara el SHA-256 (hex) de la contraseña con el hash guardado
  private async verifyPassword(password: string, hash: string): Promise<boolean> {
    if (!hash) {
      return false;
    }
    // let data = new TextEncoder().encode(password);
    // let digest = await crypto.subtle.digest('SHA-256', data);
    // let hex = Array.from(new Uint8Array(digest))
    //   .map(b => b.toString(16).padStart(2, '0'))
    //   .join('');
    return password === hash;
  }

  // constructor(
  //   private Router: Router,
  //   private Main: Main
  // ) {}

  // credentials: any = {};
  // logging: boolean = false;
  // showPassword: boolean = false;
  // ngOnInit(): void {
      
  // }

  // async login() {
  //   this.logging = true;
  //   loadingAlert("Verificando credenciales");
  //   console.log(this.credentials);
  //   let resultLogin: any = await this.Main.login(this.credentials);
  //   console.log(resultLogin);
  //   this.logging = false;
    
  //   if (!resultLogin.id) {
  //     Swal.close();
  //     messageAlert("Error", resultLogin.message, 'error');
  //     return;
  //   }
  //   if (resultLogin.is_admin) {
  //     let userLoged = structuredClone(resultLogin);
  //     delete userLoged.message;
  //     delete userLoged.success;
  
  //     let userSaved: boolean = this.Main.setSession(userLoged);
  //     Swal.close();
  //     this.Router.navigate(["backoffice/tablero"]);
  //   } else {
  //     messageAlert("Advertencia", "No tienes acceso a este servicio.", 'warning');
  //     return;
  //   }
  // }

  // goToRegister() {
  //   this.Router.navigate(["autenticacion/registro"]);
  // }

  // goToForgotPassword() {
  //   this.Router.navigate(["autenticacion/olvido-clave"]);
  // }

  // toggleShowPassword() {
  //   this.showPassword = !this.showPassword;
  // }
}
