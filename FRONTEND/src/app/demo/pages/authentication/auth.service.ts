import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Utilisateur } from '../gestion-utilisateur/gestion-utilisateur.component';
export interface User {
  id?: number;
  username: string;
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/users'; // adapte selon ton backend

  constructor(private http: HttpClient) {}

  register(user: User): Observable<any> {
    return this.http.post(`${this.apiUrl}`, user);
  }
  login(user: User): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, user); // adapte l'endpoint si nécessaire
  }
resetPassword(email: string): Observable<any> {
  return this.http.post(`${this.apiUrl}/reset-password`, { email });
}

resetPasswordConfirm(code: string, newPassword: string): Observable<any> {
  return this.http.post(`${this.apiUrl}/confirm-reset-password`, {
    resetCode: code,
    newPassword: newPassword
  });
}
getAllUsers(): Observable<Utilisateur[]> {
  return this.http.get<Utilisateur[]>(this.apiUrl);
}
updateUserStatus(id: number, active: boolean): Observable<Utilisateur> {
  return this.http.put<Utilisateur>(`${this.apiUrl}/${id}/status?active=${active}`, null);
}

// Méthode pour récupérer l'utilisateur connecté depuis localStorage
getCurrentUser(): any {
  const userStr = localStorage.getItem('user');
  if (userStr) {
    try {
      return JSON.parse(userStr);
    } catch (e) {
      console.error();
      return null;
    }
  }
  return null;
}

// Méthode pour vérifier si l'utilisateur est connecté
isLoggedIn(): boolean {
  return localStorage.getItem('token') !== null;
}

// Méthode pour se déconnecter
logout(): void {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
}

// Méthode pour vérifier si un utilisateur existe
checkUserExists(username: string, email: string): Observable<any> {
  return this.http.get(`${this.apiUrl}/check-user?username=${username}&email=${email}`);
}
}
