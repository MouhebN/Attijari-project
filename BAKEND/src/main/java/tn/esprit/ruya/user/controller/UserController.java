package tn.esprit.ruya.user.controller;

import tn.esprit.ruya.models.ConfirmResetCodeDto;
import tn.esprit.ruya.models.RoleUser;
import tn.esprit.ruya.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ruya.user.services.UserServ;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserServ userServ;

    // Constructeur pour l'injection de dépendances
    public UserController(UserServ userServ) {
        this.userServ = userServ;
    }

    // ✅ Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userServ.getAllUsers());
    }

    // ✅ Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userServ.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Create new user
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        System.out.println("Tentative de création d'utilisateur : " + user.getUsername());

        boolean usernameExists = userServ.existsByUsername(user.getUsername());
        boolean emailExists = userServ.existsByEmail(user.getEmail());

        if (usernameExists || emailExists) {
            String msg = "Utilisateur déjà existant avec : " +
                    (usernameExists ? "username " : "") +
                    (usernameExists && emailExists ? "et " : "") +
                    (emailExists ? "email" : "");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", msg);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        User createdUser = userServ.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        System.out.println("Tentative login : " + loginRequest.getUsername());
        Optional<User> userOpt = userServ.findByUsernameOrEmail(loginRequest.getUsername());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("Utilisateur trouvé : " + user.getUsername());
            if (user.getPassword().equals(loginRequest.getPassword()) && user.getIsActive()) {
                String token = generateMockToken(user.getUsername());
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("user", user);
                return ResponseEntity.ok().body(response);
            } else {
                System.out.println("❌ Mot de passe incorrect");
            }
        } else {
            System.out.println("❌ Utilisateur non trouvé");
        }

        return ResponseEntity.status(401).body("Identifiants invalides");
    }

    private String generateMockToken(String username) {
        return Base64.getEncoder().encodeToString((username + ":" + System.currentTimeMillis()).getBytes());
    }

    // ✅ Update user by ID
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userServ.updateUser(id, updatedUser);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userServ.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return userServ.generateAndSendResetCode(email);
    }

    @PostMapping("/confirm-reset-password")
    public ResponseEntity<?> confirmResetPassword(@RequestBody ConfirmResetCodeDto dto) {
        return userServ.confirmResetCode(dto);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<User> updateUserStatus(@PathVariable Long id, @RequestParam boolean active) {
        try {
            User updatedUser = userServ.updateUserStatus(id, active);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Test endpoint pour vérifier la connexion à la base de données
    @GetMapping("/test-db")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userServ.getAllUsers();
            response.put("status", "success");
            response.put("message", "Connexion à la base de données réussie");
            response.put("userCount", users.size());
            response.put("users", users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur de connexion à la base de données: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // ✅ Endpoint pour vérifier si un utilisateur existe
    @GetMapping("/check-user")
    public ResponseEntity<Map<String, Object>> checkUserExists(
            @RequestParam("username") String username,
            @RequestParam("email") String email) {
        Map<String, Object> response = new HashMap<>();

        boolean usernameExists = userServ.existsByUsername(username);
        boolean emailExists = userServ.existsByEmail(email);

        response.put("usernameExists", usernameExists);
        response.put("emailExists", emailExists);
        response.put("exists", usernameExists || emailExists);

        if (usernameExists || emailExists) {
            String message = "Utilisateur déjà existant avec : " +
                    (usernameExists ? "username " : "") +
                    (usernameExists && emailExists ? "et " : "") +
                    (emailExists ? "email" : "");
            response.put("message", message);
            return ResponseEntity.badRequest().body(response);
        }

        response.put("message", "Utilisateur disponible");
        return ResponseEntity.ok(response);
    }

    // ✅ Endpoint pour nettoyer la base de données (DEV ONLY)
    @GetMapping("/clean-db")
    public ResponseEntity<Map<String, Object>> cleanDatabase() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Supprimer tous les utilisateurs et fichiers
            List<User> allUsers = userServ.getAllUsers();
            int deletedCount = 0;

            for (User user : allUsers) {
                try {
                    userServ.deleteUser(user.getId());
                    deletedCount++;
                } catch (Exception e) {
                    System.out.println(
                            "Erreur lors de la suppression de l'utilisateur " + user.getId() + ": " + e.getMessage());
                }
            }

            response.put("status", "success");
            response.put("message", "Base de données nettoyée avec succès");
            response.put("deletedUsers", deletedCount);
            response.put("totalUsers", allUsers.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors du nettoyage: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/create-admin")
    public ResponseEntity<Map<String, Object>> createAdminUser() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Vérifier si l'admin existe déjà
            if (userServ.existsByUsername("admin")) {
                response.put("status", "warning");
                response.put("message", "L'utilisateur admin existe déjà");
                return ResponseEntity.ok(response);
            }

            // Créer l'utilisateur admin
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@admin.com");
            adminUser.setPassword("password");
            adminUser.setRole(RoleUser.ADMIN);
            adminUser.setIsActive(true);

            User createdAdmin = userServ.createUser(adminUser);

            response.put("status", "success");
            response.put("message", "Utilisateur ADMIN créé avec succès");
            response.put("user", createdAdmin);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de la création de l'admin: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
