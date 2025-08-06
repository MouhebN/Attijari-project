@echo off
echo ========================================
echo   Simple H2 Start
echo ========================================
echo.

REM Set the environment variable
set SPRING_PROFILES_ACTIVE=h2

echo Profile set to: %SPRING_PROFILES_ACTIVE%
echo.

REM Run the application
echo Starting Spring Boot application...
mvnw.cmd spring-boot:run -Dspring.profiles.active=h2

echo.
echo ========================================
echo   Application should be running!
echo ========================================
echo.
pause 