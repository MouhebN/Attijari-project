@echo off
echo ========================================
echo   Démarrage avec H2 Database
echo ========================================
echo.

echo Configuration du profil H2...
set SPRING_PROFILES_ACTIVE=h2

echo Démarrage de l'application...
mvnw.cmd spring-boot:run -Dspring.profiles.active=h2

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