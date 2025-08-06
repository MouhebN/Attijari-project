@echo off
echo ========================================
echo   Démarrage de l'application avec H2
echo ========================================
echo.

REM Configuration pour utiliser H2 Database
set SPRING_PROFILES_ACTIVE=h2

echo Configuration active: %SPRING_PROFILES_ACTIVE%
echo.

REM Démarrage avec Maven
cd /d "%~dp0"
echo Démarrage de l'application...
mvn spring-boot:run -Dspring.profiles.active=h2

echo.
echo ========================================
echo   Application démarrée avec H2 Database
echo ========================================
echo.
echo URLs utiles:
echo - Application: http://localhost:8080/api
echo - Console H2:  http://localhost:8080/api/h2-console
echo - JDBC URL:    jdbc:h2:mem:testdb
echo - Username:    sa
echo - Password:    password
echo.
pause 