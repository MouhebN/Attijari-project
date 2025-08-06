Write-Host "========================================" -ForegroundColor Green
Write-Host "  PowerShell H2 Start" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Set environment variable
$env:SPRING_PROFILES_ACTIVE = "h2"

Write-Host "Profile set to: $env:SPRING_PROFILES_ACTIVE" -ForegroundColor Cyan
Write-Host ""

# Check if Maven wrapper exists
if (Test-Path ".\mvnw.cmd") {
    Write-Host "Using Maven Wrapper..." -ForegroundColor Yellow
    Write-Host "Starting Spring Boot application..." -ForegroundColor Green
    Write-Host ""
    
    # Run the Maven wrapper with the profile
    & .\mvnw.cmd spring-boot:run -Dspring.profiles.active=h2
}
else {
    Write-Host "ERROR: Maven wrapper not found!" -ForegroundColor Red
    Write-Host "Please make sure you're in the correct directory." -ForegroundColor Red
    pause
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "  Application should be running!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Test your API at: http://localhost:8080/api/fichiers" -ForegroundColor White
Write-Host "H2 Console at: http://localhost:8080/api/h2-console" -ForegroundColor White
Write-Host ""
pause 