@echo off
echo ========================================
echo    Test Connexion Admin - Utilisateur "ala"
echo ========================================
echo.

echo 1. Vérification des utilisateurs dans la base...
curl -X GET http://localhost:8080/api/users/test-db

echo.
echo ========================================
echo    Instructions pour tester :
echo ========================================
echo.
echo 1. Va sur http://localhost:4200
echo 2. Connecte-toi avec l'utilisateur existant :
echo    - Username: ala
echo    - Password: (le mot de passe de ala)
echo.
echo 3. Vérifie dans la console du navigateur :
echo    - "Navigation mise à jour pour le rôle: ADMIN"
echo    - "Éléments visibles: [..., 'Gestion Utilisateur']"
echo.
echo 4. Vérifie que le menu "Gestion Utilisateur" apparaît
echo    dans la sidebar
echo.
echo ========================================
echo    Si ça ne marche pas :
echo ========================================
echo.
echo 1. Vérifie le mot de passe de l'utilisateur "ala"
echo 2. Essaie de te connecter avec :
echo    - Username: admin
echo    - Password: password
echo.
echo 3. Dans la console du navigateur, vérifie :
echo    localStorage.getItem('user')
echo.
echo Tu devrais voir le rôle "ADMIN"
echo.
pause 