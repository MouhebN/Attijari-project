package tn.esprit.ruya.user.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import tn.esprit.ruya.models.ConfirmResetCodeDto;
import tn.esprit.ruya.models.RoleUser;
import tn.esprit.ruya.models.User;
import org.springframework.stereotype.Service;
import tn.esprit.ruya.user.repository.IUserRepo;

import java.util.*;

@Service
public class UserServ implements IUserServ {

    private final IUserRepo userRepository;
    private final JavaMailSender mailSender;

    // Maps initialisées directement
    private final Map<String, String> emailToResetCode = new HashMap<>();
    private final Map<String, String> codeToEmail = new HashMap<>();

    // Constructeur manuel pour l'injection de dépendances
    public UserServ(IUserRepo userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        user.setRole(RoleUser.SIMPLE_USER);
        user.setIsActive(true);
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole());
            return userRepository.save(user);
        }).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByUsernameOrEmail(String input) {
        if (input.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return userRepository.findByEmail(input);
        } else {
            return userRepository.findByUsername(input);
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public ResponseEntity<?> generateAndSendResetCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        // Remplacement de isEmpty() par !isPresent()
        if (!optionalUser.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Utilisateur non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Générer un code à 6 chiffres
        String resetCode = String.format("%06d", new Random().nextInt(1_000_000));

        // Stocker dans les deux maps
        emailToResetCode.put(email, resetCode);
        codeToEmail.put(resetCode, email);

        // Envoyer par mail
        sendResetCodeByEmail(email, resetCode);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Code de réinitialisation envoyé");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> confirmResetCode(ConfirmResetCodeDto dto) {
        String email = codeToEmail.get(dto.getResetCode());

        if (email == null || !emailToResetCode.get(email).equals(dto.getResetCode())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Code invalide ou expiré");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Utilisateur non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = optionalUser.get();
        user.setPassword(dto.getNewPassword()); // TODO: Encoder avec BCrypt
        userRepository.save(user);

        // Supprimer les codes
        emailToResetCode.remove(email);
        codeToEmail.remove(dto.getResetCode());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Mot de passe réinitialisé avec succès");
        return ResponseEntity.ok(response);
    }

    private void sendResetCodeByEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Code de réinitialisation - RU'ya");
        message.setText("Votre code de réinitialisation est : " + code + "\nIl est valable pendant 10 minutes.");
        mailSender.send(message);
    }

    public User updateUserStatus(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(active);
        return userRepository.save(user);
    }
}
