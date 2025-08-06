import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { SearchFilters } from '../components/advanced-search/advanced-search.component';

export interface FileData {
  id: number;
  type: string;
  code: string;
  name: string;
  date: string;
  status: string;
  nombre?: number;
  montant?: number;
  user?: string;
}

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private searchFiltersSubject = new BehaviorSubject<SearchFilters>({});
  private fileDataSubject = new BehaviorSubject<FileData[]>([]);

  // Observable pour les filtres de recherche
  searchFilters$ = this.searchFiltersSubject.asObservable();
  
  // Observable pour les données de fichiers
  fileData$ = this.fileDataSubject.asObservable();

  // Données simulées pour les fichiers
  private mockFileData: FileData[] = [
    {
      id: 1,
      type: 'cheque',
      code: '30',
      name: 'cheques_202507.ENV',
      date: '2024-01-15',
      status: 'en_cours',
      nombre: 150,
      montant: 25000.50,
      user: 'ala'
    },
    {
      id: 2,
      type: 'cheque',
      code: '31',
      name: 'cheques_202507.RCP',
      date: '2024-01-15',
      status: 'valide',
      nombre: 89,
      montant: 15680.75,
      user: 'fedi'
    },
    {
      id: 3,
      type: 'cheque',
      code: '32',
      name: 'cheques_202507.ENV',
      date: '2024-01-15',
      status: 'en_cours',
      nombre: 234,
      montant: 45600.25,
      user: 'ala'
    },
    {
      id: 4,
      type: 'cheque',
      code: '33',
      name: 'cheques_202507.RCP',
      date: '2024-01-15',
      status: 'rejete',
      nombre: 67,
      montant: 12340.00,
      user: 'fedi'
    },
    {
      id: 5,
      type: 'effet',
      code: '40',
      name: 'effets_202507.ENV',
      date: '2024-01-15',
      status: 'valide',
      nombre: 320,
      montant: 67890.30,
      user: 'ala'
    },
    {
      id: 6,
      type: 'effet',
      code: '41',
      name: 'effets_202507.RCP',
      date: '2024-01-15',
      status: 'en_cours',
      nombre: 180,
      montant: 34500.00,
      user: 'fedi'
    },
    {
      id: 7,
      type: 'prelevement',
      code: '20',
      name: 'prelevements_202507.ENV',
      date: '2024-01-15',
      status: 'valide',
      nombre: 95,
      montant: 18900.00,
      user: 'ala'
    },
    {
      id: 8,
      type: 'virement',
      code: '10',
      name: 'virements_202507.RCP',
      date: '2024-01-15',
      status: 'en_cours',
      nombre: 120,
      montant: 45600.00,
      user: 'fedi'
    }
  ];

  constructor() {
    this.fileDataSubject.next(this.mockFileData);
  }

  // Méthode pour mettre à jour les filtres de recherche
  updateSearchFilters(filters: SearchFilters) {
    this.searchFiltersSubject.next(filters);
    this.applyFilters(filters);
  }

  // Méthode pour appliquer les filtres aux données
  private applyFilters(filters: SearchFilters) {
    let filteredData = [...this.mockFileData];

    // Filtrer par nom de fichier
    if (filters.nomFichier) {
      filteredData = filteredData.filter(file => 
        file.name.toLowerCase().includes(filters.nomFichier!.toLowerCase())
      );
    }

    // Filtrer par nombre
    if (filters.nombre !== undefined && filters.nombre !== null) {
      filteredData = filteredData.filter(file => 
        file.nombre === filters.nombre
      );
    }

    // Filtrer par montant
    if (filters.montant !== undefined && filters.montant !== null) {
      filteredData = filteredData.filter(file => 
        file.montant === filters.montant
      );
    }

    // Filtrer par plage de dates
    if (filters.dateRange?.start || filters.dateRange?.end) {
      filteredData = filteredData.filter(file => {
        const fileDate = new Date(file.date);
        const startDate = filters.dateRange?.start ? new Date(filters.dateRange.start) : null;
        const endDate = filters.dateRange?.end ? new Date(filters.dateRange.end) : null;
        
        if (startDate && endDate) {
          return fileDate >= startDate && fileDate <= endDate;
        } else if (startDate) {
          return fileDate >= startDate;
        } else if (endDate) {
          return fileDate <= endDate;
        }
        return true;
      });
    }

    // Mettre à jour les données filtrées
    this.fileDataSubject.next(filteredData);
  }

  // Méthode pour obtenir les statistiques des données filtrées
  getFilteredStats(): { totalFiles: number; totalNombre: number; totalMontant: number } {
    const currentData = this.fileDataSubject.value;
    return {
      totalFiles: currentData.length,
      totalNombre: currentData.reduce((sum, file) => sum + (file.nombre || 0), 0),
      totalMontant: currentData.reduce((sum, file) => sum + (file.montant || 0), 0)
    };
  }

  // Méthode pour réinitialiser les filtres
  resetFilters() {
    this.searchFiltersSubject.next({});
    this.fileDataSubject.next(this.mockFileData);
  }

  // Méthode pour obtenir les données par type de fichier
  getDataByFileType(type: string): FileData[] {
    return this.mockFileData.filter(file => file.type === type);
  }

  // Méthode pour obtenir les données par code de fichier
  getDataByFileCode(code: string): FileData[] {
    return this.mockFileData.filter(file => file.code === code);
  }
} 