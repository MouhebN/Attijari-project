import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router'; 
@Component({
  selector: 'app-forget-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './forget-password.component.html',
  styleUrls: ['./forget-password.component.scss']
})
export default class ForgetPasswordComponent {
  form: FormGroup;
  submitted = false;
  successMessage = '';
  errorMessage = '';

  constructor(private fb: FormBuilder, private authService: AuthService,  private router: Router) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  get f() {
    return this.form.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    this.successMessage = '';
    this.errorMessage = '';

    if (this.form.invalid) return;

    const email = this.f['email'].value;

    this.authService.resetPassword(email).subscribe({
      next: (res) => {
        this.successMessage = 'Un lien de réinitialisation a été envoyé.';
        this.router.navigate(['/guest/reset-password']);
      },
      error: (err) => {

        this.errorMessage = err.status === 404
          ? 'Adresse e-mail non reconnue.'
          : 'Erreur lors de la réinitialisation.';
      }
    });
  }
}
