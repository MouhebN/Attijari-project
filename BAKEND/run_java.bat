@echo off
echo ========================================
echo   Running with Java directly
echo ========================================
echo.

REM Set environment variables
set SPRING_PROFILES_ACTIVE=h2
set JAVA_OPTS=-Dspring.profiles.active=h2

echo Profile: %SPRING_PROFILES_ACTIVE%
echo.

REM Check if target directory exists
if not exist "target\classes" (
    echo Compiling the project first...
    echo Please run: mvn compile
    echo Or install Maven and run: mvn spring-boot:run -Dspring.profiles.active=h2
    pause
    exit /b 1
)

echo Starting application with Java...
echo.

REM Run the application directly with Java
java %JAVA_OPTS% -cp "target\classes;target\dependency\*" tn.esprit.ruya.RUyaApplication

echo.
echo ========================================
echo   Application should be running!
echo ========================================
echo.
echo Test your API at: http://localhost:8080/api/fichiers
echo.
pause 