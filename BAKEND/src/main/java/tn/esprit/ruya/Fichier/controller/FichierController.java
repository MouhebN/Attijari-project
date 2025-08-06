package tn.esprit.ruya.Fichier.controller;

import tn.esprit.ruya.Fichier.service.FichierServ;
import tn.esprit.ruya.models.Fichier;
import tn.esprit.ruya.models.FichierDTO;
import tn.esprit.ruya.models.FichierUpdateDTO;
import tn.esprit.ruya.models.User;
import tn.esprit.ruya.user.services.UserServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fichiers")
@CrossOrigin(origins = "http://localhost:4200")
public class FichierController {

    @Autowired
    private FichierServ fichierServ;

    @Autowired
    private UserServ userServ;

    // ✅ Get all fichiers
    @GetMapping
    public ResponseEntity<List<Fichier>> getAllFichiers() {
        List<Fichier> fichiers = fichierServ.getAllFichiers();
        return ResponseEntity.ok(fichiers);
    }

    // ✅ Get fichier by ID
    @GetMapping("/{id}")
    public ResponseEntity<Fichier> getFichierById(@PathVariable Long id) {
        Optional<Fichier> fichier = fichierServ.getFichierById(id);
        return fichier.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Create new fichier with validation
    @PostMapping
    public ResponseEntity<?> createFichier(@RequestBody Fichier fichier) {
        try {
            System.out.println("=== DÉBUT CRÉATION FICHIER ===");
            System.out.println("Fichier reçu: " + fichier);

            // Validate required fields
            if (fichier.getUser() == null) {
                return ResponseEntity.badRequest().body("User is required and cannot be null");
            }
            if (fichier.getNatureFichier() == null || fichier.getNatureFichier().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("NatureFichier is required and cannot be null or empty");
            }
            if (fichier.getNomFichier() == null || fichier.getNomFichier().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("NomFichier is required and cannot be null or empty");
            }

            // Handle user object - if only ID is provided, fetch the complete user
            if (fichier.getUser().getId() != null && fichier.getUser().getUsername() == null) {
                Optional<User> userOpt = userServ.getUserById(fichier.getUser().getId());
                if (userOpt.isPresent()) {
                    fichier.setUser(userOpt.get());
                    System.out.println("✅ Utilisateur trouvé et assigné: " + userOpt.get().getUsername());
                } else {
                    return ResponseEntity.badRequest().body("User with ID " + fichier.getUser().getId() + " not found");
                }
            }

            // Save the fichier
            Fichier createdFichier = fichierServ.createFichier(fichier);
            System.out.println("✅ Fichier créé avec succès: " + createdFichier.getId());
            return ResponseEntity.ok(createdFichier);
        } catch (Exception e) {
            // Log the exception for debugging (e.g., using a logger like SLF4J)
            System.err.println("❌ Error creating fichier: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FichierDTO> updateFichier(
            @PathVariable Long id,
            @RequestBody FichierUpdateDTO dto) {
        try {
            Fichier fichier = fichierServ.updateFichier(id, dto);
            if (fichier != null) {
                return ResponseEntity.ok(new FichierDTO(fichier));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error updating fichier: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFichier(@PathVariable Long id) {
        try {
            fichierServ.deleteFichier(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Error deleting fichier: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<FichierDTO>> getFichiersByNatureAndCodeValeur(
            @RequestParam String nature,
            @RequestParam String codeValeur
    ) {
        List<FichierDTO> fichiers = fichierServ.getAllFichiers().stream()
                .filter(f -> f.getNatureFichier() != null &&
                        f.getNatureFichier().equalsIgnoreCase(nature))
                .filter(f -> f.getCodeValeur() != null &&
                        f.getCodeValeur().equalsIgnoreCase(codeValeur))
                .map(FichierDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(fichiers);
    }
    @GetMapping("/names")
    public ResponseEntity<List<Map<String, Object>>> getFichierNames() {
        List<Fichier> fichiers = fichierServ.getAllFichiers();

        List<Map<String, Object>> result = fichiers.stream().map(f -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", f.getId());
            map.put("nomFichier", f.getNomFichier());
            return map;
        }).toList();

        return ResponseEntity.ok(result);
    }



}