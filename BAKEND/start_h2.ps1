Write-Host "========================================" -ForegroundColor Green
Write-Host "  Démarrage avec H2 Database" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Stop any existing process on port 8080
Write-Host "Arrêt des processus existants sur le port 8080..." -ForegroundColor Yellow
$processes = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess
foreach ($processId in $processes) {
    Write-Host "Arrêt du processus $processId" -ForegroundColor Yellow
    Stop-Process -Id $processId -Force -ErrorAction SilentlyContinue
}

Write-Host ""
Write-Host "Configuration du profil H2..." -ForegroundColor Cyan
$env:SPRING_PROFILES_ACTIVE = "h2"

Write-Host "Démarrage de l'application..." -ForegroundColor Green
Write-Host ""

# Run the application with Maven wrapper
if (Test-Path ".\mvnw.cmd") {
    Write-Host "Utilisation de Maven Wrapper..." -ForegroundColor Cyan
    .\mvnw.cmd spring-boot:run -Dspring.profiles.active=h2
}
else {
    Write-Host "ERREUR: Maven Wrapper non trouvé!" -ForegroundColor Red
    Write-Host "Veuillez vous assurer d'être dans le bon répertoire." -ForegroundColor Red
    pause
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "  Application démarrée avec H2 Database" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "URLs utiles:" -ForegroundColor Cyan
Write-Host "- Application: http://localhost:8080/api" -ForegroundColor White
Write-Host "- Console H2:  http://localhost:8080/api/h2-console" -ForegroundColor White
Write-Host "- JDBC URL:    jdbc:h2:mem:testdb" -ForegroundColor White
Write-Host "- Username:    sa" -ForegroundColor White
Write-Host "- Password:    password" -ForegroundColor White
Write-Host ""
pause 