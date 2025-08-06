# Configuration IntelliJ IDEA pour H2 Database

## 🔧 Problème
L'application est lancée depuis IntelliJ IDEA et n'utilise pas le profil H2, causant des erreurs de connexion Oracle.

## 🚀 Solution

### Étape 1: Configurer les Variables d'Environnement dans IntelliJ

1. **Ouvrir la configuration de lancement**
   - Cliquer sur "Run" → "Edit Configurations..."
   - Ou cliquer sur la configuration "RUyaApplication" dans la barre d'outils

2. **Ajouter la variable d'environnement**
   - Dans la section "Environment variables"
   - Cliquer sur le bouton "..." à côté de "Environment variables"
   - Ajouter une nouvelle variable :
     - **Name**: `SPRING_PROFILES_ACTIVE`
     - **Value**: `h2`

3. **Sauvegarder**
   - Cliquer sur "Apply" puis "OK"

### Étape 2: Alternative - Ajouter les paramètres VM

Si la méthode ci-dessus ne fonctionne pas :

1. **Dans la configuration de lancement**
   - Section "VM options"
   - Ajouter : `-Dspring.profiles.active=h2`

### Étape 3: Vérifier la configuration

Après le démarrage, vous devriez voir dans les logs :
```
=== SPRING PROFILES DEBUG ===
Active profiles: h2
Default profiles: default
Database URL: jdbc:h2:mem:testdb
Database Driver: org.h2.Driver
=================================
```

## ✅ Test

1. **Démarrer l'application** depuis IntelliJ
2. **Vérifier les logs** pour confirmer l'utilisation d'H2
3. **Tester l'API** : http://localhost:8080/api/fichiers
4. **Accéder à la console H2** : http://localhost:8080/api/h2-console

## 🔄 Si ça ne fonctionne toujours pas

Utilisez les scripts batch en dehors d'IntelliJ :
1. Ouvrir Command Prompt
2. Naviguer vers le projet
3. Exécuter : `start_h2_simple.bat` 