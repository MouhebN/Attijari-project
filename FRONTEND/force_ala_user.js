// Script pour forcer la connexion avec l'utilisateur "ala"
console.log('🔧 Forcer la connexion avec ala...');

const alaUser = {
  id: 3,
  username: 'ala',
  email: 'ala@example.com',
  role: 'ADMIN',
  isActive: true
};

const alaToken = 'fake-token-ala-' + Date.now();

// Sauvegarder dans localStorage
localStorage.setItem('user', JSON.stringify(alaUser));
localStorage.setItem('token', alaToken);

console.log('✅ Utilisateur ala connecté:', alaUser);
console.log('🔑 Token:', alaToken);

// Recharger la page pour appliquer les changements
console.log('🔄 Rechargement de la page...');
// window.location.reload(); 