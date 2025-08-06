# Script de test pour le frontend Suivi CTR/CARTHAGO
Write-Host "🧪 Test du frontend Suivi CTR/CARTHAGO..." -ForegroundColor Yellow

# Vérifier que le backend est en cours d'exécution
Write-Host "🔍 Vérification du backend..." -ForegroundColor Blue
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/users/test-db" -Method GET -TimeoutSec 5
    Write-Host "✅ Backend accessible: $response" -ForegroundColor Green
} catch {
    Write-Host "❌ Backend non accessible. Assurez-vous qu'il est démarré sur le port 8080" -ForegroundColor Red
    exit 1
}

# Tester les endpoints CTR/CARTHAGO
Write-Host "🌐 Test des endpoints CTR/CARTHAGO..." -ForegroundColor Blue

# Test CTR
try {
    $ctrResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/ctr" -Method GET -TimeoutSec 10
    Write-Host "✅ CTR endpoint: ${ctrResponse.Length} enregistrements" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur CTR endpoint: $($_.Exception.Message)" -ForegroundColor Red
}

# Test CARTHAGO
try {
    $carthagoResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/carthago" -Method GET -TimeoutSec 10
    Write-Host "✅ CARTHAGO endpoint: ${carthagoResponse.Length} enregistrements" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur CARTHAGO endpoint: $($_.Exception.Message)" -ForegroundColor Red
}

# Test Suivi CTR/CARTHAGO
try {
    $suiviResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/suivi-ctr-carthago/dashboard" -Method GET -TimeoutSec 10
    Write-Host "✅ Suivi CTR/CARTHAGO endpoint: Dashboard accessible" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur Suivi CTR/CARTHAGO endpoint: $($_.Exception.Message)" -ForegroundColor Red
}

# Vérifier que le frontend Angular est en cours d'exécution
Write-Host "🌐 Vérification du frontend Angular..." -ForegroundColor Blue
try {
    $frontendResponse = Invoke-WebRequest -Uri "http://localhost:4200" -Method GET -TimeoutSec 5
    Write-Host "✅ Frontend Angular accessible sur http://localhost:4200" -ForegroundColor Green
} catch {
    Write-Host "❌ Frontend Angular non accessible. Démarrez-le avec 'ng serve'" -ForegroundColor Red
}

Write-Host "📋 Instructions pour tester le frontend:" -ForegroundColor Cyan
Write-Host "1. Ouvrez http://localhost:4200" -ForegroundColor White
Write-Host "2. Connectez-vous avec un utilisateur ADMIN" -ForegroundColor White
Write-Host "3. Accédez à 'Suivi CTR/CARTHAGO' dans le menu" -ForegroundColor White
Write-Host "4. Vérifiez que les données backend s'affichent" -ForegroundColor White

Write-Host "🎯 Le frontend devrait maintenant afficher les vraies données CTR/CARTHAGO!" -ForegroundColor Green 