// Angular import
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';

// third party import
import { SharedModule } from 'src/app/theme/shared/shared.module';

@Component({
  selector: 'app-nav-right',
  imports: [RouterModule, SharedModule],
  templateUrl: './nav-right.component.html',
  styleUrls: ['./nav-right.component.scss']
})
export class NavRightComponent implements OnInit{
  userJson: any = null;
  constructor(private router: Router) {}
  ngOnInit(): void {
    const userStr = localStorage.getItem('user');
    this.userJson = userStr ? JSON.parse(userStr) : null;
  }
  onLogout() {
    // Ici, ajouter la logique de déconnexion réelle si besoin
    this.router.navigate(['/guest/login']);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }
}
