@echo off
echo ========================================
echo   Quick Start - H2 Database
echo ========================================
echo.

echo Starting application with H2 profile...
echo.

REM Run with Maven wrapper and pass the profile directly
cmd /c "set SPRING_PROFILES_ACTIVE=h2 && mvnw.cmd spring-boot:run -Dspring.profiles.active=h2"

echo.
echo ========================================
echo   Application should be running!
echo ========================================
echo.
echo Test your API at: http://localhost:8080/api/fichiers
echo H2 Console at: http://localhost:8080/api/h2-console
echo.
pause 