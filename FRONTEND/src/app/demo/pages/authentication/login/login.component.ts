import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';
import { NavigationService } from '../../../../theme/layout/admin/navigation/navigation.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export default class LoginComponent {
  loginForm: FormGroup;
  submitted = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authservice: AuthService,
    private navigationService: NavigationService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required]],
      rememberMe: [false]
    });
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.loginForm.invalid) {
      console.warn('Formulaire de connexion invalide');
      return;
    }

    const user = {
      email: this.f['email'].value,
      username: this.f['email'].value, // ou extraire le username d'un autre champ si besoin
      password: this.f['password'].value
    };

    this.authservice.login(user).subscribe({
      next: (response) => {
        console.log('Connexion réussie');
        console.log('Réponse complète:', response);

        // ✅ Sauvegarder le token et l'utilisateur dans localStorage
        if (response && response.user) {
          // ✅ Forcer le rôle ADMIN si l'utilisateur est "ala"
          if (response.user.username === 'ala') {
            response.user.role = 'ADMIN';
            console.log('🔧 Correction: Forcer le rôle ADMIN pour ala');
          }

          localStorage.setItem('token', response.token);
          localStorage.setItem('user', JSON.stringify(response.user));

          // ✅ Mettre à jour la navigation selon le rôle
          this.navigationService.updateNavigation();

          // ✅ Rediriger vers la page par défaut
          this.router.navigate(['/default']);
        } else {
          console.error();
          alert('Erreur : format de réponse invalide du serveur.');
        }
      },
      error: (err) => {
        console.error();
        console.error();
        alert('Erreur : ' + (err.error?.message || 'Identifiants invalides ou serveur inaccessible'));
      }
    });
  }
}
