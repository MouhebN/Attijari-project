@echo off
echo ========================================
echo    Démarrage RUya avec H2 Database
echo ========================================
echo.

echo Configuration:
echo - Profil: H2 (base de données en mémoire)
echo - Port: 8080
echo - URL: http://localhost:8080/api
echo - Console H2: http://localhost:8080/api/h2-console
echo.

echo Démarrage de l'application...
set SPRING_PROFILES_ACTIVE=h2
call mvnw.cmd spring-boot:run -Dspring.profiles.active=h2

pause 