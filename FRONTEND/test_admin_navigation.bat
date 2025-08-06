@echo off
echo ========================================
echo    Test Navigation Admin - Gestion Utilisateur
echo ========================================
echo.

echo 1. Vérification du backend...
curl -X GET http://localhost:8080/api/users/test-db

echo.
echo 2. Création de l'utilisateur ADMIN...
curl -X POST http://localhost:8080/api/users/create-admin

echo.
echo ========================================
echo    Instructions pour tester :
echo ========================================
echo.
echo 1. Va sur http://localhost:4200
echo 2. Connecte-toi avec :
echo    - Username: admin
echo    - Password: password
echo 3. Vérifie dans la console du navigateur :
echo    - "Navigation mise à jour pour le rôle: ADMIN"
echo    - "Éléments visibles: [..., 'Gestion Utilisateur']"
echo 4. Vérifie que le menu "Gestion Utilisateur" apparaît
echo    dans la sidebar
echo.
echo ========================================
echo    Debug en cas de problème :
echo ========================================
echo.
echo Dans la console du navigateur, tape :
echo localStorage.getItem('user')
echo.
echo Tu devrais voir quelque chose comme :
echo {"id":1,"username":"admin","role":"ADMIN",...}
echo.
echo Si le rôle n'est pas ADMIN, le menu ne s'affichera pas.
echo.
pause 