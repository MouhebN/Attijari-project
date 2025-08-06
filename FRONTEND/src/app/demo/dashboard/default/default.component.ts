// Angular Import
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

// project import
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { CtrService } from '../ctr.service';
import { MontantBarChartComponent } from './MontantBarChartComponent';
import { ChartComponent } from 'ng-apexcharts';

import {
  ApexChart,
  ApexNonAxisChartSeries,
  ApexResponsive,
  ApexLegend,
  ApexDataLabels
} from 'ng-apexcharts';
interface CTR {
  id: number;
  nomFichier: string;
  nombreTotalCarthago: number;
  montantTotalCarthago: number;
  nombreFichier: number;
  montantFichier: number;
  nombreOk: string;
  montantOk: string;
}
@Component({
  selector: 'app-default',
  templateUrl: './default.component.html',
  styleUrls: ['./default.component.scss'],
  standalone: true,
  imports: [CommonModule, SharedModule, MontantBarChartComponent, ChartComponent]
})
export class DefaultComponent {
  ctrs: CTR[] = [];
  loadingCTRs = true;
  selectedFichierId: number = 0;
  selectedCarthagoId: number = 0;
  fichiersOptions: { id: number; nomFichier: string }[] = [];
  carthagoOptions: { id: number; nomFichier: string }[] = [];

  constructor(private ctrService: CtrService) {}


  pieSeries: ApexNonAxisChartSeries = [];
  pieChart: ApexChart = {
    type: 'pie',
    height: 400,   // increased from 310
    width: 400,    // added this line for a wider pie
    animations: { enabled: true, speed: 900 }
  };
  pieLabels: string[] = ['Montant Déposé', 'Montant Rejeté'];
  pieResponsive: ApexResponsive[] = [
    { breakpoint: 600, options: { chart: { width: 220 }, legend: { position: 'bottom' } } }
  ];
  pieLegend: ApexLegend = {
    position: 'right',
    fontSize: '18px',
    markers: {
      strokeWidth: 2,
      fillColors: ['#2196f3', '#43e97b'],
      offsetX: 0,
      offsetY: 0,
      shape: "circle"
    },
    itemMargin: { horizontal: 14, vertical: 9 }
  };

  pieDataLabels: ApexDataLabels = {
    enabled: true,
    style: { fontSize: '19px', fontWeight: 'bold' }
  };
  ngOnInit(): void {
    this.loadOptions();
    this.loadTauxConcordance();
    this.loadTauxConcordanceEV();
    this.loadRepartitionEtat();
    this.loadMontantsEtat();
    this.loadEnteteStats();
    this.loadRemisStats();

    this.ctrService.getAll().subscribe({
      next: (data) => {
        this.ctrs = data;
        this.loadingCTRs = false;
      },
      error: (err) => {
        console.error('Erreur chargement CTRs', err);
        this.loadingCTRs = false;
      }
    });
  }

  loadOptions(): void {
    this.ctrService.getFichiersOptions().subscribe({
      next: (fichiers) => (this.fichiersOptions = fichiers),
      error: (err) => console.error('Erreur chargement fichiers', err)
    });

    this.ctrService.getCarthagosOptions().subscribe({
      next: (carthagos) => (this.carthagoOptions = carthagos),
      error: (err) => console.error('Erreur chargement carthagos', err)
    });
  }

  // public method
  ListGroup = [
    {
      name: 'Fichier 006',
      status: 'En attente',
      info: 'Type : Chèque',
      amount: '3 000 DT',
      bgColor: 'bg-light-warning',
      icon: 'ti ti-clock',
      color: 'text-warning'
    },
    {
      name: 'Fichier 007',
      status: 'En attente',
      info: 'Type : Virement',
      amount: '1 500 DT',
      bgColor: 'bg-light-warning',
      icon: 'ti ti-clock',
      color: 'text-warning'
    },
    {
      name: 'Fichier 001',
      status: 'Émis',
      info: 'Type : Chèque',
      amount: '10 000 DT',
      bgColor: 'bg-light-success',
      icon: 'ti ti-arrow-up',
      color: 'text-success'
    },
    {
      name: 'Fichier 002',
      status: 'Rejeté',
      info: 'Type : Prélèvement',
      amount: '2 500 DT',
      bgColor: 'bg-light-danger',
      icon: 'ti ti-x',
      color: 'text-danger'
    },
    {
      name: 'Fichier 003',
      status: 'Émis',
      info: 'Type : Virement',
      amount: '5 000 DT',
      bgColor: 'bg-light-success',
      icon: 'ti ti-arrow-up',
      color: 'text-success'
    },
    {
      name: 'Fichier 004',
      status: 'Rejeté',
      info: 'Type : Effet',
      amount: '1 200 DT',
      bgColor: 'bg-light-danger',
      icon: 'ti ti-x',
      color: 'text-danger'
    },
    {
      name: 'Fichier 005',
      status: 'Émis',
      info: 'Type : Chèque',
      amount: '7 800 DT',
      bgColor: 'bg-light-success',
      icon: 'ti ti-arrow-up',
      color: 'text-success',
      space: 'pb-0'
    },
    {
      name: 'Fichier 008',
      status: 'En attente',
      info: 'Type : Effet',
      amount: '2 200 DT',
      bgColor: 'bg-light-warning',
      icon: 'ti ti-clock',
      color: 'text-warning'
    },
    {
      name: 'Fichier 009',
      status: 'En attente',
      info: 'Type : Prélèvement',
      amount: '4 800 DT',
      bgColor: 'bg-light-warning',
      icon: 'ti ti-clock',
      color: 'text-warning'
    },
    {
      name: 'Fichier 010',
      status: 'En attente',
      info: 'Type : Chèque',
      amount: '6 000 DT',
      bgColor: 'bg-light-warning',
      icon: 'ti ti-clock',
      color: 'text-warning'
    }
  ];

  profileCard = [
    {
      style: 'bg-primary-dark text-white',
      background: 'bg-primary',
      value: '203k €',
      text: 'Bénéfice net',
      color: 'text-white',
      value_color: 'text-white'
    },
    {
      background: 'bg-warning',
      avatar_background: 'bg-light-warning',
      value: '550k €',
      text: 'Revenu total',
      color: 'text-warning'
    }
  ];

  totalFichiers: number = 350; // valeur exemple, à remplacer par la vraie valeur
  fichiersEmis: number = 280;
  fichiersRejetes: number = 70;
  tauxRejet: string = ((this.fichiersRejetes / this.totalFichiers) * 100).toFixed(1) + ' %';

  montantTotal: string = '550k DT';
  revenuTotal: string = '1 200k DT';

  pourcentageJour: number = 12; // exemple, à remplacer par la vraie valeur
  pourcentageMois: number = 38; // exemple, à remplacer par la vraie valeur
  pourcentageEmisJour: number = 85; // exemple, à remplacer par la vraie valeur
  pourcentageEmisMois: number = 92; // exemple, à remplacer par la vraie valeur
  pourcentageRejeteJour: number = 15; // exemple, à remplacer par la vraie valeur
  pourcentageRejeteMois: number = 8; // exemple, à remplacer par la vraie valeur
  montantJour: number = 12000; // exemple, à remplacer par la vraie valeur
  montantMois: number = 350000; // exemple, à remplacer par la vraie valeur

  remisCount: number = 120;
  recuCount: number = 95;
  reprisCount: number = 15;
  renduCount: number = 8;

  // pie-chart.component.ts
  chartOptions = {
    series: [120, 80, 60, 40], // Chèque, Prélèvement, Virement, Effet
    chart: {
      type: 'pie',
      width: 380
    },
    labels: ['Chèque', 'Prélèvement', 'Virement', 'Effet'],
    colors: ['#2196f3', '#43a047', '#e53935', '#fbb034'],
    title: {
      text: 'Répartition des fichiers par type',
      align: 'left',
      style: { fontSize: '1.2rem', fontWeight: 'bold', color: '#222' }
    }
  };

  getTotalEmis(): string {
    // Additionne les montants des fichiers émis
    const total = this.ListGroup.filter((f) => f.status === 'Émis')
      .map((f) => Number(f.amount.replace(/\s|€|DT|,/g, '')))
      .reduce((a, b) => a + b, 0);
    // Formate le total avec séparateur de milliers et le symbole DT
    return total.toLocaleString('fr-FR') + ' DT';
  }

  get pendingFiles() {
    return this.ListGroup.filter((l) => l.status === 'En attente');
  }
  tauxConcordanceEV: number | null = null;

  loadTauxConcordanceEV(): void {
    this.ctrService.getTauxConcordanceEV().subscribe({
      next: (res) => (this.tauxConcordanceEV = res),
      error: (err) => console.error('Erreur chargement taux concordance EV', err)
    });
  }

  tauxConcordanceInterne: number | null = null;
  loadTauxConcordance(): void {
    this.ctrService.getTauxConcordanceInterne().subscribe({
      next: (res) => (this.tauxConcordanceInterne = res),
      error: (err) => console.error('Erreur chargement taux concordance', err)
    });
  }
  lancerComparaison(): void {
    if (!this.selectedFichierId || !this.selectedCarthagoId) {
      alert('Veuillez saisir les deux IDs.');
      return;
    }

    this.loadingCTRs = true;
    this.ctrService.comparer(this.selectedFichierId, this.selectedCarthagoId).subscribe({
      next: (res) => {
        console.log('Résultat comparaison:', res);
        this.ctrService.getAll().subscribe({
          next: (data) => {
            this.ctrs = data;
            this.loadingCTRs = false;
          },
          error: (err) => {
            console.error('Erreur rechargement CTRs', err);
            this.loadingCTRs = false;
          }
        });
      },
      error: (err) => {
        console.error('Erreur comparaison', err);
        this.loadingCTRs = false;
      }
    });
  }
  supprimerCTR(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce CTR ?')) {
      this.ctrService.deleteCTR(id).subscribe({
        next: () => {
          this.ctrs = this.ctrs.filter((c) => c.id !== id);
        },
        error: (err) => console.error('Erreur lors de la suppression', err)
      });
    }
  }
  repartitionEtat: { [key: string]: number } = {};
  loadRepartitionEtat(): void {
    this.ctrService.getRepartitionEtat().subscribe({
      next: (res) => {
        this.repartitionEtat = res;
        console.log('✔️ RepartitionEtat loaded:', this.repartitionEtat); // 👈 ici
      },
      error: (err) => console.error('❌ Erreur chargement répartition état', err)
    });
  }

  montantsEtat: { [key: string]: number } = {};

  loadMontantsEtat(): void {
    this.ctrService.getMontantsEtat().subscribe({
      next: (res) => {
        this.montantsEtat = res;
        // Update pie chart data
        this.pieSeries = [
          this.montantsEtat['DEPOSE'] || 0,
          this.montantsEtat['REJETE'] || 0
        ];
      },
      error: (err) => console.error('Erreur chargement montants par état', err)
    });
  }


  getMontantsKeys(): string[] {
    return Object.keys(this.montantsEtat);
  }
  loadingCompareAll = false;

  comparerTous(): void {
    if (this.loadingCompareAll) return;
    this.loadingCompareAll = true;
    this.ctrService.comparerTous().subscribe({
      next: (res) => {
        alert(res); // Or use a toast for nicer feedback
        // Reload data after comparison
        this.ctrService.getAll().subscribe({
          next: (data) => {
            this.ctrs = data;
            this.loadingCTRs = false;
          },
          error: (err) => {
            console.error('Erreur rechargement CTRs', err);
            this.loadingCTRs = false;
          }
        });
        this.loadingCompareAll = false;
      },
      error: (err) => {
        alert("Erreur lors de la comparaison : " + (err?.error || err));
        this.loadingCompareAll = false;
      }
    });
  }
  enteteStats: { totalMontantEntete: number, totalNombreEntete: number } | null = null;
  remisStats: { montantRemis: number, totalRemis: number } | null = null;


  loadEnteteStats(): void {
    this.ctrService.getEnteteStats().subscribe({
      next: stats => this.enteteStats = stats,
      error: err => { this.enteteStats = null; console.error('Erreur chargement stats entête', err); }
    });
  }

  loadRemisStats(): void {
    this.ctrService.getRemisStats().subscribe({
      next: stats => this.remisStats = stats,
      error: err => { this.remisStats = null; console.error('Erreur chargement stats remis', err); }
    });
  }


}
