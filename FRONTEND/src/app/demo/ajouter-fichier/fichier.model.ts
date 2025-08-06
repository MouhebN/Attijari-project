export interface Fichier {
  idFichier?: number;
  typeFichier: string;
  codeValeur: string;
  sens: string;
  codEn: string;
  nomFichier: string;
  natureFichier: string;
  formatFichier?: string;
  nombre?: number;
  montant?: number;
  user: { id: number };
  createdAt?: Date;
  updatedAt?: Date;
}

export interface User {
  id: number;
  username?: string;
  email?: string;
} 