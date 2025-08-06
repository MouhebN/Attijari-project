@echo off
echo ========================================
echo    Force Affichage Menu Admin
echo ========================================
echo.

echo 1. Vérification des utilisateurs ADMIN dans la base...
curl -X GET http://localhost:8080/api/users/test-db

echo.
echo ========================================
echo    Instructions pour forcer le menu :
echo ========================================
echo.
echo 1. Va sur http://localhost:4200
echo 2. Ouvre la console du navigateur (F12)
echo 3. Tape ces commandes dans la console :
echo.
echo ========================================
echo    Commandes à exécuter :
echo ========================================
echo.
echo // Vérifier l'utilisateur actuel
echo localStorage.getItem('user')
echo.
echo // Si le rôle n'est pas ADMIN, forcer le rôle
echo localStorage.setItem('user', JSON.stringify({
echo   id: 1,
echo   username: 'ala',
echo   email: 'ala@gmail.com',
echo   role: 'ADMIN',
echo   isActive: true
echo }))
echo.
echo // Recharger la page pour appliquer les changements
echo window.location.reload()
echo.
echo ========================================
echo    Alternative : Créer un nouvel admin
echo ========================================
echo.
echo Si ça ne marche pas, crée un nouvel admin :
echo curl -X POST http://localhost:8080/api/users/create-admin
echo.
echo Puis connecte-toi avec :
echo - Username: admin
echo - Password: password
echo.
pause 