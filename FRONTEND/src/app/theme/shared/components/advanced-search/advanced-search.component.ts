import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

export interface SearchFilters {
  nomFichier?: string;
  nombre?: number;
  montant?: number;
  dateRange?: {
    start: string;
    end: string;
  };
}

@Component({
  selector: 'app-advanced-search',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './advanced-search.component.html',
  styleUrls: ['./advanced-search.component.scss']
})
export class AdvancedSearchComponent {
  @Input() showDateRange: boolean = true;
  @Output() filtersChanged = new EventEmitter<SearchFilters>();

  isExpanded: boolean = false;
  filters: SearchFilters = {
    nomFichier: '',
    nombre: undefined,
    montant: undefined,
    dateRange: {
      start: '',
      end: ''
    }
  };

  // Options pour les codes de fichiers
  fileCodes = [
    { value: '30', label: 'Fichier n° 30', icon: 'ti ti-file-text' },
    { value: '31', label: 'Fichier n° 31', icon: 'ti ti-file-text' },
    { value: '32', label: 'Fichier n° 32', icon: 'ti ti-file-text' },
    { value: '33', label: 'Fichier n° 33', icon: 'ti ti-file-text' }
  ];

  // Méthode pour obtenir les codes disponibles
  getAvailableFileCodes() {
    return this.fileCodes;
  }

  // Méthode pour basculer l'expansion de la recherche
  toggleExpansion() {
    this.isExpanded = !this.isExpanded;
  }

  // Méthode pour appliquer les filtres
  applyFilters() {
    this.filtersChanged.emit(this.filters);
  }

  // Méthode pour réinitialiser les filtres
  resetFilters() {
    this.filters = {
      nomFichier: '',
      nombre: undefined,
      montant: undefined,
      dateRange: {
        start: '',
        end: ''
      }
    };
    this.filtersChanged.emit(this.filters);
  }

  // Méthode pour détecter les changements de filtres
  onFilterChange() {
    this.applyFilters();
  }

  // Méthode pour effacer un filtre spécifique
  clearFilter(filterName: keyof SearchFilters) {
    delete this.filters[filterName];
    this.applyFilters();
  }

  // Méthodes getter pour simplifier les expressions dans le template
  getNomFichier(): string {
    return this.filters.nomFichier || '';
  }

  getNombre(): number | undefined {
    return this.filters.nombre;
  }

  getMontant(): number | undefined {
    return this.filters.montant;
  }

  getDateRangeStart(): string {
    return this.filters.dateRange?.start || '';
  }

  getDateRangeEnd(): string {
    return this.filters.dateRange?.end || '';
  }

  setDateRangeStart(value: string) {
    if (!this.filters.dateRange) {
      this.filters.dateRange = { start: '', end: '' };
    }
    this.filters.dateRange.start = value;
  }

  setDateRangeEnd(value: string) {
    if (!this.filters.dateRange) {
      this.filters.dateRange = { start: '', end: '' };
    }
    this.filters.dateRange.end = value;
  }
} 