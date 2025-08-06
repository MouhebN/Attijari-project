// Script pour forcer l'affichage du menu "Gestion Utilisateur"
// Copie et colle ces commandes dans la console du navigateur (F12)

console.log('🔧 Début du script de force menu admin');

// 1. Vérifier l'utilisateur actuel
const currentUser = localStorage.getItem('user');
console.log('👤 Utilisateur actuel:', currentUser);

// 2. Forcer le rôle ADMIN pour l'utilisateur ala
const adminUser = {
  id: 3,
  username: 'ala',
  email: 'ala@gmail.com',
  role: 'ADMIN',
  isActive: true
};

localStorage.setItem('user', JSON.stringify(adminUser));
console.log('✅ Rôle ADMIN forcé pour ala');

// 3. Forcer la mise à jour de la navigation
// Chercher le service de navigation dans l'application Angular
const app = window.angular?.element(document.body).scope();
if (app && app.navigationService) {
  app.navigationService.updateNavigation();
  console.log('🔄 Navigation mise à jour via service');
} else {
  console.log('⚠️ Service de navigation non trouvé, rechargement nécessaire');
}

// 4. Recharger la page pour appliquer les changements
console.log('🔄 Rechargement de la page...');
setTimeout(() => {
  window.location.reload();
}, 1000);

console.log('✅ Script terminé - Vérifie la sidebar pour le menu "Gestion Utilisateur"'); 