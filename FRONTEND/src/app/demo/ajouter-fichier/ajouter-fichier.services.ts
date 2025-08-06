import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Fichier } from "./fichier.model";

@Injectable({
    providedIn: 'root'
})
export class AjouterFichierService {
    public baseUrl = 'http://localhost:8080/api/fichiers';

    constructor(private http: HttpClient) { }
    
    getAllFichiers(): Observable<Fichier[]> {
        return this.http.get<Fichier[]>(`${this.baseUrl}`);
    }

    createFichier(fichier: Fichier): Observable<Fichier> {
        console.log('Service: Envoi vers', this.baseUrl);
        console.log('Service: Fichier à envoyer (JSON complet):', JSON.stringify(fichier, null, 2));
        console.log('Service: Structure de l\'objet:', {
            typeFichier: fichier.typeFichier,
            codeValeur: fichier.codeValeur,
            sens: fichier.sens,
            codEn: fichier.codEn,
            nomFichier: fichier.nomFichier,
            natureFichier: fichier.natureFichier,
            formatFichier: fichier.formatFichier,
            user: fichier.user
        });
        
        return this.http.post<Fichier>(`${this.baseUrl}`, fichier);
    }
}