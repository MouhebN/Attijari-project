# Guide de Correction Oracle 21 - Compte Verrouillé

## 🔧 Problème Identifié
**ORA-28000: Compte verrouillé** - Le compte RUYA dans Oracle est verrouillé.

## 🚀 Solutions

### Solution 1: Déverrouiller le compte Oracle (Recommandé)

#### Étape 1: Se connecter à Oracle en tant qu'administrateur
```sql
-- Ouvrir SQL*Plus ou SQL Developer
-- Se connecter en tant que SYSTEM
CONNECT SYSTEM/root@localhost:1521/XEPDB1
```

#### Étape 2: Déverrouiller le compte RUYA
```sql
-- Déverrouiller le compte
ALTER USER RUYA ACCOUNT UNLOCK;

-- Réinitialiser le mot de passe si nécessaire
ALTER USER RUYA IDENTIFIED BY ruya123;

-- Donner les privilèges nécessaires
GRANT CONNECT, RESOURCE, DBA TO RUYA;
GRANT CREATE SESSION TO RUYA;
GRANT UNLIMITED TABLESPACE TO RUYA;

-- Vérifier que le compte est déverrouillé
SELECT username, account_status FROM dba_users WHERE username = 'RUYA';
```

#### Étape 3: Tester la connexion
```sql
-- Se connecter en tant que RUYA
CONNECT RUYA/ruya123@localhost:1521/XEPDB1

-- Vérifier la connexion
SELECT 'Connexion réussie!' FROM DUAL;
```

### Solution 2: Utiliser H2 Database (Alternative rapide)

#### Étape 1: Lancer avec le profil H2
```bash
# Dans le terminal, naviguer vers le projet
cd C:\Users\Cyrine\Downloads\project-attijaryVV\project-attijary\BAKEND

# Lancer avec le profil H2
set SPRING_PROFILES_ACTIVE=h2
mvnw.cmd spring-boot:run -Dspring.profiles.active=h2
```

#### Étape 2: Vérifier que l'application fonctionne
- Application: http://localhost:8080/api
- Console H2: http://localhost:8080/api/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### Solution 3: Configuration IntelliJ IDEA

#### Étape 1: Configurer les variables d'environnement
1. Ouvrir "Run" → "Edit Configurations..."
2. Sélectionner "RUyaApplication"
3. Dans "Environment variables", ajouter:
   - **Name**: `SPRING_PROFILES_ACTIVE`
   - **Value**: `h2`

#### Étape 2: Alternative - Paramètres VM
Dans "VM options", ajouter:
```
-Dspring.profiles.active=h2
```

## ✅ Vérification

### Pour Oracle:
```sql
-- Vérifier le statut du compte
SELECT username, account_status, lock_date, expiry_date 
FROM dba_users 
WHERE username = 'RUYA';
```

### Pour H2:
- L'application démarre sans erreur
- Les logs montrent: `Active profiles: h2`
- L'URL de base de données est: `jdbc:h2:mem:testdb`

## 🔄 Scripts de Lancement

### Script H2 (start_h2.bat):
```batch
@echo off
echo Démarrage avec H2 Database...
set SPRING_PROFILES_ACTIVE=h2
mvnw.cmd spring-boot:run -Dspring.profiles.active=h2
```

### Script Oracle (start_oracle.bat):
```batch
@echo off
echo Démarrage avec Oracle Database...
set SPRING_PROFILES_ACTIVE=default
mvnw.cmd spring-boot:run -Dspring.profiles.active=default
```

## 📝 Notes Importantes

1. **Oracle**: Assurez-vous que le service Oracle est démarré
2. **H2**: Base de données en mémoire, données perdues au redémarrage
3. **Profils**: Utilisez `h2` pour le développement, `default` pour la production
4. **Logs**: Vérifiez les logs pour confirmer le profil actif

## 🆘 En cas de problème

### Oracle toujours verrouillé:
```sql
-- Vérifier les tentatives de connexion échouées
SELECT username, failed_login_attempts 
FROM dba_users 
WHERE username = 'RUYA';

-- Réinitialiser complètement le compte
ALTER USER RUYA ACCOUNT UNLOCK;
ALTER USER RUYA IDENTIFIED BY ruya123;
```

### H2 ne démarre pas:
- Vérifier que le fichier `application-h2.properties` existe
- Vérifier que la variable `SPRING_PROFILES_ACTIVE=h2` est définie
- Redémarrer IntelliJ IDEA si nécessaire 