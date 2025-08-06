// Script pour tester l'ajout d'utilisateur avec des données uniques
// Copie et colle ce script dans la console du navigateur

console.log('🧪 Test d\'ajout d\'utilisateur');

// Générer un nom d'utilisateur et email uniques
const timestamp = Date.now();
const uniqueUsername = `testuser${timestamp}`;
const uniqueEmail = `test${timestamp}@example.com`;

console.log('📝 Données de test:');
console.log('Username:', uniqueUsername);
console.log('Email:', uniqueEmail);

// Simuler l'ajout d'utilisateur via l'API
const testUser = {
  username: uniqueUsername,
  email: uniqueEmail,
  password: 'password123'
};

console.log('👤 Utilisateur de test:', testUser);

// Instructions pour tester manuellement
console.log('📋 Instructions pour tester:');
console.log('1. Va sur la page Gestion Utilisateur');
console.log('2. Clique sur "Ajouter un utilisateur"');
console.log('3. Utilise ces données:');
console.log('   - Username:', uniqueUsername);
console.log('   - Email:', uniqueEmail);
console.log('   - Password: password123');
console.log('4. Clique sur "Ajouter"');
console.log('5. Vérifie que l\'utilisateur est ajouté sans erreur'); 