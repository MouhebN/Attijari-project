import { Component, Input } from '@angular/core';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  dataLabels: ApexDataLabels;
  title: ApexTitleSubtitle;
};
import {
  ApexChart,
  ApexNonAxisChartSeries,
  ApexResponsive,
  ApexLegend,
  ApexDataLabels, ChartComponent
} from 'ng-apexcharts';

@Component({
  selector: 'app-montant-bar-chart',
  standalone: true,
  imports: [ChartComponent],
  template: `
    <apx-chart
      [series]="chartOptions.series"
      [chart]="chartOptions.chart"
      [xaxis]="chartOptions.xaxis"
      [dataLabels]="chartOptions.dataLabels"
      [title]="chartOptions.title">
    </apx-chart>
  `
})
export class MontantBarChartComponent {
  @Input() montantDepose: number = 0;
  @Input() montantRejete: number = 0;

  chartOptions: ChartOptions = {
    series: [
      {
        name: "Montants (DT)",
        data: []
      }
    ],
    chart: {
      type: "bar",
      height: 350
    },
    xaxis: {
      categories: ["Déposé", "Rejeté"]
    },
    dataLabels: {
      enabled: true
    },
    title: {
      text: "Montants dépôts vs rejets"
    }
  };

  ngOnChanges() {
    this.chartOptions.series[0].data = [this.montantDepose, this.montantRejete];
  }
}
