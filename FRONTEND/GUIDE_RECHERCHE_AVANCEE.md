# Guide d'Utilisation - Recherche Avancée

## Vue d'ensemble

La recherche avancée permet de filtrer les données par :
- **Type de fichier** : Chèque, Effet, Prélèvement, Virement
- **Code de fichier** : 30, 31, 32, 33 (pour les chèques)
- **Plage de dates** : Date de début et de fin
- **Statut** : En cours, Validé, Rejeté, Erreur

## Composants créés

### 1. AdvancedSearchComponent
**Fichier** : `src/app/theme/shared/components/advanced-search/`

**Fonctionnalités** :
- Interface de recherche extensible
- Filtres dynamiques selon le type de fichier
- Badges pour les filtres actifs
- Design responsive

**Utilisation** :
```typescript
import { AdvancedSearchComponent, SearchFilters } from '../path/to/advanced-search.component';

@Component({
  imports: [AdvancedSearchComponent],
  template: `
    <app-advanced-search 
      [showDateRange]="true"
      [showStatus]="true"
      (filtersChanged)="onFiltersChanged($event)">
    </app-advanced-search>
  `
})
```

### 2. SearchService
**Fichier** : `src/app/theme/shared/services/search.service.ts`

**Fonctionnalités** :
- Gestion centralisée des données
- Filtrage automatique
- Statistiques en temps réel
- Observable pour les mises à jour

## Intégration dans les pages existantes

### Étape 1 : Importer le composant
```typescript
import { AdvancedSearchComponent, SearchFilters } from '../../theme/shared/components/advanced-search/advanced-search.component';

@Component({
  imports: [AdvancedSearchComponent]
})
```

### Étape 2 : Ajouter le template
```html
<app-advanced-search 
  [showDateRange]="true"
  [showStatus]="true"
  (filtersChanged)="onFiltersChanged($event)">
</app-advanced-search>
```

### Étape 3 : Gérer les filtres
```typescript
onFiltersChanged(filters: SearchFilters) {
  console.log('Filtres changés:', filters);
  this.applyFilters(filters);
}

private applyFilters(filters: SearchFilters) {
  let filteredData = [...this.originalData];

  // Filtrer par type
  if (filters.selectedFileType) {
    filteredData = filteredData.filter(item => 
      item.type === filters.selectedFileType
    );
  }

  // Filtrer par code
  if (filters.selectedFileCode) {
    filteredData = filteredData.filter(item => 
      item.code === filters.selectedFileCode
    );
  }

  // Mettre à jour les données affichées
  this.displayData = filteredData;
}
```

## Codes de fichiers par type

### Chèque
- **30** : Fichier n° 30
- **31** : Fichier n° 31  
- **32** : Fichier n° 32
- **33** : Fichier n° 33

### Effet
- **40** : Fichier n° 40
- **41** : Fichier n° 41

### Prélèvement
- **20** : Fichier n° 20

### Virement
- **10** : Fichier n° 10

## Fonctionnalités avancées

### 1. Filtres dynamiques
Les codes de fichiers s'adaptent automatiquement selon le type sélectionné.

### 2. Badges actifs
Les filtres appliqués sont affichés sous forme de badges cliquables.

### 3. Réinitialisation
Bouton pour effacer tous les filtres en une fois.

### 4. Responsive design
Interface adaptée aux mobiles et tablettes.

## Exemple d'utilisation complète

```typescript
@Component({
  selector: 'app-example',
  template: `
    <div class="container">
      <!-- Recherche avancée -->
      <app-advanced-search 
        [showDateRange]="true"
        [showStatus]="true"
        (filtersChanged)="onFiltersChanged($event)">
      </app-advanced-search>

      <!-- Affichage des données -->
      <div class="results">
        <div *ngFor="let item of filteredData">
          {{ item.name }} - {{ item.code }}
        </div>
      </div>

      <!-- Statistiques -->
      <div class="stats">
        Total: {{ filteredData.length }}
      </div>
    </div>
  `
})
export class ExampleComponent {
  originalData: any[] = [];
  filteredData: any[] = [];

  onFiltersChanged(filters: SearchFilters) {
    this.applyFilters(filters);
  }

  private applyFilters(filters: SearchFilters) {
    // Logique de filtrage
  }
}
```

## Personnalisation

### Styles CSS
Les styles sont dans `advanced-search.component.scss` et peuvent être personnalisés.

### Options du composant
- `showDateRange` : Afficher/masquer la plage de dates
- `showStatus` : Afficher/masquer le filtre de statut

### Événements
- `filtersChanged` : Émis quand les filtres changent

## Support

Pour toute question ou problème, consulter :
1. Les logs de la console pour le débogage
2. Les données simulées dans `SearchService`
3. Le composant de démonstration `SearchDemoComponent` 