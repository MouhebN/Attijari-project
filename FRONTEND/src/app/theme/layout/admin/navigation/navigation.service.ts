import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { NavigationItem, NavigationItems } from './navigation';

@Injectable({
  providedIn: 'root'
})
export class NavigationService {
  private navigationSubject = new BehaviorSubject<NavigationItem[]>([]);
  public navigation$ = this.navigationSubject.asObservable();

  constructor() {
    this.updateNavigation();
  }

  // ✅ Mettre à jour la navigation selon le rôle de l'utilisateur
  updateNavigation(): void {
    console.log('🔄 Début de la mise à jour de la navigation');

    const userStr = localStorage.getItem('user');
    console.log('📦 Données utilisateur dans localStorage:', userStr);

    if (!userStr) {
      console.log('⚠️ Aucun utilisateur connecté, affichage navigation publique');
      this.navigationSubject.next(NavigationItems.filter(item => !item.role));
      return;
    }

    try {
      const user = JSON.parse(userStr);
      const userRole = user.role || 'SIMPLE_USER';

      console.log('👤 Utilisateur connecté:', user.username);
      console.log('🎭 Rôle de l\'utilisateur:', userRole);

      // ✅ Forcer le rôle ADMIN si l'utilisateur est "ala"
      if (user.username === 'ala' && userRole !== 'ADMIN') {
        console.log('🔧 Correction: Forcer le rôle ADMIN pour ala');
        user.role = 'ADMIN';
        localStorage.setItem('user', JSON.stringify(user));
      }

      const filteredNavigation = NavigationItems.filter(item => {
        // Si l'élément n'a pas de rôle spécifié, il est visible pour tous
        if (!item.role) {
          console.log('✅ Élément visible pour tous:', item.title);
          return true;
        }

        // Si l'élément a un rôle spécifié, vérifier si l'utilisateur a ce rôle
        const hasRole = item.role.includes(user.role);
        console.log(`${hasRole ? '✅' : '❌'} Élément "${item.title}" (rôle requis: ${item.role}, rôle utilisateur: ${user.role})`);
        return hasRole;
      });

      this.navigationSubject.next(filteredNavigation);

      console.log('🔄 Navigation mise à jour pour le rôle:', user.role);
      console.log('📋 Éléments visibles:', filteredNavigation.map(item => item.title));

      // Vérifier spécifiquement les menus admin
      const gestionUtilisateur = filteredNavigation.find(item => item.id === 'gestion-utilisateur');
      const suiviCtr = filteredNavigation.find(item => item.id === 'suivi-ctr-bo');

      if (gestionUtilisateur) {
        console.log('✅ Menu "Gestion Utilisateur" est visible');
      } else {
        console.log('❌ Menu "Gestion Utilisateur" n\'est PAS visible');
      }

      if (suiviCtr) {
        console.log('✅ Menu "Suivi CTR/CARTHAGO" est visible');
      } else {
        console.log('❌ Menu "Suivi CTR/CARTHAGO" n\'est PAS visible');
      }
    } catch (error) {
      console.error();
      this.navigationSubject.next(NavigationItems.filter(item => !item.role));
    }
  }

  // ✅ Obtenir la navigation actuelle
  getNavigation(): NavigationItem[] {
    return this.navigationSubject.value;
  }

  // ✅ Vérifier si l'utilisateur a un rôle spécifique
  hasRole(role: string): boolean {
    const userStr = localStorage.getItem('user');
    if (!userStr) return false;

    try {
      const user = JSON.parse(userStr);
      return user.role === role;
    } catch (error) {
      return false;
    }
  }

  // ✅ Vérifier si l'utilisateur est admin
  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  // ✅ Forcer la mise à jour de la navigation
  refreshNavigation(): void {
    this.updateNavigation();
  }
}
