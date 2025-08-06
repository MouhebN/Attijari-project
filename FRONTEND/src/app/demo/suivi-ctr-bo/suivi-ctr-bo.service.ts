import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface CTR {
  id: number;
  dateCtr: string;
  fichierRef: string;
  nbChequesCtr: number;
  montantTotalCtr: number;
}

export interface Carthago {
  id: number;
  dateRemise: string;
  etatRemise: string;
  situationFichier: string;
  heureProchaineGeneration: string;
  fichierUpload: string;
  chequeControler: number;
  chequeVerifier: number;
  montantTotal: number;
  image: string;
}

export interface Fichier {
  id: number;
  typeFichier: string;
  codeValeur: string;
  sens: string;
  codEn: string;
  nomFichier: string;
  natureFichier: string;
  formatFichier: string;
  nombre: number;
  montant: number;
  userId: number;
}

export interface ResultatComparaison {
  id: number;
  carthago: Carthago;
  fichier: Fichier;
  ctr: CTR;
  montantCarthago: number;
  montantFichier: number;
  montantCtr: number;
  nombreChequesCarthago: number;
  nombreChequesFichier: number;
  nombreChequesCtr: number;
  resultatGlobal: string;
  remarques: string;
}

export interface DashboardData {
  ctrStats: any;
  carthagoStats: any;
  resultatsStats: any;
  fichierStats: any;
}

export interface ChartData {
  name: string;
  data: number[];
  color: string;
}

@Injectable({
  providedIn: 'root'
})
export class SuiviCtrBoService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // ==================== CTR ENDPOINTS ====================

  getAllCTR(): Observable<CTR[]> {
    return this.http.get<CTR[]>(`${this.apiUrl}/api/ctr`);
  }

  getCTRById(id: number): Observable<CTR> {
    return this.http.get<CTR>(`${this.apiUrl}/api/ctr/${id}`);
  }

  createCTR(ctr: CTR): Observable<CTR> {
    return this.http.post<CTR>(`${this.apiUrl}/api/ctr`, ctr);
  }

  updateCTR(id: number, ctr: CTR): Observable<CTR> {
    return this.http.put<CTR>(`${this.apiUrl}/api/ctr/${id}`, ctr);
  }

  deleteCTR(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/api/ctr/${id}`);
  }

  getCTRByDate(date: string): Observable<CTR[]> {
    return this.http.get<CTR[]>(`${this.apiUrl}/api/ctr/date/${date}`);
  }

  getCTRBetweenDates(dateDebut: string, dateFin: string): Observable<CTR[]> {
    return this.http.get<CTR[]>(`${this.apiUrl}/api/ctr/date-range?dateDebut=${dateDebut}&dateFin=${dateFin}`);
  }

  getCTRStatsByDateRange(dateDebut: string, dateFin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/ctr/stats/date-range?dateDebut=${dateDebut}&dateFin=${dateFin}`);
  }

  getCTRGlobalStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/ctr/stats/global`);
  }

  // ==================== CARTHAGO ENDPOINTS ====================

  getAllCarthago(): Observable<Carthago[]> {
    return this.http.get<Carthago[]>(`${this.apiUrl}/api/carthago`);
  }

  getCarthagoById(id: number): Observable<Carthago> {
    return this.http.get<Carthago>(`${this.apiUrl}/api/carthago/${id}`);
  }

  createCarthago(carthago: Carthago): Observable<Carthago> {
    return this.http.post<Carthago>(`${this.apiUrl}/api/carthago`, carthago);
  }

  updateCarthago(id: number, carthago: Carthago): Observable<Carthago> {
    return this.http.put<Carthago>(`${this.apiUrl}/api/carthago/${id}`, carthago);
  }

  deleteCarthago(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/api/carthago/${id}`);
  }

  getCarthagoByDate(date: string): Observable<Carthago[]> {
    return this.http.get<Carthago[]>(`${this.apiUrl}/api/carthago/date/${date}`);
  }

  getCarthagoBetweenDates(dateDebut: string, dateFin: string): Observable<Carthago[]> {
    return this.http.get<Carthago[]>(`${this.apiUrl}/api/carthago/date-range?dateDebut=${dateDebut}&dateFin=${dateFin}`);
  }

  getCarthagoByEtatRemise(etatRemise: string): Observable<Carthago[]> {
    return this.http.get<Carthago[]>(`${this.apiUrl}/api/carthago/etat/${etatRemise}`);
  }

  getCarthagoBySituationFichier(situationFichier: string): Observable<Carthago[]> {
    return this.http.get<Carthago[]>(`${this.apiUrl}/api/carthago/situation/${situationFichier}`);
  }

  getCarthagoStatsByDateRange(dateDebut: string, dateFin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/carthago/stats/date-range?dateDebut=${dateDebut}&dateFin=${dateFin}`);
  }

  getCarthagoGlobalStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/carthago/stats/global`);
  }

  getCarthagoStatsByEtat(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/api/carthago/stats/etat`);
  }

  getCarthagoStatsBySituation(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/api/carthago/stats/situation`);
  }

  // ==================== FICHIERS ENDPOINTS ====================

  getAllFichiers(): Observable<Fichier[]> {
    return this.http.get<Fichier[]>(`${this.apiUrl}/api/fichiers`);
  }

  getFichierById(id: number): Observable<Fichier> {
    return this.http.get<Fichier>(`${this.apiUrl}/api/fichiers/${id}`);
  }

  getFichiersByUserId(userId: number): Observable<Fichier[]> {
    return this.http.get<Fichier[]>(`${this.apiUrl}/api/fichiers/user/${userId}`);
  }

  getFichiersByCodEn(codEn: string): Observable<Fichier[]> {
    return this.http.get<Fichier[]>(`${this.apiUrl}/api/fichiers/cod-en/${codEn}`);
  }

  getFichiersBySens(sens: string): Observable<Fichier[]> {
    return this.http.get<Fichier[]>(`${this.apiUrl}/api/fichiers/sens/${sens}`);
  }

  getFichiersByCodeValeur(codeValeur: string): Observable<Fichier[]> {
    return this.http.get<Fichier[]>(`${this.apiUrl}/api/fichiers/code-valeur/${codeValeur}`);
  }

  // ==================== RESULTATS COMPARAISON ENDPOINTS ====================

  getAllResultatsComparaison(): Observable<ResultatComparaison[]> {
    return this.http.get<ResultatComparaison[]>(`${this.apiUrl}/api/resultat-comparaison`);
  }

  getResultatComparaisonById(id: number): Observable<ResultatComparaison> {
    return this.http.get<ResultatComparaison>(`${this.apiUrl}/api/resultat-comparaison/${id}`);
  }

  createResultatComparaison(resultat: ResultatComparaison): Observable<ResultatComparaison> {
    return this.http.post<ResultatComparaison>(`${this.apiUrl}/api/resultat-comparaison`, resultat);
  }

  updateResultatComparaison(id: number, resultat: ResultatComparaison): Observable<ResultatComparaison> {
    return this.http.put<ResultatComparaison>(`${this.apiUrl}/api/resultat-comparaison/${id}`, resultat);
  }

  deleteResultatComparaison(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/api/resultat-comparaison/${id}`);
  }

  getResultatsByResultatGlobal(resultatGlobal: string): Observable<ResultatComparaison[]> {
    return this.http.get<ResultatComparaison[]>(`${this.apiUrl}/api/resultat-comparaison/resultat/${resultatGlobal}`);
  }

  getResultatsByFichier(fichierId: number): Observable<ResultatComparaison[]> {
    return this.http.get<ResultatComparaison[]>(`${this.apiUrl}/api/resultat-comparaison/fichier/${fichierId}`);
  }

  getResultatsByCtr(ctrId: number): Observable<ResultatComparaison[]> {
    return this.http.get<ResultatComparaison[]>(`${this.apiUrl}/api/resultat-comparaison/ctr/${ctrId}`);
  }

  getResultatsByCarthago(carthagoId: number): Observable<ResultatComparaison[]> {
    return this.http.get<ResultatComparaison[]>(`${this.apiUrl}/api/resultat-comparaison/carthago/${carthagoId}`);
  }

  getResultatsRecents(): Observable<ResultatComparaison[]> {
    return this.http.get<ResultatComparaison[]>(`${this.apiUrl}/api/resultat-comparaison/recents`);
  }

  getResultatsAvecDifferences(seuil: number): Observable<ResultatComparaison[]> {
    return this.http.get<ResultatComparaison[]>(`${this.apiUrl}/api/resultat-comparaison/differences?seuil=${seuil}`);
  }

  getResultatsGlobalStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/resultat-comparaison/stats/global`);
  }

  getResultatsComparaisonStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/resultat-comparaison/stats/comparaison`);
  }

  getResultatsDetailleesStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/resultat-comparaison/stats/detaillees`);
  }

  // ==================== DASHBOARD ENDPOINTS ====================

  getDashboardData(): Observable<DashboardData> {
    return this.http.get<DashboardData>(`${this.apiUrl}/api/suivi-ctr-carthago/dashboard`);
  }

  getDashboardDataByDateRange(dateDebut: string, dateFin: string): Observable<DashboardData> {
    return this.http.get<DashboardData>(`${this.apiUrl}/api/suivi-ctr-carthago/dashboard/date-range?dateDebut=${dateDebut}&dateFin=${dateFin}`);
  }

  // ==================== UTILITY METHODS ====================

  convertCTRToChartData(ctrData: CTR[]): ChartData[] {
    const chartData: ChartData[] = [];
    
    // Grouper par fichierRef pour analyser les patterns
    const groupedData = ctrData.reduce((acc, ctr) => {
      const key = ctr.fichierRef;
      if (!acc[key]) {
        acc[key] = { montants: [], chèques: [] };
      }
      acc[key].montants.push(ctr.montantTotalCtr);
      acc[key].chèques.push(ctr.nbChequesCtr);
      return acc;
    }, {} as any);

    // Créer les séries de données
    const montantsData = Object.values(groupedData).map((group: any) => 
      group.montants.reduce((sum: number, montant: number) => sum + montant, 0)
    );
    
    const chèquesData = Object.values(groupedData).map((group: any) => 
      group.chèques.reduce((sum: number, chèque: number) => sum + chèque, 0)
    );

    chartData.push({
      name: 'Montants CTR',
      data: montantsData,
      color: '#3b82f6'
    });

    chartData.push({
      name: 'Chèques CTR',
      data: chèquesData,
      color: '#10b981'
    });

    return chartData;
  }

  convertCarthagoToChartData(carthagoData: Carthago[]): ChartData[] {
    const chartData: ChartData[] = [];
    
    // Grouper par état de remise
    const groupedByEtat = carthagoData.reduce((acc, carthago) => {
      const etat = carthago.etatRemise;
      if (!acc[etat]) {
        acc[etat] = { montants: [], contrôlés: [], vérifiés: [] };
      }
      acc[etat].montants.push(carthago.montantTotal);
      acc[etat].contrôlés.push(carthago.chequeControler);
      acc[etat].vérifiés.push(carthago.chequeVerifier);
      return acc;
    }, {} as any);

    // Créer les séries de données
    Object.keys(groupedByEtat).forEach(etat => {
      const group = groupedByEtat[etat];
      const totalMontant = group.montants.reduce((sum: number, montant: number) => sum + montant, 0);
      const totalContrôlés = group.contrôlés.reduce((sum: number, contrôlé: number) => sum + contrôlé, 0);
      const totalVérifiés = group.vérifiés.reduce((sum: number, vérifié: number) => sum + vérifié, 0);

      chartData.push({
        name: `CARTHAGO - ${etat}`,
        data: [totalMontant, totalContrôlés, totalVérifiés],
        color: this.getStatusColor(etat)
      });
    });

    return chartData;
  }

  calculateStats(data: any[]): any {
    if (!data || data.length === 0) {
      return {
        total: 0,
        count: 0,
        average: 0,
        min: 0,
        max: 0
      };
    }

    const values = data.map(item => item.montant || item.nombre || 0);
    const total = values.reduce((sum, val) => sum + val, 0);
    const count = values.length;
    const average = count > 0 ? total / count : 0;
    const min = Math.min(...values);
    const max = Math.max(...values);

    return {
      total,
      count,
      average,
      min,
      max
    };
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR');
  }

  getStatusColor(status: string): string {
    switch (status.toLowerCase()) {
      case 'valide':
      case 'validé':
        return '#28a745';
      case 'non valide':
      case 'non validé':
        return '#dc3545';
      case 'prévalidé':
      case 'prevalide':
        return '#ffc107';
      case 'en cours':
        return '#17a2b8';
      default:
        return '#6c757d';
    }
  }

  getStatusIcon(status: string): string {
    switch (status.toLowerCase()) {
      case 'valide':
      case 'validé':
        return 'ti ti-check';
      case 'non valide':
      case 'non validé':
        return 'ti ti-x';
      case 'prévalidé':
      case 'prevalide':
        return 'ti ti-clock';
      case 'en cours':
        return 'ti ti-loader';
      default:
        return 'ti ti-help';
    }
  }

  // Nouveaux endpoints pour les cartes
  getRemisesStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/suivi-ctr-carthago/stats/remises`);
  }

  getCarthagoStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/suivi-ctr-carthago/stats/carthago`);
  }

  getCTRStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/suivi-ctr-carthago/stats/ctr`);
  }

  getFichiersCodeValeurStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/suivi-ctr-carthago/stats/fichiers-code-valeur`);
  }

  regenererCarthago(): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/suivi-ctr-carthago/carthago/regenerer`, {});
  }

  getResumeGlobal(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/suivi-ctr-carthago/stats/resume-global`);
  }
} 