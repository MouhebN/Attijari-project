# Guide d'Installation Maven pour Windows

## 🔧 Problème Actuel
Le Maven Wrapper ne fonctionne pas car PowerShell n'est pas trouvé dans le PATH.

## 🚀 Solutions

### Option 1: Installer Maven (Recommandé)

1. **Télécharger Maven**
   - Aller sur: https://maven.apache.org/download.cgi
   - Télécharger: `apache-maven-3.9.6-bin.zip`

2. **Extraire Maven**
   - Créer un dossier: `C:\Program Files\Apache\maven`
   - Extraire le contenu du ZIP dans ce dossier

3. **Configurer les Variables d'Environnement**
   - Ouvrir "Paramètres système avancés"
   - Cliquer sur "Variables d'environnement"
   - Dans "Variables système", ajouter:
     - `MAVEN_HOME`: `C:\Program Files\Apache\maven`
     - `PATH`: ajouter `%MAVEN_HOME%\bin`

4. **Redémarrer PowerShell/CMD**

5. **Tester l'installation**
   ```cmd
   mvn --version
   ```

### Option 2: Utiliser Command Prompt

1. **Ouvrir Command Prompt** (pas PowerShell)
   - Windows + R, taper `cmd`, Enter

2. **Naviguer vers le projet**
   ```cmd
   cd C:\Users\Cyrine\Downloads\project-attijaryVV\project-attijary\BAKEND
   ```

3. **Lancer l'application**
   ```cmd
   set SPRING_PROFILES_ACTIVE=h2
   mvnw.cmd spring-boot:run -Dspring.profiles.active=h2
   ```

### Option 3: Compiler et Exécuter avec Java

1. **Compiler le projet** (une seule fois)
   ```cmd
   mvnw.cmd compile
   ```

2. **Exécuter avec Java**
   ```cmd
   run_java.bat
   ```

## ✅ Test

Après installation, tester:
```cmd
mvn --version
```

Si ça fonctionne, lancer l'application:
```cmd
set SPRING_PROFILES_ACTIVE=h2
mvn spring-boot:run -Dspring.profiles.active=h2
``` 