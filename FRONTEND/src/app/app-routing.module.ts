import { NgModule, inject } from '@angular/core';
import { RouterModule, Routes, CanActivateFn, Router } from '@angular/router';

// Layouts
import { AdminComponent } from './theme/layout/admin/admin.component';
import { GuestComponent } from './theme/layout/guest/guest.component';

// Guard pour vérifier que l'utilisateur est connecté
export const userGuard: CanActivateFn = () => {
  const router = inject(Router);
  const userStr = localStorage.getItem('user');
  if (!userStr) {
    return router.parseUrl('/guest/login');
  }
  // Tu peux ajouter une vérification plus poussée ici (token, expiration, etc)
  return true;
};

// Guard pour vérifier que l'utilisateur est admin
export const adminGuard: CanActivateFn = () => {
  const router = inject(Router);
  const userStr = localStorage.getItem('user');
  if (!userStr) {
    return router.parseUrl('/guest/login');
  }
  const user = JSON.parse(userStr);
  const roles = Array.isArray(user.role) ? user.role : [user.role];

  if (roles.includes('ADMIN')) {
    return true;
  } else {
    // Rediriger vers tableau de bord ou une page accès refusé
    return router.parseUrl('/default');
  }
};

const routes: Routes = [
  {
    path: '',
    component: AdminComponent,
    children: [
      {
        path: '',
        redirectTo: '/default',
        pathMatch: 'full'
      },
      {
        path: 'default',
        loadComponent: () => import('./demo/dashboard/default/default.component').then((c) => c.DefaultComponent),
        canActivate: [userGuard]
      },

      {
        path: 'sample-page',
        loadComponent: () => import('./demo/other/sample-page/sample-page.component'),
        canActivate: [userGuard]
      },
      {
        path: 'cheque/30',
        loadComponent: () => import('./demo/cheque/cheque30/cheque30.component').then(m => m.Cheque30Component),
        canActivate: [userGuard]
      },
      {
        path: 'cheque/31',
        loadComponent: () => import('./demo/cheque/cheque31/cheque31.component').then(m => m.Cheque31Component),
        canActivate: [userGuard]
      },
      {
        path: 'cheque/32',
        loadComponent: () => import('./demo/cheque/cheque32/cheque32.component').then(m => m.Cheque32Component),
        canActivate: [userGuard]
      },
      {
        path: 'cheque/33',
        loadComponent: () => import('./demo/cheque/cheque33/cheque33.component').then(m => m.Cheque33Component),
        canActivate: [userGuard]
      },
      {
        path: 'prlv/20',
        loadComponent: () => import('./demo/prlv/prlv20/prlv20.component').then(m => m.Prlv20Component),
        canActivate: [userGuard]
      },
      {
        path: 'effet/41',
        loadComponent: () => import('./demo/effet/effet41/effet41.component').then(m => m.Effet41Component),
        canActivate: [userGuard]
      },
      {
        path: 'effet/42',
        loadComponent: () => import('./demo/effet/effet42/effet42.component').then(m => m.Effet42Component),
        canActivate: [userGuard]
      },
      {
        path: 'virement/10',
        loadComponent: () => import('./demo/virement/virement10/virement10.component').then(m => m.Virement10Component),
        canActivate: [userGuard]
      },
      {
        path: 'gestion-utilisateur',
        loadComponent: () => import('./demo/pages/gestion-utilisateur/gestion-utilisateur.component').then(m => m.GestionUtilisateurComponent),
        canActivate: [adminGuard]  // <-- Seul admin peut accéder
      },
      {
        path: 'suivi-ctr-bo',
        loadComponent: () => import('./demo/suivi-ctr-bo/suivi-ctr-bo.component').then(m => m.SuiviCtrBoComponent),
        canActivate: [adminGuard]  // <-- Seul admin peut accéder
      },

      {
        path: 'logout',
        redirectTo: '/guest/login',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: '',
    component: GuestComponent,
    children: [
      {
        path: 'guest',
        loadChildren: () => import('./demo/pages/authentication/authentication.module').then((m) => m.AuthenticationModule)
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
