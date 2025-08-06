# Composant Advanced DataTable

Ce composant fournit une interface de datatable avancée avec des fonctionnalités de recherche, filtrage et pagination.

## Fonctionnalités

### 🔍 Recherche avancée
- Recherche par nom de fichier
- Recherche par code
- Recherche par montant/nombre
- Filtrage par nature du fichier
- Filtrage par sens (Émis/Reçu)
- Filtrage par date de création

### 📊 Statistiques en temps réel
- Total des fichiers
- Nombre de résultats trouvés
- Nombre de fichiers émis
- Nombre de fichiers reçus

### 🎨 Design moderne
- Interface utilisateur moderne avec gradients
- Animations fluides
- Design responsive
- Badges colorés pour les différents types

### 📱 Fonctionnalités avancées
- Pagination personnalisée
- Tri des colonnes
- Actions (Modifier, Voir, Supprimer)
- Export de données
- Réinitialisation des filtres

## Utilisation

### Import du composant

```typescript
import { AdvancedDataTableComponent, DataTableOptions, DataTableColumn } from '../../../theme/shared/components/advanced-datatable/advanced-datatable.component';
```

### Configuration des données

```typescript
interface Fichier {
  COD_EN: string;
  CODE_VALEUR: string;
  CREATED_AT: string;
  NATURE_FICHIER: string;
  NOM_FICHIER: string;
  SENS: string;
  TYPE_FICHIER: string;
  UPDATED_AT: string;
}

// Configuration du datatable
datatableOptions: DataTableOptions = {
  columns: [
    { title: 'Code EN', data: 'COD_EN', width: '10%' },
    { title: 'Code Valeur', data: 'CODE_VALEUR', width: '12%' },
    { title: 'Date de création', data: 'CREATED_AT', width: '15%' },
    { title: 'Nature du fichier', data: 'NATURE_FICHIER', width: '15%' },
    { title: 'Nom du fichier', data: 'NOM_FICHIER', width: '20%' },
    { title: 'Sens', data: 'SENS', width: '8%' },
    { title: 'Type de fichier', data: 'TYPE_FICHIER', width: '12%' },
    { title: 'Date de mise à jour', data: 'UPDATED_AT', width: '15%' },
  ],
  data: this.fichiers,
  pageLength: 10,
  responsive: true,
  search: true,
  ordering: true,
  info: true,
  paging: true,
  lengthChange: true,
  dom: 'lfrtip'
};
```

### Template HTML

```html
<app-advanced-datatable 
  [options]="datatableOptions"
  [showSearchBar]="true"
  [showActions]="true"
  [showPagination]="true"
  [showStats]="true"
  [itemsPerPage]="10"
  (editItem)="onEditFichier($event)"
  (viewItem)="onViewFichier($event)"
  (deleteItem)="onDeleteFichier($event)">
</app-advanced-datatable>
```

### Gestion des événements

```typescript
// Méthodes pour gérer les événements du datatable avancé
onEditFichier(fichier: any) {
  console.log('Modifier fichier:', fichier);
  // Implémenter la logique de modification
}

onViewFichier(fichier: any) {
  console.log('Voir fichier:', fichier);
  // Implémenter la logique de visualisation
}

onDeleteFichier(fichier: any) {
  if (confirm('Voulez-vous vraiment supprimer ce fichier ?')) {
    this.fichiers = this.fichiers.filter(f => f !== fichier);
    this.datatableOptions.data = [...this.fichiers];
  }
}
```

## Propriétés d'entrée

| Propriété | Type | Défaut | Description |
|-----------|------|--------|-------------|
| `options` | `DataTableOptions` | Requis | Configuration du datatable |
| `showSearchBar` | `boolean` | `true` | Afficher la barre de recherche |
| `showActions` | `boolean` | `true` | Afficher les boutons d'action |
| `showPagination` | `boolean` | `true` | Afficher la pagination |
| `showStats` | `boolean` | `true` | Afficher les statistiques |
| `itemsPerPage` | `number` | `10` | Nombre d'éléments par page |

## Événements de sortie

| Événement | Type | Description |
|-----------|------|-------------|
| `editItem` | `EventEmitter<any>` | Émis lors du clic sur le bouton Modifier |
| `viewItem` | `EventEmitter<any>` | Émis lors du clic sur le bouton Voir |
| `deleteItem` | `EventEmitter<any>` | Émis lors du clic sur le bouton Supprimer |

## Interfaces

### DataTableColumn

```typescript
interface DataTableColumn {
  title: string;           // Titre de la colonne
  data: string;            // Clé des données
  searchable?: boolean;    // Colonne recherchable
  orderable?: boolean;     // Colonne triable
  width?: string;          // Largeur de la colonne
  className?: string;      // Classe CSS
  render?: (data: any, type: any, row: any) => string; // Fonction de rendu personnalisée
}
```

### DataTableOptions

```typescript
interface DataTableOptions {
  columns: DataTableColumn[];  // Configuration des colonnes
  data: any[];                 // Données du tableau
  pageLength?: number;         // Nombre d'éléments par page
  responsive?: boolean;        // Tableau responsive
  search?: boolean;           // Activer la recherche
  ordering?: boolean;         // Activer le tri
  info?: boolean;            // Afficher les informations
  paging?: boolean;          // Activer la pagination
  lengthChange?: boolean;    // Permettre le changement de longueur
  dom?: string;             // Configuration DOM
}
```

## Styles personnalisés

Le composant utilise des styles SCSS modernes avec :
- Gradients colorés
- Animations fluides
- Effets de survol
- Design responsive
- Badges stylisés

## Dépendances

- Angular 20+
- Bootstrap 5
- Tabler Icons
- DataTables.net (optionnel)

## Exemple complet

Voir les fichiers `cheque30.component.ts` et `cheque31.component.ts` pour des exemples d'utilisation complets. 