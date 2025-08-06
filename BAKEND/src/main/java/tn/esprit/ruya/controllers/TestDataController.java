/*
package tn.esprit.ruya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ruya.models.CTR;
import tn.esprit.ruya.models.Carthago;
import tn.esprit.ruya.models.Fichier;
import tn.esprit.ruya.models.User;
import tn.esprit.ruya.services.CTRService;
import tn.esprit.ruya.services.CarthagoService;
import tn.esprit.ruya.Fichier.service.FichierServ;
import tn.esprit.ruya.models.CarthagoDetail;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/test-data")
@CrossOrigin(origins = "http://localhost:4200")
public class TestDataController {

    @Autowired
    private CTRService ctrService;

    @Autowired
    private CarthagoService carthagoService;

    @Autowired
    private FichierServ fichierService;

    // Endpoint pour insérer des données de test CTR
    @PostMapping("/insert-ctr-test-data")
    public ResponseEntity<String> insertCTRTestData() {
        try {
            // Nettoyer les données existantes
            List<CTR> existingCTRs = ctrService.getAllCTR();
            for (CTR ctr : existingCTRs) {
                ctrService.deleteCTR(ctr.getId());
            }

            // Insérer les données de test CTR
            insertCTRData();

            return ResponseEntity.ok("Données CTR de test insérées avec succès!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'insertion des données CTR: " + e.getMessage());
        }
    }

    // Endpoint pour insérer des données de test CARTHAGO
    @PostMapping("/insert-carthago-test-data")
    public ResponseEntity<String> insertCarthagoTestData() {
        try {
            // Nettoyer les données existantes
            List<Carthago> existingCarthagos = carthagoService.getAllCarthago();
            for (Carthago carthago : existingCarthagos) {
                carthagoService.deleteCarthago(carthago.getId());
            }

            // Insérer les données de test CARTHAGO
            insertCarthagoData();

            return ResponseEntity.ok("Données CARTHAGO de test insérées avec succès!");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Erreur lors de l'insertion des données CARTHAGO: " + e.getMessage());
        }
    }

    // Endpoint pour insérer des données FICHIERS de test
    @PostMapping("/insert-fichiers-test-data")
    public ResponseEntity<String> insertFichiersTestData() {
        try {
            // Nettoyer les données existantes
            List<Fichier> existingFichiers = fichierService.getAllFichiers();
            for (Fichier fichier : existingFichiers) {
                fichierService.deleteFichier(fichier.getId());
            }

            // Insérer les données de test FICHIERS
            insertFichiersData();

            return ResponseEntity.ok("Données FICHIERS de test insérées avec succès!");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Erreur lors de l'insertion des données FICHIERS: " + e.getMessage());
        }
    }

    // Endpoint pour insérer des données CTR compatibles
    @PostMapping("/insert-ctr-compatible")
    public ResponseEntity<String> insertCTRCompatible() {
        try {
            // Nettoyer les données existantes
            List<CTR> existingCTRs = ctrService.getAllCTR();
            for (CTR ctr : existingCTRs) {
                ctrService.deleteCTR(ctr.getId());
            }

            // Insérer les données CTR compatibles
            insertCTRCompatibleData();

            return ResponseEntity.ok("Données CTR compatibles insérées avec succès!");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Erreur lors de l'insertion des données CTR compatibles: " + e.getMessage());
        }
    }

    // Endpoint pour insérer des données CARTHAGO compatibles
    @PostMapping("/insert-carthago-compatible")
    public ResponseEntity<String> insertCarthagoCompatible() {
        try {
            // Nettoyer les données existantes
            List<Carthago> existingCarthagos = carthagoService.getAllCarthago();
            for (Carthago carthago : existingCarthagos) {
                carthagoService.deleteCarthago(carthago.getId());
            }

            // Insérer les données CARTHAGO compatibles
            insertCarthagoCompatibleData();

            return ResponseEntity.ok("Données CARTHAGO compatibles insérées avec succès!");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Erreur lors de l'insertion des données CARTHAGO compatibles: " + e.getMessage());
        }
    }

    // Endpoint pour insérer toutes les données de test
    @PostMapping("/insert-all-test-data")
    public ResponseEntity<String> insertAllTestData() {
        try {
            // Nettoyer et insérer CTR
            List<CTR> existingCTRs = ctrService.getAllCTR();
            for (CTR ctr : existingCTRs) {
                ctrService.deleteCTR(ctr.getId());
            }
            insertCTRData();

            // Nettoyer et insérer CARTHAGO
            List<Carthago> existingCarthagos = carthagoService.getAllCarthago();
            for (Carthago carthago : existingCarthagos) {
                carthagoService.deleteCarthago(carthago.getId());
            }
            insertCarthagoData();

            return ResponseEntity.ok("Toutes les données de test insérées avec succès!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'insertion des données: " + e.getMessage());
        }
    }

    // Méthode pour insérer les données FICHIERS
    private void insertFichiersData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // FICHIERS pour Chèques 30, 31, 32, 33
        createFichier(1L, "cheque", "30", "emis", "21", "cheque-30-21", "Chèque", "env", 140, 119999.97, 3L);
        createFichier(2L, "cheque", "30", "recus", "22", "cheque-30-22", "Chèque", "env", 155, 135000.50, 3L);
        createFichier(3L, "cheque", "31", "emis", "23", "cheque-31-23", "Chèque", "env", 120, 98000.25, 3L);
        createFichier(4L, "cheque", "31", "recus", "24", "cheque-31-24", "Chèque", "env", 180, 165000.75, 3L);
        createFichier(5L, "cheque", "32", "emis", "25", "cheque-32-25", "Chèque", "env", 200, 185000.00, 3L);
        createFichier(6L, "cheque", "32", "recus", "26", "cheque-32-26", "Chèque", "env", 160, 145000.30, 3L);
        createFichier(7L, "cheque", "33", "emis", "27", "cheque-33-27", "Chèque", "env", 175, 158000.45, 3L);
        createFichier(8L, "cheque", "33", "recus", "28", "cheque-33-28", "Chèque", "env", 190, 172000.60, 3L);
    }

    // Méthode pour insérer les données CTR compatibles
    private void insertCTRCompatibleData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // CTR compatibles avec les FICHIERS (mêmes codes 30, 31, 32, 33)
        createCTR(1L, LocalDate.parse("2024-01-15", formatter), "CTR_cheque-30-21", 135, 115000.00);
        createCTR(2L, LocalDate.parse("2024-01-16", formatter), "CTR_cheque-30-22", 150, 130000.00);
        createCTR(3L, LocalDate.parse("2024-01-17", formatter), "CTR_cheque-31-23", 115, 95000.00);
        createCTR(4L, LocalDate.parse("2024-01-18", formatter), "CTR_cheque-31-24", 175, 160000.00);
        createCTR(5L, LocalDate.parse("2024-01-19", formatter), "CTR_cheque-32-25", 195, 180000.00);
        createCTR(6L, LocalDate.parse("2024-01-20", formatter), "CTR_cheque-32-26", 155, 140000.00);
        createCTR(7L, LocalDate.parse("2024-01-21", formatter), "CTR_cheque-33-27", 170, 155000.00);
        createCTR(8L, LocalDate.parse("2024-01-22", formatter), "CTR_cheque-33-28", 185, 170000.00);
    }

    // Méthode pour insérer les données CARTHAGO compatibles
    private void insertCarthagoCompatibleData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // CARTHAGO compatibles avec les FICHIERS (mêmes codes 30, 31, 32, 33)
        createCarthago(1L, LocalDate.parse("2024-01-15", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-30-21.txt", 130, 125, 110000.00, "image_30_21.jpg");
        createCarthago(2L, LocalDate.parse("2024-01-16", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-30-22.txt", 145, 140, 125000.00, "image_30_22.jpg");
        createCarthago(3L, LocalDate.parse("2024-01-17", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-31-23.txt", 110, 105, 90000.00, "image_31_23.jpg");
        createCarthago(4L, LocalDate.parse("2024-01-18", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-31-24.txt", 170, 165, 155000.00, "image_31_24.jpg");
        createCarthago(5L, LocalDate.parse("2024-01-19", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-32-25.txt", 190, 185, 175000.00, "image_32_25.jpg");
        createCarthago(6L, LocalDate.parse("2024-01-20", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-32-26.txt", 150, 145, 135000.00, "image_32_26.jpg");
        createCarthago(7L, LocalDate.parse("2024-01-21", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-33-27.txt", 165, 160, 150000.00, "image_33_27.jpg");
        createCarthago(8L, LocalDate.parse("2024-01-22", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-33-28.txt", 180, 175, 165000.00, "image_33_28.jpg");
    }

    // Méthode pour insérer les données CTR
    private void insertCTRData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // CTR pour Chèques 30
        createCTR(1L, LocalDate.parse("2024-01-15", formatter), "CTR_cheque-30-21", 140, 119999.97);
        createCTR(2L, LocalDate.parse("2024-01-16", formatter), "CTR_cheque-30-22", 155, 135000.50);
        createCTR(3L, LocalDate.parse("2024-01-17", formatter), "CTR_cheque-30-23", 120, 98000.25);
        createCTR(4L, LocalDate.parse("2024-01-18", formatter), "CTR_cheque-30-24", 180, 165000.75);
        createCTR(5L, LocalDate.parse("2024-01-19", formatter), "CTR_cheque-30-25", 200, 185000.00);
        createCTR(6L, LocalDate.parse("2024-01-20", formatter), "CTR_cheque-31-26", 160, 145000.30);
        createCTR(7L, LocalDate.parse("2024-01-21", formatter), "CTR_cheque-31-27", 175, 158000.45);
        createCTR(8L, LocalDate.parse("2024-01-22", formatter), "CTR_cheque-31-28", 190, 172000.60);
        createCTR(9L, LocalDate.parse("2024-01-23", formatter), "CTR_cheque-32-29", 210, 195000.80);
        createCTR(10L, LocalDate.parse("2024-01-24", formatter), "CTR_cheque-32-30", 225, 210000.90);
        createCTR(11L, LocalDate.parse("2024-01-25", formatter), "CTR_cheque-33-31", 130, 115000.20);
        createCTR(12L, LocalDate.parse("2024-01-26", formatter), "CTR_cheque-33-32", 145, 128000.35);
        createCTR(13L, LocalDate.parse("2024-01-27", formatter), "CTR_cheque-30-33", 150, 135000.40);
        createCTR(14L, LocalDate.parse("2024-01-28", formatter), "CTR_cheque-31-34", 165, 148000.55);
        createCTR(15L, LocalDate.parse("2024-01-29", formatter), "CTR_cheque-32-35", 170, 155000.65);
    }

    // Méthode pour insérer les données CARTHAGO
    private void insertCarthagoData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // CARTHAGO pour Chèques 30
        createCarthago(1L, LocalDate.parse("2024-01-15", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-30-21.txt", 135, 130, 115000.00, "image_30_21.jpg");
        createCarthago(2L, LocalDate.parse("2024-01-16", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-30-22.txt", 150, 145, 130000.00, "image_30_22.jpg");
        createCarthago(3L, LocalDate.parse("2024-01-17", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-30-23.txt", 115, 110, 95000.00, "image_30_23.jpg");
        createCarthago(4L, LocalDate.parse("2024-01-18", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-30-24.txt", 175, 170, 160000.00, "image_30_24.jpg");
        createCarthago(5L, LocalDate.parse("2024-01-19", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-30-25.txt", 195, 190, 180000.00, "image_30_25.jpg");
        createCarthago(6L, LocalDate.parse("2024-01-20", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-31-26.txt", 155, 150, 140000.00, "image_31_26.jpg");
        createCarthago(7L, LocalDate.parse("2024-01-21", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-31-27.txt", 170, 165, 155000.00, "image_31_27.jpg");
        createCarthago(8L, LocalDate.parse("2024-01-22", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-31-28.txt", 185, 180, 170000.00, "image_31_28.jpg");
        createCarthago(9L, LocalDate.parse("2024-01-23", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-32-29.txt", 205, 200, 190000.00, "image_32_29.jpg");
        createCarthago(10L, LocalDate.parse("2024-01-24", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-32-30.txt", 220, 215, 205000.00, "image_32_30.jpg");
        createCarthago(11L, LocalDate.parse("2024-01-25", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-33-31.txt", 125, 120, 110000.00, "image_33_31.jpg");
        createCarthago(12L, LocalDate.parse("2024-01-26", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-33-32.txt", 140, 135, 125000.00, "image_33_32.jpg");
        createCarthago(13L, LocalDate.parse("2024-01-27", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-30-33.txt", 145, 140, 130000.00, "image_30_33.jpg");
        createCarthago(14L, LocalDate.parse("2024-01-28", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-31-34.txt", 160, 155, 145000.00, "image_31_34.jpg");
        createCarthago(15L, LocalDate.parse("2024-01-29", formatter), "Valide", "Généré",
                "CARTHAGO_cheque-32-35.txt", 165, 160, 150000.00, "image_32_35.jpg");
    }

    // Méthode utilitaire pour créer un FICHIER
    private void createFichier(Long id, String typeFichier, String codeValeur, String sens,
            String codEn, String nomFichier, String natureFichier,
            String formatFichier, Integer nombre, Double montant, Long userId) {
        Fichier fichier = new Fichier();
        fichier.setId(id);
        fichier.setTypeFichier(typeFichier);
        fichier.setCodeValeur(codeValeur);
        fichier.setSens(sens);
        fichier.setCodEn(codEn);
        fichier.setNomFichier(nomFichier);
        fichier.setNatureFichier(natureFichier);
        fichier.setFormatFichier(formatFichier);
        fichier.setNombre(nombre);
        fichier.setMontant(montant);
        // Créer un utilisateur temporaire pour le fichier
        User user = new User();
        user.setId(userId);
        fichier.setUser(user);
        fichierService.createFichier(fichier);
    }

    // Méthode utilitaire pour créer un CTR
    private void createCTR(Long id, LocalDate dateCtr, String fichierRef, Integer nbChequesCtr,
            Double montantTotalCtr) {
        CTR ctr = new CTR();
        ctr.setId(id);
        ctr.setCreatedAt(dateCtr);

        // Créer un Fichier temporaire
        Fichier fichier = new Fichier();
        fichier.setNomFichier(fichierRef);
        fichier.setNombre(nbChequesCtr);
        fichier.setMontant(montantTotalCtr);
        fichier.setTypeFichier("ENCAISSE_VALEUR");
        fichier.setCodeValeur("EV");
        fichier.setSens("D");
        fichier.setCodEn("ENC");
        fichier.setNatureFichier("REMISE");
        fichier.setFormatFichier("TXT");

        // Créer un utilisateur temporaire
        User user = new User();
        user.setId(1L);
        fichier.setUser(user);

        // Créer un Carthago temporaire
        Carthago carthago = new Carthago();
        carthago.setNomFichier(fichierRef + ".DATA");
        carthago.setDateImport(dateCtr);
        carthago.setContenu("Contenu du fichier " + fichierRef);

        // Définir les propriétés de comparaison
        ctr.setFichier(fichier);
        ctr.setCarthago(carthago);
        ctr.setNombreTotalCarthago(nbChequesCtr);
        ctr.setMontantTotalCarthago(BigDecimal.valueOf(montantTotalCtr));
        ctr.setNombreDepCarthago(nbChequesCtr - 5); // Simuler quelques rejets
        ctr.setMontantDepCarthago(BigDecimal.valueOf(montantTotalCtr - 5000.0));
        ctr.setNombreRejCarthago(5);
        ctr.setMontantRejCarthago(BigDecimal.valueOf(5000.0));
        ctr.setNombreFichier(nbChequesCtr);
        ctr.setMontantFichier(BigDecimal.valueOf(montantTotalCtr));
        ctr.setNombreOk("Y");
        ctr.setMontantOk("Y");

        ctrService.createCTR(ctr);
    }

    // Méthode utilitaire pour créer un CARTHAGO
    private void createCarthago(Long id, LocalDate dateRemise, String etatRemise, String situationFichier,
            String fichierUpload, Integer chequeController, Integer chequeVerifier,
            Double montantTotal, String image) {
        Carthago carthago = new Carthago();
        carthago.setId(id);
        carthago.setNomFichier(fichierUpload);
        carthago.setDateImport(dateRemise);
        carthago.setContenu("Contenu du fichier " + fichierUpload);

        // Créer des détails Carthago
        List<CarthagoDetail> details = new ArrayList<>();
        for (int i = 1; i <= chequeController; i++) {
            CarthagoDetail detail = new CarthagoDetail();
            detail.setNumeroCheque("CHEQUE_" + id + "_" + i);
            detail.setMontant(montantTotal / chequeController);
            detail.setEtatCheque(i <= chequeVerifier ? "DEPOSE" : "REJETE");
            detail.setLigneOrigine("Ligne " + i + " du fichier " + fichierUpload);
            detail.setCarthago(carthago);
            details.add(detail);
        }
        carthago.setCarthagoDetails(details);

        carthagoService.createCarthago(carthago);
    }
}*/
