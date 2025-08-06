@echo off
echo ========================================
echo   Test de Connexion Oracle 21
echo ========================================
echo.

echo Test de connexion au compte RUYA...
echo.

REM Test de connexion avec sqlplus
echo Tentative de connexion...
sqlplus RUYA/ruya123@localhost:1521/XEPDB1 @test_connection.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo   Connexion Oracle reussie !
    echo ========================================
    echo.
    echo Vous pouvez maintenant demarrer l'application.
    echo.
) else (
    echo.
    echo ========================================
    echo   Erreur de connexion Oracle
    echo ========================================
    echo.
    echo Le compte RUYA est probablement verrouille.
    echo.
    echo Solutions :
    echo 1. Deverrouiller le compte avec le script unlock_oracle.sql
    echo 2. Ou utiliser H2 Database : start_h2_simple.bat
    echo.
)

pause 