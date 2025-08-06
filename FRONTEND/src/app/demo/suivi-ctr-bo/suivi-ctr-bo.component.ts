 import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

interface CTR {
  id: number;
  fichier?: {
    id: number;
    nomFichier: string;
    nombre: number;
    montant: number;
  };
  carthago?: {
    id: number;
    nomFichier: string;
    contenu: string;
  };
  nombreTotalCarthago: number;
  montantTotalCarthago: number;
  nombreDepCarthago: number;
  montantDepCarthago: number;
  nombreRejCarthago: number;
  montantRejCarthago: number;
  nombreFichier: number;
  montantFichier: number;
  nombreOk: string;
  montantOk: string;
  createdAt: string;
}

interface DashboardStats {
  totalComparaisons: number;
  comparaisonsOK: number;
  comparaisonsKO: number;
  tauxConcordance: number;
  montantTotalCarthago: number;
  montantTotalEncaisse: number;
}

@Component({
  selector: 'app-suivi-ctr-bo',
  standalone: true,
  imports: [
    CommonModule,  // Fournit *ngFor, *ngIf, ngClass, date pipe, number pipe
    FormsModule    // Fournit ngModel pour la liaison bidirectionnelle
  ],
  templateUrl: './suivi-ctr-bo.component.html',
  styleUrls: ['./suivi-ctr-bo.component.scss']
})
export class SuiviCtrBoComponent implements OnInit, OnDestroy {

  // Données
  comparaisons: CTR[] = [];
  filteredComparaisons: CTR[] = [];
  stats: DashboardStats = {
    totalComparaisons: 0,
    comparaisonsOK: 0,
    comparaisonsKO: 0,
    tauxConcordance: 0,
    montantTotalCarthago: 0,
    montantTotalEncaisse: 0
  };

  // Filtres
  filterStatus: string = '';
  filterDate: string = '';
  searchTerm: string = '';

  // Pagination
  currentPage: number = 1;
  itemsPerPage: number = 10;
  totalItems: number = 0;
  totalPages: number = 0;
  pages: number[] = [];

  // Sélection
  selectedComparison: CTR | null = null;

  // États
  isLoading: boolean = false;
  errorMessage: string = '';

  // Calculs pagination
  get startIndex(): number {
    return (this.currentPage - 1) * this.itemsPerPage;
  }

  get endIndex(): number {
    return Math.min(this.startIndex + this.itemsPerPage, this.totalItems);
  }

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadDashboardData();
    this.loadComparaisons();
  }

  ngOnDestroy(): void {
    // Cleanup si nécessaire
  }

  // Chargement des données du dashboard
  loadDashboardData(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.http.get<DashboardStats>(`${environment.apiUrl}/api/dashboard/stats`)
      .subscribe({
        next: (data) => {
          this.stats = data;
          this.isLoading = false;
          this.initializeCharts();
        },
        error: (error) => {
          console.error();
          this.errorMessage = 'Erreur lors du chargement des statistiques';
          this.isLoading = false;
        }
      });
  }

  // Chargement des comparaisons
  loadComparaisons(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.http.get<CTR[]>(`${environment.apiUrl}/api/ctr/all`)
      .subscribe({
        next: (data) => {
          this.comparaisons = data;
          this.applyFilters();
          this.isLoading = false;
        },
        error: (error) => {
          console.error();
          this.errorMessage = 'Erreur lors du chargement des comparaisons';
          this.isLoading = false;
        }
      });
  }

  // Application des filtres
  applyFilters(): void {
    let filtered = [...this.comparaisons];

    // Filtre par statut
    if (this.filterStatus) {
      filtered = filtered.filter(ctr =>
        ctr.nombreOk === this.filterStatus || ctr.montantOk === this.filterStatus
      );
    }

    // Filtre par date
    if (this.filterDate) {
      const filterDate = new Date(this.filterDate);
      filtered = filtered.filter(ctr => {
        const ctrDate = new Date(ctr.createdAt);
        return ctrDate.toDateString() === filterDate.toDateString();
      });
    }

    // Filtre par recherche
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(ctr =>
        (ctr.fichier?.nomFichier?.toLowerCase().includes(term) || false) ||
        (ctr.carthago?.nomFichier?.toLowerCase().includes(term) || false)
      );
    }

    this.filteredComparaisons = filtered;
    this.updatePagination();
  }

  // Effacer les filtres
  clearFilters(): void {
    this.filterStatus = '';
    this.filterDate = '';
    this.searchTerm = '';
    this.applyFilters();
  }

  // Mise à jour de la pagination
  updatePagination(): void {
    this.totalItems = this.filteredComparaisons.length;
    this.totalPages = Math.ceil(this.totalItems / this.itemsPerPage);
    this.currentPage = Math.min(this.currentPage, this.totalPages);

    // Générer les pages pour la pagination
    this.pages = [];
    const startPage = Math.max(1, this.currentPage - 2);
    const endPage = Math.min(this.totalPages, this.currentPage + 2);

    for (let i = startPage; i <= endPage; i++) {
      this.pages.push(i);
    }
  }

  // Changement de page
  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }

  // Actualiser les données
  refreshData(): void {
    this.loadDashboardData();
    this.loadComparaisons();
  }

  // Voir les détails d'une comparaison
  viewDetails(ctr: CTR): void {
    this.selectedComparison = ctr;
    // Ouvrir le modal Bootstrap
    const modal = document.getElementById('detailsModal');
    if (modal) {
      const bootstrapModal = new (window as any).bootstrap.Modal(modal);
      bootstrapModal.show();
    }
  }

  // Éditer une comparaison
  editComparison(ctr: CTR): void {
    // TODO: Implémenter l'édition
    console.log('Éditer la comparaison:', ctr);
  }

  // Obtenir la classe CSS pour le statut
  getComparisonStatusClass(): string {
    if (!this.selectedComparison) return 'alert-secondary';

    const nombreOk = this.selectedComparison.nombreOk === 'Y';
    const montantOk = this.selectedComparison.montantOk === 'Y';

    if (nombreOk && montantOk) {
      return 'alert-success';
    } else if (nombreOk || montantOk) {
      return 'alert-warning';
    } else {
      return 'alert-danger';
    }
  }

  // Obtenir le texte du statut
  getComparisonStatusText(): string {
    if (!this.selectedComparison) return 'Non disponible';

    const nombreOk = this.selectedComparison.nombreOk === 'Y';
    const montantOk = this.selectedComparison.montantOk === 'Y';

    if (nombreOk && montantOk) {
      return 'Comparaison parfaitement conforme';
    } else if (nombreOk && !montantOk) {
      return 'Nombre OK mais montant différent';
    } else if (!nombreOk && montantOk) {
      return 'Montant OK mais nombre différent';
    } else {
      return 'Comparaison non conforme';
    }
  }

  // Initialisation des graphiques
  initializeCharts(): void {
    this.initializeDepositsRejectsChart();
    this.initializeAmountsEvolutionChart();
  }

  // Graphique répartition dépôts/rejets
  initializeDepositsRejectsChart(): void {
    const chartElement = document.getElementById('chart-deposits-rejects');
    if (!chartElement) return;

    const totalDeposits = this.comparaisons.reduce((sum, ctr) => sum + ctr.nombreDepCarthago, 0);
    const totalRejects = this.comparaisons.reduce((sum, ctr) => sum + ctr.nombreRejCarthago, 0);

    const options = {
      series: [totalDeposits, totalRejects],
      chart: {
        type: 'pie',
        height: 300
      },
      labels: ['Déposés', 'Rejetés'],
      colors: ['#28a745', '#dc3545'],
      legend: {
        position: 'bottom'
      },
      responsive: [{
        breakpoint: 480,
        options: {
          chart: {
            width: 200
          },
          legend: {
            position: 'bottom'
          }
        }
      }]
    };

    // Utiliser ApexCharts si disponible
    if ((window as any).ApexCharts) {
      const chart = new (window as any).ApexCharts(chartElement, options);
      chart.render();
    }
  }

  // Graphique évolution des montants
  initializeAmountsEvolutionChart(): void {
    const chartElement = document.getElementById('chart-amounts-evolution');
    if (!chartElement) return;

    // Grouper les données par date
    const dataByDate = this.comparaisons.reduce((acc, ctr) => {
      const date = new Date(ctr.createdAt).toLocaleDateString();
      if (!acc[date]) {
        acc[date] = { carthago: 0, fichier: 0 };
      }
      acc[date].carthago += ctr.montantTotalCarthago;
      acc[date].fichier += ctr.montantFichier;
      return acc;
    }, {} as any);

    const dates = Object.keys(dataByDate);
    const carthagoAmounts = dates.map(date => dataByDate[date].carthago);
    const fichierAmounts = dates.map(date => dataByDate[date].fichier);

    const options = {
      series: [{
        name: 'Montant Carthago',
        data: carthagoAmounts
      }, {
        name: 'Montant Fichier',
        data: fichierAmounts
      }],
      chart: {
        type: 'line',
        height: 300
      },
      xaxis: {
        categories: dates
      },
      colors: ['#007bff', '#28a745'],
      legend: {
        position: 'top'
      },
      stroke: {
        curve: 'smooth'
      }
    };

    // Utiliser ApexCharts si disponible
    if ((window as any).ApexCharts) {
      const chart = new (window as any).ApexCharts(chartElement, options);
      chart.render();
    }
  }
}
