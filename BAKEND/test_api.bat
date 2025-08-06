@echo off
echo ========================================
echo   Test des API Endpoints
echo ========================================
echo.

echo Test de l'endpoint /api/fichiers...
curl -X GET http://localhost:8080/api/fichiers
echo.
echo.

echo Test de l'endpoint /api/users...
curl -X GET http://localhost:8080/api/users
echo.
echo.

echo Test de l'endpoint de santé...
curl -X GET http://localhost:8080/api/actuator/health
echo.

echo.
echo ========================================
echo   Tests terminés
echo ========================================
echo.
pause 