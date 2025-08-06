import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AjouterFichierService {
  private isModalOpen = new BehaviorSubject<boolean>(false);
  isModalOpen$ = this.isModalOpen.asObservable();

  openModal() {
    this.isModalOpen.next(true);
  }

  closeModal() {
    this.isModalOpen.next(false);
  }
} 