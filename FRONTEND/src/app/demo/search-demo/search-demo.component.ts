import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvancedSearchComponent, SearchFilters } from '../../theme/shared/components/advanced-search/advanced-search.component';
import { SearchService, FileData } from '../../theme/shared/services/search.service';

@Component({
  selector: 'app-search-demo',
  standalone: true,
  imports: [CommonModule, AdvancedSearchComponent],
  template: `
    <div class="container mt-4">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h4 class="mb-0">
                <i class="ti ti-search me-2"></i>
                Démonstration de la Recherche Avancée
              </h4>
            </div>
            <div class="card-body">
              <!-- Composant de recherche avancée -->
              <app-advanced-search 
                [showDateRange]="true"
                (filtersChanged)="onFiltersChanged($event)">
              </app-advanced-search>

              <!-- Résultats de la recherche -->
              <div class="results-section mt-4">
                <h5>Résultats de la recherche</h5>
                <div class="table-responsive">
                  <table class="table table-striped">
                    <thead>
                      <tr>
                        <th>ID</th>
                        <th>Type</th>
                        <th>Code</th>
                        <th>Nom</th>
                        <th>Date</th>
                        <th>Statut</th>
                        <th>Nombre</th>
                        <th>Montant</th>
                        <th>Utilisateur</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr *ngFor="let file of filteredData">
                        <td>{{ file.id }}</td>
                        <td>
                          <span class="badge" [ngClass]="getTypeBadgeClass(file.type)">
                            {{ getTypeLabel(file.type) }}
                          </span>
                        </td>
                        <td>
                          <span class="badge bg-info">
                            {{ file.code }}
                          </span>
                        </td>
                        <td>{{ file.name }}</td>
                        <td>{{ file.date | date:'dd/MM/yyyy' }}</td>
                        <td>
                          <span class="badge" [ngClass]="getStatusBadgeClass(file.status)">
                            {{ getStatusLabel(file.status) }}
                          </span>
                        </td>
                        <td>{{ file.nombre }}</td>
                        <td>{{ file.montant | number:'1.2-2' }} DT</td>
                        <td>{{ file.user }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>

                <!-- Statistiques -->
                <div class="stats-section mt-3">
                  <div class="row">
                    <div class="col-md-4">
                      <div class="card bg-primary text-white">
                        <div class="card-body">
                          <h6>Total Fichiers</h6>
                          <h3>{{ stats.totalFiles }}</h3>
                        </div>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="card bg-success text-white">
                        <div class="card-body">
                          <h6>Total Nombre</h6>
                          <h3>{{ stats.totalNombre }}</h3>
                        </div>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="card bg-info text-white">
                        <div class="card-body">
                          <h6>Total Montant</h6>
                          <h3>{{ stats.totalMontant | number:'1.2-2' }} DT</h3>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class SearchDemoComponent implements OnInit {
  filteredData: FileData[] = [];
  stats = { totalFiles: 0, totalNombre: 0, totalMontant: 0 };

  constructor(private searchService: SearchService) {}

  ngOnInit() {
    // S'abonner aux données filtrées
    this.searchService.fileData$.subscribe(data => {
      this.filteredData = data;
      this.stats = this.searchService.getFilteredStats();
    });
  }

  onFiltersChanged(filters: SearchFilters) {
    this.searchService.updateSearchFilters(filters);
  }

  getTypeLabel(type: string): string {
    const typeLabels: { [key: string]: string } = {
      'cheque': 'Chèque',
      'effet': 'Effet',
      'prelevement': 'Prélèvement',
      'virement': 'Virement'
    };
    return typeLabels[type] || type;
  }

  getStatusLabel(status: string): string {
    const statusLabels: { [key: string]: string } = {
      'en_cours': 'En cours',
      'valide': 'Validé',
      'rejete': 'Rejeté',
      'erreur': 'Erreur'
    };
    return statusLabels[status] || status;
  }

  getTypeBadgeClass(type: string): string {
    const badgeClasses: { [key: string]: string } = {
      'cheque': 'bg-primary',
      'effet': 'bg-success',
      'prelevement': 'bg-warning',
      'virement': 'bg-info'
    };
    return badgeClasses[type] || 'bg-secondary';
  }

  getStatusBadgeClass(status: string): string {
    const badgeClasses: { [key: string]: string } = {
      'en_cours': 'bg-warning',
      'valide': 'bg-success',
      'rejete': 'bg-danger',
      'erreur': 'bg-danger'
    };
    return badgeClasses[status] || 'bg-secondary';
  }
} 