// Script pour tester la connexion utilisateur
console.log('🔍 Test de connexion utilisateur...');

// Vérifier localStorage
const userStr = localStorage.getItem('user');
const token = localStorage.getItem('token');

console.log('📋 Token:', token);
console.log('📋 User string:', userStr);

if (userStr) {
  try {
    const user = JSON.parse(userStr);
    console.log('✅ Utilisateur connecté:', user);
    console.log('🆔 ID utilisateur:', user.id);
    console.log('👤 Username:', user.username);
    console.log('📧 Email:', user.email);
    console.log('🔑 Rôle:', user.role);
  } catch (e) {
    console.error();
  }
} else {
  console.log('❌ Aucun utilisateur trouvé dans localStorage');
}

// Test de la méthode getCurrentUser
console.log('🔍 Test getCurrentUser...');
// Cette méthode sera testée dans le composant Angular
