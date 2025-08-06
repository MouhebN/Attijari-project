# Guide d'Intégration - Recherche Avancée dans les Fichiers Chèque

## ✅ **Intégration Terminée**

La recherche avancée a été intégrée avec succès dans tous les fichiers chèque :

### **📁 Fichiers Mis à Jour**

1. **Chèque 30** (`cheque30/`)
   - ✅ Composant TypeScript mis à jour
   - ✅ Template HTML mis à jour
   - ✅ Recherche avancée intégrée

2. **Chèque 31** (`cheque31/`)
   - ✅ Composant TypeScript mis à jour
   - ✅ Template HTML mis à jour
   - ✅ Recherche avancée intégrée

3. **Chèque 32** (`cheque32/`)
   - ✅ Composant TypeScript mis à jour
   - ✅ Template HTML mis à jour
   - ✅ Recherche avancée intégrée

4. **Chèque 33** (`cheque33/`)
   - ✅ Composant TypeScript mis à jour
   - ✅ Template HTML mis à jour
   - ✅ Recherche avancée intégrée

## 🔧 **Modifications Apportées**

### **1. Imports Ajoutés**
```typescript
import { AdvancedSearchComponent, SearchFilters } from '../../../theme/shared/components/advanced-search/advanced-search.component';
```

### **2. Imports du Composant**
```typescript
@Component({
  imports: [CommonModule, FormsModule, AdvancedSearchComponent],
})
```

### **3. Méthodes de Filtrage Ajoutées**
```typescript
// Méthode pour gérer les filtres de recherche avancée
onSearchFiltersChanged(filters: SearchFilters) {
  console.log('Filtres de recherche changés:', filters);
  this.applyAdvancedFilters(filters);
}

// Méthode pour appliquer les filtres avancés
private applyAdvancedFilters(filters: SearchFilters) {
  let filteredData = [...this.fichiers];

  // Filtrer par type de fichier
  if (filters.selectedFileType) {
    filteredData = filteredData.filter(fichier => 
      fichier.TYPE_FICHIER.toLowerCase().includes(filters.selectedFileType!.toLowerCase())
    );
  }

  // Filtrer par code de fichier
  if (filters.selectedFileCode) {
    filteredData = filteredData.filter(fichier => 
      fichier.CODE_VALEUR.includes(filters.selectedFileCode!)
    );
  }

  // Filtrer par plage de dates
  if (filters.dateRange?.start || filters.dateRange?.end) {
    filteredData = filteredData.filter(fichier => {
      const fileDate = new Date(fichier.CREATED_AT);
      const startDate = filters.dateRange?.start ? new Date(filters.dateRange.start) : null;
      const endDate = filters.dateRange?.end ? new Date(filters.dateRange.end) : null;
      
      if (startDate && endDate) {
        return fileDate >= startDate && fileDate <= endDate;
      } else if (startDate) {
        return fileDate >= startDate;
      } else if (endDate) {
        return fileDate <= endDate;
      }
      return true;
    });
  }

  // Filtrer par statut (basé sur le sens)
  if (filters.status && filters.status !== 'all') {
    filteredData = filteredData.filter(fichier => {
      switch (filters.status) {
        case 'en_cours':
          return fichier.SENS === 'E';
        case 'valide':
          return fichier.SENS === 'R';
        case 'rejete':
          return fichier.SENS === '';
        default:
          return true;
      }
    });
  }

  // Mettre à jour les données affichées
  this.fichiers = filteredData;
  
  console.log('Données filtrées:', filteredData);
}
```

### **4. Template HTML Mis à Jour**
```html
<!-- Composant de recherche avancée -->
<app-advanced-search 
  [showDateRange]="true"
  [showStatus]="true"
  (filtersChanged)="onSearchFiltersChanged($event)">
</app-advanced-search>
```

## 🎯 **Fonctionnalités Disponibles**

### **Filtres par Type de Fichier**
- **Chèque** : Fichiers de type chèque
- **Effet** : Fichiers de type effet
- **Prélèvement** : Fichiers de type prélèvement
- **Virement** : Fichiers de type virement

### **Filtres par Code de Fichier**
- **30** : Fichier n° 30
- **31** : Fichier n° 31
- **32** : Fichier n° 32
- **33** : Fichier n° 33

### **Filtres par Date**
- Date de début
- Date de fin
- Plage de dates personnalisable

### **Filtres par Statut**
- **En cours** : Fichiers en cours de traitement
- **Validé** : Fichiers validés
- **Rejeté** : Fichiers rejetés
- **Erreur** : Fichiers en erreur

## 🚀 **Utilisation**

1. **Accéder à une page chèque** (30, 31, 32, ou 33)
2. **Cliquer sur "Filtres"** dans la barre de recherche
3. **Sélectionner les critères** souhaités
4. **Appliquer les filtres** pour voir les résultats filtrés
5. **Réinitialiser** pour effacer tous les filtres

## 📊 **Avantages**

- ✅ **Interface unifiée** : Même composant sur toutes les pages
- ✅ **Filtrage en temps réel** : Résultats instantanés
- ✅ **Design responsive** : Compatible mobile et desktop
- ✅ **Badges actifs** : Visualisation des filtres appliqués
- ✅ **Réinitialisation facile** : Bouton pour tout effacer

## 🔍 **Débogage**

Pour vérifier que tout fonctionne :
1. Ouvrir la console du navigateur
2. Appliquer des filtres
3. Vérifier les logs : `Filtres de recherche changés:` et `Données filtrées:`

## 📝 **Notes Techniques**

- Les filtres sont appliqués côté client pour des performances optimales
- Les données sont filtrées en temps réel sans rechargement de page
- Le composant est réutilisable et peut être facilement adapté à d'autres pages 