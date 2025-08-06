// fichier.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Fichier } from './cheque/cheque30/cheque30.component';
import { FichierUpdateDTO } from './fichier.model';

@Injectable({ providedIn: 'root' })
export class FichierService {
  private readonly apiUrl = 'http://localhost:8080/api/fichiers';

  constructor(private http: HttpClient) {}


  getFichiersByNatureAndCodeValeur(nature: string, codeValeur: string): Observable<Fichier[]> {
    return this.http.get<Fichier[]>(
      `${this.apiUrl}/search?nature=${nature}&codeValeur=${codeValeur}`
    );
  }

updateFichier(id: number, dto: FichierUpdateDTO) {
  return this.http.put<Fichier>(`${this.apiUrl}/${id}`, dto);
}

  deleteFichier(id: number) {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}
