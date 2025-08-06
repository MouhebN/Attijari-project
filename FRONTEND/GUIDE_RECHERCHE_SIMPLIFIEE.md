# Guide de Recherche Avancée Simplifiée

## ✅ **Modifications Terminées**

La recherche avancée a été simplifiée pour inclure uniquement les champs demandés :

### **🎯 Nouveaux Champs de Recherche**

1. **Nom du fichier** (`nomFichier`)
   - Recherche textuelle dans le nom du fichier
   - Insensible à la casse
   - Recherche partielle

2. **Nombre** (`nombre`)
   - Recherche exacte par nombre
   - Champ numérique avec validation

3. **Montant** (`montant`)
   - Recherche exacte par montant
   - Champ numérique avec validation (step="0.01")

4. **Date** (`dateRange`)
   - Plage de dates (début et fin)
   - Filtrage par date de création

### **📁 Fichiers Mis à Jour**

#### **1. Composant de Recherche Avancée**
- ✅ `advanced-search.component.ts` - Interface et logique mises à jour
- ✅ `advanced-search.component.html` - Template simplifié

#### **2. Composants Chèque**
- ✅ `cheque30/` - Interface et filtres mis à jour
- ✅ `cheque31/` - Interface et filtres mis à jour
- ✅ `cheque32/` - Interface et filtres mis à jour
- ✅ `cheque33/` - Interface et filtres mis à jour

#### **3. Services et Autres**
- ✅ `search.service.ts` - Logique de filtrage mise à jour
- ✅ `search-demo.component.ts` - Template mis à jour

### **🔧 Modifications Techniques**

#### **1. Interface SearchFilters**
```typescript
export interface SearchFilters {
  nomFichier?: string;
  nombre?: number;
  montant?: number;
  dateRange?: {
    start: string;
    end: string;
  };
}
```

#### **2. Interface Fichier (Mise à Jour)**
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
  NOMBRE?: number;    // Ajouté
  MONTANT?: number;   // Ajouté
}
```

#### **3. Logique de Filtrage**
```typescript
private applyAdvancedFilters(filters: SearchFilters) {
  let filteredData = [...this.fichiers];

  // Filtrer par nom de fichier
  if (filters.nomFichier) {
    filteredData = filteredData.filter(fichier => 
      fichier.NOM_FICHIER.toLowerCase().includes(filters.nomFichier!.toLowerCase())
    );
  }

  // Filtrer par nombre
  if (filters.nombre !== undefined && filters.nombre !== null) {
    filteredData = filteredData.filter(fichier => 
      fichier.NOMBRE === filters.nombre
    );
  }

  // Filtrer par montant
  if (filters.montant !== undefined && filters.montant !== null) {
    filteredData = filteredData.filter(fichier => 
      fichier.MONTANT === filters.montant
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

  // Mettre à jour les données affichées
  this.datatableOptions.data = filteredData;
}
```

### **🎨 Interface Utilisateur**

#### **Champs de Recherche**
- **Nom du fichier** : Champ texte avec icône document
- **Nombre** : Champ numérique avec icône calculatrice
- **Montant** : Champ numérique avec icône devise
- **Date de début** : Sélecteur de date
- **Date de fin** : Sélecteur de date

#### **Badges Actifs**
- Affichage des filtres appliqués
- Bouton de suppression pour chaque filtre
- Couleurs distinctes pour chaque type de filtre

#### **Boutons d'Action**
- **Réinitialiser** : Efface tous les filtres
- **Appliquer** : Applique les filtres sélectionnés

### **🚀 Utilisation**

1. **Accéder à une page chèque** (30, 31, 32, ou 33)
2. **Cliquer sur "Filtres"** dans la barre de recherche
3. **Remplir les champs souhaités** :
   - Nom du fichier (recherche textuelle)
   - Nombre (valeur exacte)
   - Montant (valeur exacte)
   - Plage de dates
4. **Appliquer les filtres** pour voir les résultats
5. **Réinitialiser** pour effacer tous les filtres

### **📊 Avantages**

- ✅ **Interface simplifiée** : Seulement les champs nécessaires
- ✅ **Recherche précise** : Filtrage exact pour nombre et montant
- ✅ **Recherche flexible** : Recherche partielle pour le nom
- ✅ **Plage de dates** : Filtrage par période
- ✅ **Performance optimisée** : Filtrage côté client
- ✅ **Interface cohérente** : Même design sur toutes les pages

### **🔍 Débogage**

Pour vérifier que tout fonctionne :
1. Ouvrir la console du navigateur
2. Appliquer des filtres
3. Vérifier les logs : `Filtres de recherche changés:` et `Données filtrées:`

### **📝 Notes Techniques**

- Les filtres sont appliqués côté client pour des performances optimales
- Les données sont filtrées en temps réel sans rechargement de page
- Le composant est réutilisable et peut être facilement adapté à d'autres pages
- Les champs nombre et montant utilisent une comparaison exacte
- Le champ nom de fichier utilise une recherche insensible à la casse 