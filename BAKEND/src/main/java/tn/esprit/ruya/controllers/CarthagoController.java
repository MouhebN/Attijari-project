package tn.esprit.ruya.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.ruya.models.Carthago;
import tn.esprit.ruya.models.CarthagoDetail;
import tn.esprit.ruya.services.CarthagoParserService;
import tn.esprit.ruya.services.CarthagoParserService.CarthagoStats;
import tn.esprit.ruya.services.CarthagoService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carthago")
public class CarthagoController {

    private final CarthagoParserService carthagoParserService;
    private final CarthagoService carthagoService;

    public CarthagoController(CarthagoParserService carthagoParserService, CarthagoService carthagoService) {
        this.carthagoParserService = carthagoParserService;
        this.carthagoService = carthagoService;
    }

    @GetMapping("/names")
    public ResponseEntity<List<Map<String, Object>>> getCarthagoNames() {
        List<Carthago> carthagos = carthagoService.getAllCarthago();

        List<Map<String, Object>> result = carthagos.stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("nomFichier", c.getNomFichier());
            return map;
        }).toList();

        return ResponseEntity.ok(result);
    }

    /**
     * Import d'un fichier Carthago via upload
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadCarthagoFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("nomFichier") String nomFichier) {

        try {
            // 1. Lire le contenu brut
            String contenuBrut = new String(file.getBytes(), StandardCharsets.UTF_8);

            // 2. Créer l’objet Carthago
            Carthago carthago = new Carthago(nomFichier, contenuBrut);

            // 3. Parser le contenu (retourne CarthagoParseResult)
            CarthagoParserService.CarthagoParseResult parseResult = carthagoParserService.parseCarthagoFile(contenuBrut);

            // 4. Attacher les valeurs d'entête (nombre/montant)
            if (parseResult.entete != null) {
                carthago.setNombreEntete(parseResult.entete.nombreTotal);
                carthago.setMontantEntete(parseResult.entete.montantTotal != null ? parseResult.entete.montantTotal.doubleValue() : null);
            }

            // 5. Attacher les statuts de concordance (nombre/montant)
            if (parseResult.stats != null) {
                carthago.setNombreOk(parseResult.stats.nombreOk);
                carthago.setMontantOk(parseResult.stats.montantOk);
            }

            // 6. Sauvegarder dans la base
            carthagoService.saveParsedCarthago(carthago, parseResult.details);

            // 7. Préparer la réponse
            Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Fichier Carthago importé et parsé avec succès",
                    "nomFichier", nomFichier,
                    "nombreLignes", parseResult.details.size(),
                    "statistiques", parseResult.stats,
                    "nombreOk", parseResult.stats.nombreOk,
                    "montantOk", parseResult.stats.montantOk
            );

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erreur lors de la lecture du fichier: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erreur lors du parsing: " + e.getMessage()));
        }
    }


    @GetMapping("/stats/entete")
    public ResponseEntity<Map<String, Object>> getEnteteStats() {
        System.out.println("Controller: /stats/entete called");
        return ResponseEntity.ok(carthagoService.getEnteteStats());
    }
}
