import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CTR {
  id: number;
  nomFichier: string;
  nombreTotalCarthago: number;
  montantTotalCarthago: number;
  nombreFichier: number;
  montantFichier: number;
  nombreOk: string;
  montantOk: string;
}

@Injectable({
  providedIn: 'root'
})
export class CtrService {
  private readonly baseUrl = 'http://localhost:8080/api/ctr';

  constructor(private http: HttpClient) {}

  getAll(): Observable<CTR[]> {
    return this.http.get<CTR[]>(this.baseUrl);
  }
  comparer(fichierId: number, carthagoId: number): Observable<any> {
    const params = new URLSearchParams();
    params.set('fichierId', fichierId.toString());
    params.set('carthagoId', carthagoId.toString());

    return this.http.post(`${this.baseUrl}/comparer?${params.toString()}`, {});
  }
  getFichiersOptions(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/fichiers/names');
  }

  getCarthagosOptions(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/carthago/names');
  }


  deleteCTR(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
  getTauxConcordanceInterne(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/stats/concordance-interne`);
  }
  getTauxConcordanceEV(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/stats/concordance-ev-carthago`);
  }
  getRepartitionEtat(): Observable<{ [key: string]: number }> {
    return this.http.get<{ [key: string]: number }>(`${this.baseUrl}/stats/repartition-etat`);
  }
  getMontantsEtat(): Observable<{ [key: string]: number }> {
    return this.http.get<{ [key: string]: number }>(`${this.baseUrl}/stats/montant-etat`);
  }
  comparerTous(): Observable<any> {
    return this.http.post('http://localhost:8080/api/ctr/comparer-tous', {}, { responseType: 'text' });
  }
  getEnteteStats(): Observable<{ totalMontantEntete: number, totalNombreEntete: number }> {
    return this.http.get<{ totalMontantEntete: number, totalNombreEntete: number }>('http://localhost:8080/api/carthago/stats/entete');
  }

  getRemisStats(): Observable<{ montantRemis: number, totalRemis: number }> {
    return this.http.get<{ montantRemis: number, totalRemis: number }>('http://localhost:8080/api/carthago-detail/stats/remis');
  }



}
