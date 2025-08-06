@echo off
echo ========================================
echo    Debug Navigation - Gestion Utilisateur
echo ========================================
echo.

echo 1. Vérification du backend...
curl -X GET http://localhost:8080/api/users/test-db

echo.
echo ========================================
echo    Instructions de Debug :
echo ========================================
echo.
echo 1. Va sur http://localhost:4200
echo 2. Ouvre la console du navigateur (F12)
echo 3. Connecte-toi avec un utilisateur ADMIN
echo 4. Dans la console, tape ces commandes :
echo.
echo ========================================
echo    Commandes de Debug :
echo ========================================
echo.
echo // Vérifier l'utilisateur connecté
echo localStorage.getItem('user')
echo.
echo // Vérifier le rôle
echo JSON.parse(localStorage.getItem('user')).role
echo.
echo // Forcer la mise à jour de la navigation
echo window.location.reload()
echo.
echo ========================================
echo    Résultats attendus :
echo ========================================
echo.
echo - localStorage.getItem('user') doit contenir "role":"ADMIN"
echo - La navigation doit afficher "Gestion Utilisateur"
echo - Les logs doivent montrer "Navigation mise à jour pour le rôle: ADMIN"
echo.
echo ========================================
echo    Si ça ne marche pas :
echo ========================================
echo.
echo 1. Vérifie que l'utilisateur a bien le rôle ADMIN
echo 2. Essaie de te reconnecter
echo 3. Vérifie les erreurs dans la console
echo 4. Redémarre le frontend si nécessaire
echo.
pause 