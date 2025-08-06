import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { AjouterFichierService } from './ajouter-fichier.service';
import { AjouterFichierService as FichierService } from './ajouter-fichier.services';
import { AuthService } from '../pages/authentication/auth.service';
import { Fichier } from './fichier.model';

@Component({
  selector: 'app-ajouter-fichier',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ajouter-fichier.component.html',
  styleUrls: ['./ajouter-fichier.component.scss']
})
export class AjouterFichierComponent implements OnInit, OnDestroy {
  isOpen: boolean = false;
  private subscription: Subscription = new Subscription();

  // Propriétés du formulaire
  _typeFichier: string = '';
  get typeFichier() {
    return this._typeFichier;
  }
  set typeFichier(val: string) {
    this._typeFichier = val;
    // Auto-select codeFichier if only one code is available
    const codes = this.codesFichier[val] || [];
    if (codes.length === 1) {
      this.codeFichier = codes[0].value;
    } else {
      this.codeFichier = '';
    }
  }
  codeFichier: string = '';
  sens: string = '';
  codeEnregistrement: string = '';
  formatFichier: string = '';
  nomFichier: string = '';
  nombre: number = 0;
  montant: number = 0;

  // Options pour les champs
  typesFichier = [
    { value: 'cheque', label: 'Chèque', icon: 'ti ti-currency-dollar' },
    { value: 'effet', label: 'Effet', icon: 'ti ti-file-invoice' },
    { value: 'prelevement', label: 'Prélèvement', icon: 'ti ti-credit-card' },
    { value: 'virement', label: 'Virement', icon: 'ti ti-arrows-double-ne-sw' }
  ];

  codesFichier: { [key: string]: Array<{value: string, label: string}> } = {
    cheque: [
      { value: '30', label: '30' },
      { value: '31', label: '31' },
      { value: '32', label: '32' },
      { value: '33', label: '33' }
    ],
    effet: [
      { value: '41', label: '41' },
      { value: '42', label: '42' }
    ],
    prelevement: [
      { value: '20', label: '20' }
    ],
    virement: [
      { value: '10', label: '10' }
    ]
  };

  sensOptions = [
    { value: 'emis', label: 'Émis', icon: 'ti ti-arrow-up' },
    { value: 'recu', label: 'Reçu', icon: 'ti ti-arrow-down' }
  ];

  codeEnregistrementOptions = [
    { value: '21', label: '21 - Présentation', icon: 'ti ti-check' },
    { value: '22', label: '22 - Rejet', icon: 'ti ti-x' }
  ];

  formatFichierOptions = [
    { value: 'env', label: '.ENV', icon: 'ti ti-file' },
    { value: 'rcp', label: '.RCP', icon: 'ti ti-file' }
  ];

  constructor(
    private ajouterFichierService: AjouterFichierService,
    private fichierService: FichierService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.subscription = this.ajouterFichierService.isModalOpen$.subscribe(
      isOpen => this.isOpen = isOpen
    );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  // Méthode pour obtenir les codes selon le type sélectionné
  getCodesDisponibles() {
    if (this.typeFichier && this.codesFichier[this.typeFichier]) {
      return this.codesFichier[this.typeFichier];
    }
    return [];
  }

  // Méthode pour fermer le modal
  onClose() {
    this.ajouterFichierService.closeModal();
  }

  // Méthode pour fermer en cliquant sur l'arrière-plan
  onBackdropClick(event: Event) {
    if (event.target === event.currentTarget) {
      this.onClose();
    }
  }

  // Méthode pour soumettre le formulaire
  onSubmit() {
    console.log('=== DÉBUT SOUMISSION FORMULAIRE ===');

    // Validation du nom de fichier
    const nomFichierValide = /^[a-zA-Z0-9_-]{5,}$/.test(this.nomFichier);
    if (!nomFichierValide) {
      alert('Le nom du fichier doit comporter au moins 5 caractères (lettres, chiffres, tirets ou underscores).');
      return;
    }

    // Vérifier l'utilisateur connecté
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser || !currentUser.id) {
      console.error();
      alert('Erreur: Utilisateur non connecté');
      return;
    }

    console.log('✅ Utilisateur connecté:', currentUser);

    // Déterminer la nature du fichier basée sur le type
    const natureFichier = this.getNatureFichier(this.typeFichier);
    console.log('✅ Nature du fichier déterminée:', natureFichier);

    // Créer l'objet Fichier complet
    const fichier: Fichier = {
      typeFichier: this.typeFichier,
      codeValeur: this.codeFichier,
      sens: this.sens,
      codEn: this.codeEnregistrement,
      nomFichier: this.nomFichier,
      natureFichier: natureFichier,
      formatFichier: this.formatFichier,
      nombre: this.nombre,
      montant: this.montant,
      user: { id: currentUser.id },
      createdAt: null,
      updatedAt: null
    };

    console.log('📋 Fichier à envoyer (JSON complet):', JSON.stringify(fichier, null, 2));
    console.log('📋 Structure de validation:', {
      typeFichier: fichier.typeFichier,
      codeValeur: fichier.codeValeur,
      sens: fichier.sens,
      codEn: fichier.codEn,
      nomFichier: fichier.nomFichier,
      natureFichier: fichier.natureFichier,
      formatFichier: fichier.formatFichier,
      user: fichier.user
    });

    // Validation des champs requis côté client
    if (!fichier.typeFichier || !fichier.codeValeur || !fichier.nomFichier || !fichier.natureFichier) {
      console.error();
      alert('Veuillez remplir tous les champs obligatoires.');
      return;
    }

    console.log('✅ Validation côté client réussie');

    // Appel au backend pour créer le fichier
    console.log('🚀 Envoi vers le backend...');
    this.fichierService.createFichier(fichier).subscribe({
      next: (response) => {
        console.log('✅ Succès - Réponse du serveur:', response);
        alert('Fichier créé avec succès !');
        this.resetForm();
        this.onClose();
      },
      error: (err) => {
        console.error();
        console.error();
        console.error();
        if (err.error) {
          console.error();
          console.error();
        }
        console.error();

        let errorMessage = 'Erreur lors de la création du fichier';
        if (err.error?.message) {
          errorMessage += ': ' + err.error.message;
        } else if (err.message) {
          errorMessage += ': ' + err.message;
        }
        alert(errorMessage);
      }
    });
  }

  // Méthode pour déterminer la nature du fichier
  getNatureFichier(type: string): string {
    const natureMap: { [key: string]: string } = {
      'cheque': 'Chèque',
      'effet': 'Effet',
      'prelevement': 'Prélèvement',
      'virement': 'Virement'
    };
    const nature = natureMap[type] || '';
    console.log('🔍 Nature déterminée pour type', type, ':', nature);
    return nature;
  }

  // Méthode pour réinitialiser le formulaire
  resetForm() {
    this.typeFichier = '';
    this.codeFichier = '';
    this.sens = '';
    this.codeEnregistrement = '';
    this.formatFichier = '';
    this.nomFichier = '';
    this.nombre = 0;
    this.montant = 0;
  }
}
