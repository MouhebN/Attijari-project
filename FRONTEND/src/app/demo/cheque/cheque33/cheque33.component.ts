import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  AdvancedDataTableComponent,
  DataTableOptions
} from '../../../theme/shared/components/advanced-datatable/advanced-datatable.component';
import { AdvancedSearchComponent, SearchFilters } from '../../../theme/shared/components/advanced-search/advanced-search.component';
import { FichierService } from '../../fichier.service';
import { FichierUpdateDTO } from '../../fichier.model';

export interface Fichier {
  id: number;
  codEn: string;
  codeValeur: string;
  createdAt: string;
  natureFichier: string;
  nomFichier: string;
  sens: string;
  typeFichier: string;
  updatedAt: string;
  nombre?: number;
  montant?: number;
}

@Component({
  selector: 'app-cheque33',
  templateUrl: './cheque33.html',
  styleUrl: './cheque33.scss',
  standalone: true,
  imports: [CommonModule, FormsModule, AdvancedSearchComponent, AdvancedDataTableComponent]
})
export class Cheque33Component implements OnInit {
  fichiers: Fichier[] = [];
  datatableOptions: DataTableOptions;
  fichierSelectionne: Fichier | null = null;
  isEditModalOpen = false;
  nomFichierBase = '';
  extensionFichier = '';

  // Filtres simples
  searchNomFichier = '';
  searchNombre = '';
  searchMontant = '';
  searchDateCreation = '';
  searchCodeEN = '';
  searchCode30 = '';

  constructor(private fichierService: FichierService) {}

  ngOnInit(): void {
    this.loadFichiers();
  }

  loadFichiers(): void {
    this.fichierService.getFichiersByNatureAndCodeValeur('Chèque', '33').subscribe({
      next: (data: Fichier[]) => {
        this.fichiers = data;
        this.datatableOptions = { ...this.buildDatatableOptions(data) };
      },
      error: (err) => {
        console.error();
      }
    });
  }

  buildDatatableOptions(data: Fichier[]): DataTableOptions {
    return {
      columns: [
        { title: 'Code EN', data: 'COD_EN', width: '8%' },
        { title: 'Code Valeur', data: 'CODE_VALEUR', width: '10%' },
        { title: 'Date de création', data: 'CREATED_AT', width: '12%' },
        { title: 'Nature du fichier', data: 'NATURE_FICHIER', width: '12%' },
        { title: 'Nom du fichier', data: 'NOM_FICHIER', width: '18%' },
        { title: 'Sens', data: 'SENS', width: '6%' },
        { title: 'Nombre', data: 'NOMBRE', width: '8%' },
        { title: 'Montant', data: 'MONTANT', width: '10%' },
        { title: 'Type de fichier', data: 'TYPE_FICHIER', width: '10%' },
        { title: 'Date de mise à jour', data: 'UPDATED_AT', width: '12%' }
      ],
      data: data.map((f) => ({
        ...f,
        COD_EN: f.codEn,
        CODE_VALEUR: f.codeValeur,
        CREATED_AT: f.createdAt,
        NATURE_FICHIER: f.natureFichier,
        NOM_FICHIER: f.nomFichier,
        SENS: f.sens,
        NOMBRE: f.nombre,
        MONTANT: f.montant,
        TYPE_FICHIER: f.typeFichier,
        UPDATED_AT: f.updatedAt
      })),
      pageLength: 10,
      responsive: true,
      search: true,
      ordering: true,
      info: true,
      paging: true,
      lengthChange: true,
      dom: 'lfrtip'
    };
  }

  filterData() {
    let filteredData = [...this.fichiers];

    if (this.searchNomFichier) {
      filteredData = filteredData.filter((f) => f.nomFichier.toLowerCase().includes(this.searchNomFichier.toLowerCase()));
    }
    if (this.searchNombre) {
      const val = parseInt(this.searchNombre);
      if (!isNaN(val)) {
        filteredData = filteredData.filter((f) => f.nombre === val);
      }
    }
    if (this.searchMontant) {
      const val = parseFloat(this.searchMontant);
      if (!isNaN(val)) {
        filteredData = filteredData.filter((f) => f.montant === val);
      }
    }
    if (this.searchDateCreation) {
      filteredData = filteredData.filter((f) => f.createdAt.includes(this.searchDateCreation));
    }
    if (this.searchCodeEN) {
      filteredData = filteredData.filter((f) => f.codEn.toLowerCase().includes(this.searchCodeEN.toLowerCase()));
    }
    if (this.searchCode30) {
      filteredData = filteredData.filter((f) => f.codeValeur.toLowerCase().includes(this.searchCode30.toLowerCase()));
    }

    this.datatableOptions.data = filteredData;
  }

  clearAllFilters() {
    this.searchNomFichier = '';
    this.searchNombre = '';
    this.searchMontant = '';
    this.searchDateCreation = '';
    this.searchCodeEN = '';
    this.searchCode30 = '';
    this.datatableOptions.data = [...this.fichiers];
  }

  getStats() {
    const totalFichiers = this.fichiers.length;
    const totalNombre = this.fichiers.reduce((sum, f) => sum + (f.nombre || 0), 0);
    const totalMontant = this.fichiers.reduce((sum, f) => sum + (f.montant || 0), 0);
    const emisCount = this.fichiers.filter((f) => f.sens === 'E').length;
    const recuCount = this.fichiers.filter((f) => f.sens === 'R').length;

    return { totalFichiers, totalNombre, totalMontant, emisCount, recuCount };
  }

  modifierFichier(fichier: Fichier) {
    console.log('Ouverture de la modale pour:', fichier); // <-- debug
    this.fichierSelectionne = fichier;
    this.isEditModalOpen = true;
    const parts = fichier.nomFichier.split('.');
    this.extensionFichier = parts.length > 1 ? parts.pop()! : '';
    this.nomFichierBase = parts.join('.');
  }

  fermerEditModal() {
    this.isEditModalOpen = false;
    this.fichierSelectionne = null;
    this.nomFichierBase = '';
    this.extensionFichier = '';
  }

  onEditFichier(fichier: Fichier) {
    this.modifierFichier(fichier);
  }

  onViewFichier(fichier: Fichier) {
    console.log('Voir fichier:', fichier);
  }

  onDeleteFichier(fichier: Fichier) {
    this.supprimerFichier(fichier);
  }

  onSearchFiltersChanged(filters: SearchFilters) {
    this.applyAdvancedFilters(filters);
  }

  private applyAdvancedFilters(filters: SearchFilters) {
    let filteredData = [...this.fichiers];

    if (filters.nomFichier) {
      filteredData = filteredData.filter((f) => f.nomFichier.toLowerCase().includes(filters.nomFichier.toLowerCase()));
    }
    if (filters.nombre !== undefined) {
      filteredData = filteredData.filter((f) => f.nombre === filters.nombre);
    }
    if (filters.montant !== undefined) {
      filteredData = filteredData.filter((f) => f.montant === filters.montant);
    }
    if (filters.dateRange?.start || filters.dateRange?.end) {
      filteredData = filteredData.filter((f) => {
        const fileDate = new Date(f.createdAt);
        const start = filters.dateRange?.start ? new Date(filters.dateRange.start) : null;
        const end = filters.dateRange?.end ? new Date(filters.dateRange.end) : null;
        if (start && end) return fileDate >= start && fileDate <= end;
        if (start) return fileDate >= start;
        if (end) return fileDate <= end;
        return true;
      });
    }

    this.datatableOptions.data = filteredData;
  }

  supprimerFichier(fichier: Fichier) {
    if (!fichier?.id) return;

    if (confirm('Voulez-vous vraiment supprimer ce fichier ?')) {
      this.fichierService.deleteFichier(fichier.id).subscribe({
        next: () => {
          this.loadFichiers();
        },
        error: (err) => console.error()
      });
    }
  }

  sauvegarderModificationsFichier() {
    if (!this.fichierSelectionne) return;

    const nomComplet = this.extensionFichier ? `${this.nomFichierBase}.${this.extensionFichier}` : this.nomFichierBase;

    const dto: FichierUpdateDTO = {
      nomFichier: nomComplet,
      montant: this.fichierSelectionne.montant,
      typeFichier: this.fichierSelectionne.typeFichier,
      codeValeur: this.fichierSelectionne.codeValeur,
      codEn: this.fichierSelectionne.codEn,
      nombre: this.fichierSelectionne.nombre
    };

    this.fichierService.updateFichier(this.fichierSelectionne.id, dto).subscribe({
      next: () => {
        this.fermerEditModal();
        this.loadFichiers();
      },
      error: (err) => console.error()
    });
  }
}
