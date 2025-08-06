// angular import
import { Component, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';

// project import

// third party
import { NgApexchartsModule, ChartComponent, ApexOptions } from 'ng-apexcharts';

@Component({
  selector: 'app-bar-chart',
  templateUrl: './bar-chart.component.html',
  styleUrl: './bar-chart.component.scss',
  standalone: true,
  imports: [NgApexchartsModule, FormsModule]
})
export class BarChartComponent {
  // public props
  @ViewChild('chart') chart!: ChartComponent;
  chartOptions!: Partial<ApexOptions>;

  // Constructor
  constructor() {
    this.chartOptions = {
      series: [
        {
          name: 'Fichiers réçu',
          data: [90, 60, 45, 30] // exemple : Chèque, Prélèvement, Virement, Effet
        },
        {
          name: 'Fichier rejet',
          data: [30, 20, 15, 10] // exemple : Chèque, Prélèvement, Virement, Effet
        },
        {
          name: 'Fichier Rendu',
          data: [10, 15, 8, 5] // exemple : Chèque, Prélèvement, Virement, Effet
        }
      ],
      dataLabels: {
        enabled: true
      },
      chart: {
        type: 'bar',
        height: 400,
        stacked: false,
        toolbar: {
          show: true
        },
        background: 'transparent',
        id: 'fichiers-bar',
      },
      title: {
        text: 'Statistiques des fichiers par type',
        align: 'left',
        style: {
          fontSize: '1.2rem',
          fontWeight: 'bold',
          color: '#222'
        }
      },
      colors: ['#43a047', '#e53935', '#1e88e5'], // vert, rouge, bleu
      plotOptions: {
        bar: {
          horizontal: false,
          columnWidth: '40%' // ou '50%' pour des barres plus larges
        }
      },
      xaxis: {
        type: 'category',
        categories: ['Chèque', 'Prélèvement', 'Virement', 'Effet']
      },
      tooltip: {
        theme: 'light',
        y: {
          formatter: (val: number) => `${val} fichiers`
        }
      }
    };
  }
}
