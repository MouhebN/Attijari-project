import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export default class RegisterComponent {

  registerForm: FormGroup;
  submitted = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authservice: AuthService
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      terms: [false, Validators.requiredTrue]
    });
  }

  get f() {
    return this.registerForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.registerForm.invalid) {
      console.warn('Formulaire invalide');
      return;
    }

    const user = {
      username: this.f['username'].value,
      email: this.f['email'].value,
      password: this.f['password'].value
    };

    // ✅ Vérifier d'abord si l'utilisateur existe
    this.authservice.checkUserExists(user.username, user.email).subscribe({
      next: (response) => {
        // Utilisateur disponible, procéder à l'enregistrement
        this.registerUser(user);
      },
      error: (err) => {
        console.error();
        let errorMessage = 'Erreur lors de la vérification';

        if (err.status === 400) {
          errorMessage = err.error?.message || 'Utilisateur déjà existant';
        } else if (err.status === 0) {
          errorMessage = 'Impossible de se connecter au serveur. Vérifiez que le backend est démarré.';
        }

        alert(errorMessage);
      }
    });
  }

  private registerUser(user: any): void {
    // ✅ Appel API backend + redirection
    this.authservice.register(user).subscribe({
      next: (response) => {
        console.log('Enregistrement réussi:', response);
        alert('Compte créé avec succès ! Vous pouvez maintenant vous connecter.');
        this.router.navigate(['/guest/login']);
      },
      error: (err) => {
        console.error();

        let errorMessage = 'Erreur lors de l\'enregistrement';

        if (err.status === 400) {
          if (err.error?.error) {
            errorMessage = err.error.error;
          } else if (err.error?.message) {
            errorMessage = err.error.message;
          } else {
            errorMessage = 'Données invalides ou utilisateur déjà existant';
          }
        } else if (err.status === 0) {
          errorMessage = 'Impossible de se connecter au serveur. Vérifiez que le backend est démarré.';
        } else {
          errorMessage = err.error?.message || 'Erreur serveur';
        }

        alert(errorMessage);
      }
    });
  }
}
