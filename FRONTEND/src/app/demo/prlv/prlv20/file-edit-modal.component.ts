import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

export interface Fichier {
  id: number;
  codEn: string;
  codeValeur: string;
  createdAt: string;
  natureFichier: string;
  nomFichier: string;
  sens: string;
  typeFichier: string;
  updatedAt: string;
  nombre?: number;
  montant?: number;
}
@Component({
  selector: 'app-file-edit-modal',
  templateUrl: './file-edit-modal.component.html',
  styleUrls: ['./file-edit-modal.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class FileEditModalComponent implements OnChanges {
  @Input() fichier: Fichier | null = null;
  @Input() isOpen: boolean = false;
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<string>();

  nomFichierBase = '';
  extensionFichier = '';

  ngOnChanges(changes: SimpleChanges) {
    if (changes['fichier'] && this.fichier) {
      const parts = this.fichier.nomFichier.split('.');
      this.extensionFichier = parts.length > 1 ? parts.pop()! : '';
      this.nomFichierBase = parts.join('.');
    }
  }

  onClose() {
    this.close.emit();
  }

  onSave() {
    if (this.fichier) {
      const nouveauNom = this.nomFichierBase + (this.extensionFichier ? '.' + this.extensionFichier : '');
      this.save.emit(nouveauNom);
    }
  }
}
