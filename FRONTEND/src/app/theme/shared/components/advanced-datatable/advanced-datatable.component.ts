import {
  Component,
  Input,
  OnInit,
  AfterViewInit,
  ElementRef,
  ViewChild,
  Output,
  EventEmitter,
  SimpleChanges
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

declare var $: any;

export interface DataTableColumn {
  title: string;
  data: string;
  searchable?: boolean;
  orderable?: boolean;
  width?: string;
  className?: string;
  render?: (data: any, type: any, row: any) => string;
}

export interface DataTableOptions {
  columns: DataTableColumn[];
  data: any[];
  pageLength?: number;
  responsive?: boolean;
  search?: boolean;
  ordering?: boolean;
  info?: boolean;
  paging?: boolean;
  lengthChange?: boolean;
  dom?: string;
}

@Component({
  selector: 'app-advanced-datatable',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="advanced-datatable-container">
      <!-- Barre de recherche avancée -->
      <div class="advanced-search-bar mb-4" *ngIf="showSearchBar">
        <div class="card">
          <div class="card-header bg-gradient-primary text-white">
            <h5 class="mb-0">
              <i class="ti ti-search me-2"></i>
              Recherche avancée
            </h5>
          </div>
          <div class="card-body">
            <div class="row g-3">
              <!-- Recherche par nom -->
              <div class="col-md-4">
                <div class="form-group">
                  <label class="form-label fw-bold">
                    <i class="ti ti-file-text me-1"></i>
                    Nom du fichier
                  </label>
                  <div class="input-group">
                    <span class="input-group-text">
                      <i class="ti ti-search"></i>
                    </span>
                    <input
                      type="text"
                      class="form-control"
                      placeholder="Rechercher par nom de fichier..."
                      [(ngModel)]="searchNom"
                      (input)="onSearchChange()"
                    >
                  </div>
                </div>
              </div>

              <!-- Recherche par code -->
              <div class="col-md-4">
                <div class="form-group">
                  <label class="form-label fw-bold">
                    <i class="ti ti-hash me-1"></i>
                    Code
                  </label>
                  <div class="input-group">
                    <span class="input-group-text">
                      <i class="ti ti-code"></i>
                    </span>
                    <input
                      type="text"
                      class="form-control"
                      placeholder="Rechercher par code..."
                      [(ngModel)]="searchCode"
                      (input)="onSearchChange()"
                    >
                  </div>
                </div>
              </div>

              <!-- Recherche par montant/nombre -->
              <div class="col-md-4">
                <div class="form-group">
                  <label class="form-label fw-bold">
                    <i class="ti ti-currency-dollar me-1"></i>
                    Montant/Nombre
                  </label>
                  <div class="input-group">
                    <span class="input-group-text">
                      <i class="ti ti-calculator"></i>
                    </span>
                    <input
                      type="text"
                      class="form-control"
                      placeholder="Rechercher par montant..."
                      [(ngModel)]="searchMontant"
                      (input)="onSearchChange()"
                    >
                  </div>
                </div>
              </div>

              <!-- Filtre par nature -->
              <div class="col-md-3">
                <div class="form-group">
                  <label class="form-label fw-bold">
                    <i class="ti ti-category me-1"></i>
                    Nature du fichier
                  </label>
                  <select class="form-select" [(ngModel)]="filterNature" (change)="onSearchChange()">
                    <option value="">Toutes les natures</option>
                    <option value="PRELEVEMENT">Prélèvement</option>
                    <option value="VIREMENT">Virement</option>
                    <option value="CHEQUE">Chèque</option>
                  </select>
                </div>
              </div>

              <!-- Filtre par sens -->
              <div class="col-md-3">
                <div class="form-group">
                  <label class="form-label fw-bold">
                    <i class="ti ti-arrow-right-left me-1"></i>
                    Sens
                  </label>
                  <select class="form-select" [(ngModel)]="filterSens" (change)="onSearchChange()">
                    <option value="">Tous les sens</option>
                    <option value="E">Émis</option>
                    <option value="R">Reçu</option>
                  </select>
                </div>
              </div>

              <!-- Date de création -->
              <div class="col-md-3">
                <div class="form-group">
                  <label class="form-label fw-bold">
                    <i class="ti ti-calendar me-1"></i>
                    Date de création
                  </label>
                  <input
                    type="date"
                    class="form-control"
                    [(ngModel)]="filterDate"
                    (change)="onSearchChange()"
                  >
                </div>
              </div>

              <!-- Boutons d'action -->
              <div class="col-md-3">
                <div class="form-group">
                  <label class="form-label fw-bold">
                    <i class="ti ti-settings me-1"></i>
                    Actions
                  </label>
                  <div class="d-flex gap-2">
                    <button class="btn btn-outline-primary btn-sm" (click)="clearFilters()">
                      <i class="ti ti-refresh me-1"></i>
                      Réinitialiser
                    </button>
                    <button class="btn btn-success btn-sm" (click)="exportData()">
                      <i class="ti ti-download me-1"></i>
                      Exporter
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Statistiques -->
      <div class="stats-cards mb-3" *ngIf="showStats">
        <div class="row g-3">
          <div class="col-md-3">
            <div class="card stat-card">
              <div class="card-body text-center">
                <div class="stat-icon bg-primary">
                  <i class="ti ti-files"></i>
                </div>
                <h4 class="stat-number">{{ totalItems }}</h4>
                <p class="stat-label">Total des fichiers</p>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="card stat-card">
              <div class="card-body text-center">
                <div class="stat-icon bg-success">
                  <i class="ti ti-check"></i>
                </div>
                <h4 class="stat-number">{{ filteredItems }}</h4>
                <p class="stat-label">Résultats trouvés</p>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="card stat-card">
              <div class="card-body text-center">
                <div class="stat-icon bg-warning">
                  <i class="ti ti-arrow-up"></i>
                </div>
                <h4 class="stat-number">{{ emisCount }}</h4>
                <p class="stat-label">Fichiers émis</p>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="card stat-card">
              <div class="card-body text-center">
                <div class="stat-icon bg-info">
                  <i class="ti ti-arrow-down"></i>
                </div>
                <h4 class="stat-number">{{ recuCount }}</h4>
                <p class="stat-label">Fichiers reçus</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Table -->
      <div class="table-responsive">
        <table #dataTable class="table table-modern table-hover align-middle">
          <thead class="table-dark">
            <tr>
              <th *ngFor="let column of options.columns"
                  [style.width]="column.width"
                  [class]="column.className">
                <div class="d-flex align-items-center justify-content-between">
                  <span>{{ column.title }}</span>
                  <i class="ti ti-arrows-sort sort-icon" *ngIf="column.orderable !== false"></i>
                </div>
              </th>
              <th *ngIf="showActions" class="text-center" style="width: 120px;">
                Actions
              </th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let row of filteredData; let i = index" class="table-row-anim" [style.animationDelay]="(0.05 * i) + 's'">
              <td *ngFor="let column of options.columns">
                <ng-container [ngSwitch]="column.data">
                  <span *ngSwitchCase="'NATURE_FICHIER'" class="badge badge-nature">
                    <i class="ti ti-category me-1"></i>
                    {{ getValue(row, column.data) }}
                  </span>
                  <span *ngSwitchCase="'SENS'"
                        class="badge"
                        [ngClass]="{'badge-sens-emis': getValue(row, column.data) === 'E', 'badge-sens-recu': getValue(row, column.data) === 'R'}">
                    <i class="ti" [ngClass]="getValue(row, column.data) === 'E' ? 'ti-arrow-up' : 'ti-arrow-down'"></i>
                    {{ getValue(row, column.data) === 'E' ? 'Émis' : 'Reçu' }}
                  </span>
                  <span *ngSwitchCase="'TYPE_FICHIER'" class="badge badge-type">
                    <i class="ti ti-file me-1"></i>
                    {{ getValue(row, column.data) }}
                  </span>
                  <span *ngSwitchDefault>
                    {{ getValue(row, column.data) }}
                  </span>
                </ng-container>
              </td>
              <td *ngIf="showActions" class="text-center">
                <div class="btn-group" role="group">
                  <button class="btn btn-outline-primary btn-sm" (click)="onEdit(row)" title="Modifier">
                    <i class="ti ti-edit"></i>
                  </button>
                 
                  <button class="btn btn-outline-danger btn-sm" (click)="onDelete(row)" title="Supprimer">
                    <i class="ti ti-trash"></i>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination avancée -->
      <div class="advanced-pagination mt-4" *ngIf="showPagination">
        <div class="row align-items-center">
          <div class="col-md-6">
            <div class="d-flex align-items-center">
              <span class="me-3">Affichage:</span>
              <select class="form-select form-select-sm me-3" style="width: auto;" [(ngModel)]="itemsPerPage" (change)="onItemsPerPageChange()">
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="25">25</option>
                <option value="50">50</option>
                <option value="100">100</option>
              </select>
              <span class="text-muted">
                {{ startIndex + 1 }} à {{ endIndex }} sur {{ totalItems }} éléments
              </span>
            </div>
          </div>
          <div class="col-md-6">
            <nav aria-label="Pagination">
              <ul class="pagination pagination-sm justify-content-end mb-0">
                <li class="page-item" [class.disabled]="currentPage === 1">
                  <a class="page-link" (click)="previousPage()">
                    <i class="ti ti-chevron-left"></i>
                  </a>
                </li>
                <li class="page-item" *ngFor="let page of visiblePages" [class.active]="page === currentPage">
                  <a class="page-link" (click)="goToPage(page)">{{ page }}</a>
                </li>
                <li class="page-item" [class.disabled]="currentPage === totalPages">
                  <a class="page-link" (click)="nextPage()">
                    <i class="ti ti-chevron-right"></i>
                  </a>
                </li>
              </ul>
            </nav>
          </div>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./advanced-datatable.component.scss']
})
export class AdvancedDataTableComponent implements OnInit, AfterViewInit {
  @Input() options!: DataTableOptions;
  @Input() showSearchBar: boolean = true;
  @Input() showActions: boolean = true;
  @Input() showPagination: boolean = true;
  @Input() showStats: boolean = true;
  @Input() itemsPerPage: number = 10;

  @Output() editItem = new EventEmitter<any>();
  @Output() viewItem = new EventEmitter<any>();
  @Output() deleteItem = new EventEmitter<any>();

  @ViewChild('dataTable') dataTable!: ElementRef;

  // Variables de recherche
  searchNom: string = '';
  searchCode: string = '';
  searchMontant: string = '';
  filterNature: string = '';
  filterSens: string = '';
  filterDate: string = '';

  // Variables de pagination
  filteredData: any[] = [];
  currentPage: number = 1;
  totalPages: number = 1;
  totalItems: number = 0;
  filteredItems: number = 0;
  startIndex: number = 0;
  endIndex: number = 0;

  // Statistiques
  emisCount: number = 0;
  recuCount: number = 0;

  ngOnInit() {
    this.filterData();
    this.calculateStats();
  }

  ngAfterViewInit() {
    this.initializeDataTable();
  }

  initializeDataTable() {
    // Initialisation de DataTables si nécessaire
    if (typeof $ !== 'undefined' && this.dataTable) {
      $(this.dataTable.nativeElement).DataTable({
        responsive: true,
        language: {
          url: '//cdn.datatables.net/plug-ins/1.10.24/i18n/French.json'
        }
      });
    }
  }

  onSearchChange() {
    this.currentPage = 1;
    this.filterData();
    this.calculateStats();
  }

  onItemsPerPageChange() {
    this.currentPage = 1;
    this.filterData();
  }

  filterData() {
    let filtered = [...this.options.data];

    // Filtrage par nom
    if (this.searchNom) {
      filtered = filtered.filter(item =>
        this.getValue(item, 'NOM_FICHIER')?.toLowerCase().includes(this.searchNom.toLowerCase()) ||
        this.getValue(item, 'COD_EN')?.toLowerCase().includes(this.searchNom.toLowerCase()) ||
        this.getValue(item, 'CODE_VALEUR')?.toLowerCase().includes(this.searchNom.toLowerCase())
      );
    }

    // Filtrage par code
    if (this.searchCode) {
      filtered = filtered.filter(item =>
        this.getValue(item, 'COD_EN')?.includes(this.searchCode) ||
        this.getValue(item, 'CODE_VALEUR')?.includes(this.searchCode)
      );
    }

    // Filtrage par montant
    if (this.searchMontant) {
      filtered = filtered.filter(item =>
        this.getValue(item, 'CODE_VALEUR')?.includes(this.searchMontant) ||
        this.getValue(item, 'COD_EN')?.includes(this.searchMontant)
      );
    }

    // Filtrage par nature
    if (this.filterNature) {
      filtered = filtered.filter(item =>
        this.getValue(item, 'NATURE_FICHIER') === this.filterNature
      );
    }

    // Filtrage par sens
    if (this.filterSens) {
      filtered = filtered.filter(item =>
        this.getValue(item, 'SENS') === this.filterSens
      );
    }

    // Filtrage par date
    if (this.filterDate) {
      filtered = filtered.filter(item => {
        const itemDate = this.getValue(item, 'CREATED_AT');
        return itemDate && itemDate.includes(this.filterDate);
      });
    }

    this.filteredItems = filtered.length;
    this.totalItems = this.options.data.length;
    this.totalPages = Math.ceil(this.filteredItems / this.itemsPerPage);

    // Pagination
    this.startIndex = (this.currentPage - 1) * this.itemsPerPage;
    this.endIndex = Math.min(this.startIndex + this.itemsPerPage, this.filteredItems);

    this.filteredData = filtered.slice(this.startIndex, this.endIndex);
  }

  calculateStats() {
    this.emisCount = this.options.data.filter(item => this.getValue(item, 'SENS') === 'E').length;
    this.recuCount = this.options.data.filter(item => this.getValue(item, 'SENS') === 'R').length;
  }

  clearFilters() {
    this.searchNom = '';
    this.searchCode = '';
    this.searchMontant = '';
    this.filterNature = '';
    this.filterSens = '';
    this.filterDate = '';
    this.onSearchChange();
  }

  exportData() {
    // Logique d'exportation
    console.log('Export des données:', this.filteredData);
  }

  getValue(item: any, key: string): any {
    return item[key] || '';
  }

  get visiblePages(): number[] {
    const pages: number[] = [];
    const maxVisible = 5;
    let start = Math.max(1, this.currentPage - Math.floor(maxVisible / 2));
    let end = Math.min(this.totalPages, start + maxVisible - 1);

    if (end - start + 1 < maxVisible) {
      start = Math.max(1, end - maxVisible + 1);
    }

    for (let i = start; i <= end; i++) {
      pages.push(i);
    }

    return pages;
  }

  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.filterData();
    }
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.filterData();
    }
  }

  goToPage(page: number) {
    this.currentPage = page;
    this.filterData();
  }

  onEdit(item: any) {
    this.editItem.emit(item);
  }

 

  onDelete(item: any) {
    this.deleteItem.emit(item);
  }
  ngOnChanges(changes: SimpleChanges) {
    if (changes['options'] && changes['options'].currentValue) {
      this.currentPage = 1;
      this.filterData();
      this.calculateStats();
    }
  }
}
