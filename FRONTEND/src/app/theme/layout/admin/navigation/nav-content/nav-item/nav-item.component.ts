// Angular import
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

// Project import
import { NavigationItem } from '../../navigation';
import { AjouterFichierService } from 'src/app/demo/ajouter-fichier/ajouter-fichier.service';

@Component({
  selector: 'app-nav-item',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './nav-item.component.html',
  styleUrl: './nav-item.component.scss'
})
export class NavItemComponent {
  // public props
  @Input() item!: NavigationItem;

  constructor(private ajouterFichierService: AjouterFichierService) {}

  // public method
  closeOtherMenu(event: MouseEvent) {
    // Fermer tous les menus principaux ouverts
    const sections = document.querySelectorAll('.coded-hasmenu');
    for (let i = 0; i < sections.length; i++) {
      sections[i].classList.remove('active');
      sections[i].classList.remove('coded-trigger');
    }
  }

  // Méthode pour gérer le clic sur l'item
  onItemClick(event: MouseEvent) {
    // Si c'est l'item "Ajouter un fichier", ouvrir le modal
    if (this.item.id === 'ajouter-fichier') {
      event.preventDefault();
      this.ajouterFichierService.openModal();
    } else {
      // Sinon, comportement normal
      this.closeOtherMenu(event);
    }
  }
}
