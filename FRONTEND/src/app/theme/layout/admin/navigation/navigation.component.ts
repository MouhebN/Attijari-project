
// navigation.component.ts
import { CommonModule } from '@angular/common';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NavContentComponent } from './nav-content/nav-content.component';
import { NavigationItem, NavigationItems } from './navigation';

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [NavContentComponent, CommonModule, RouterModule],
  templateUrl: './navigation.component.html',
  styleUrl: './navigation.component.scss'
})
export class NavigationComponent implements OnInit {
  @Output() NavCollapsedMob = new EventEmitter<void>();
  @Output() SubmenuCollapse = new EventEmitter<void>();

  navCollapsedMob = false;
  windowWidth = window.innerWidth;
  themeMode!: string;
  navigations: NavigationItem[] = [];

  ngOnInit() {
    this.filterNavigationByRole();
  }

  // ✅ Filtrer la navigation selon le rôle de l'utilisateur
  private filterNavigationByRole() {
    const userStr = localStorage.getItem('user');
    if (!userStr) {
      this.navigations = NavigationItems.filter(item => !item.role);
      return;
    }

    try {
      const user = JSON.parse(userStr);
      const userRole = user.role || 'SIMPLE_USER';

      this.navigations = NavigationItems.filter(item => {
        // Si l'élément n'a pas de rôle spécifié, il est visible pour tous
        if (!item.role) {
          return true;
        }

        // Si l'élément a un rôle spécifié, vérifier si l'utilisateur a ce rôle
        return item.role.includes(userRole);
      });

      console.log('Navigation filtrée pour le rôle:', userRole);
      console.log('Éléments visibles:', this.navigations.map(item => item.title));
    } catch (error) {
      console.error();
      this.navigations = NavigationItems.filter(item => !item.role);
    }
  }

  navCollapseMob() {
    if (this.windowWidth < 1025) {
      this.NavCollapsedMob.emit();
    }
  }

  navSubmenuCollapse() {
    document.querySelector('app-navigation.coded-navbar')?.classList.add('coded-trigger');
  }
}
