package tn.esprit.ruya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ruya.models.CTR;
import tn.esprit.ruya.models.Carthago;
import tn.esprit.ruya.models.ResultatComparaison;
import tn.esprit.ruya.services.CTRService;
import tn.esprit.ruya.services.CarthagoService;
import tn.esprit.ruya.services.ResultatComparaisonService;
import tn.esprit.ruya.Fichier.service.FichierServ;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/suivi-ctr-carthago")
@CrossOrigin(origins = "http://localhost:4200")
public class SuiviCTRCarthagoController {

    @Autowired
    private CTRService ctrService;

    @Autowired
    private CarthagoService carthagoService;

    @Autowired
    private ResultatComparaisonService resultatComparaisonService;

    @Autowired
    private FichierServ fichierService;

    // ==================== ENDPOINTS CTR ====================

    @GetMapping("/ctr")
    public ResponseEntity<List<CTR>> getAllCTR() {
        List<CTR> ctrList = ctrService.getAllCTR();
        return ResponseEntity.ok(ctrList);
    }

    @GetMapping("/ctr/{id}")
    public ResponseEntity<CTR> getCTRById(@PathVariable Long id) {
        return ctrService.getCTRById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/ctr")
    public ResponseEntity<CTR> createCTR(@RequestBody CTR ctr) {
        CTR createdCTR = ctrService.createCTR(ctr);
        return ResponseEntity.ok(createdCTR);
    }

    @PutMapping("/ctr/{id}")
    public ResponseEntity<CTR> updateCTR(@PathVariable Long id, @RequestBody CTR ctrDetails) {
        CTR updatedCTR = ctrService.updateCTR(id, ctrDetails);
        if (updatedCTR != null) {
            return ResponseEntity.ok(updatedCTR);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/ctr/{id}")
    public ResponseEntity<Void> deleteCTR(@PathVariable Long id) {
        if (ctrService.deleteCTR(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/ctr/date/{date}")
    public ResponseEntity<List<CTR>> getCTRByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CTR> ctrList = ctrService.getCTRByDate(date);
        return ResponseEntity.ok(ctrList);
    }

    @GetMapping("/ctr/date-range")
    public ResponseEntity<List<CTR>> getCTRBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<CTR> ctrList = ctrService.getCTRBetweenDates(dateDebut, dateFin);
        return ResponseEntity.ok(ctrList);
    }

    @GetMapping("/ctr/stats/date-range")
    public ResponseEntity<Map<String, Object>> getCTRStatsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        Map<String, Object> stats = ctrService.getStatistiquesByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/ctr/stats/global")
    public ResponseEntity<Map<String, Object>> getCTRGlobalStats() {
        Map<String, Object> stats = ctrService.getStatistiquesGlobales();
        return ResponseEntity.ok(stats);
    }

    // ==================== ENDPOINTS CARTHAGO ====================

    @GetMapping("/carthago")
    public ResponseEntity<List<Carthago>> getAllCarthago() {
        List<Carthago> carthagoList = carthagoService.getAllCarthago();
        return ResponseEntity.ok(carthagoList);
    }

    @GetMapping("/carthago/{id}")
    public ResponseEntity<Carthago> getCarthagoById(@PathVariable Long id) {
        return carthagoService.getCarthagoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/carthago")
    public ResponseEntity<Carthago> createCarthago(@RequestBody Carthago carthago) {
        Carthago createdCarthago = carthagoService.createCarthago(carthago);
        return ResponseEntity.ok(createdCarthago);
    }

    @PutMapping("/carthago/{id}")
    public ResponseEntity<Carthago> updateCarthago(@PathVariable Long id, @RequestBody Carthago carthagoDetails) {
        Carthago updatedCarthago = carthagoService.updateCarthago(id, carthagoDetails);
        if (updatedCarthago != null) {
            return ResponseEntity.ok(updatedCarthago);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/carthago/{id}")
    public ResponseEntity<Void> deleteCarthago(@PathVariable Long id) {
        if (carthagoService.deleteCarthago(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/carthago/date/{date}")
    public ResponseEntity<List<Carthago>> getCarthagoByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Carthago> carthagoList = carthagoService.getCarthagoByDate(date);
        return ResponseEntity.ok(carthagoList);
    }

    @GetMapping("/carthago/date-range")
    public ResponseEntity<List<Carthago>> getCarthagoBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<Carthago> carthagoList = carthagoService.getCarthagoBetweenDates(dateDebut, dateFin);
        return ResponseEntity.ok(carthagoList);
    }

    @GetMapping("/carthago/etat/{etatRemise}")
    public ResponseEntity<List<Carthago>> getCarthagoByEtatRemise(@PathVariable String etatRemise) {
        List<Carthago> carthagoList = carthagoService.getCarthagoByEtatRemise(etatRemise);
        return ResponseEntity.ok(carthagoList);
    }

    @GetMapping("/carthago/situation/{situationFichier}")
    public ResponseEntity<List<Carthago>> getCarthagoBySituationFichier(@PathVariable String situationFichier) {
        List<Carthago> carthagoList = carthagoService.getCarthagoBySituationFichier(situationFichier);
        return ResponseEntity.ok(carthagoList);
    }

    @GetMapping("/carthago/stats/date-range")
    public ResponseEntity<Map<String, Object>> getCarthagoStatsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        Map<String, Object> stats = carthagoService.getStatistiquesByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/carthago/stats/global")
    public ResponseEntity<Map<String, Object>> getCarthagoGlobalStats() {
        Map<String, Object> stats = carthagoService.getStatistiquesGlobales();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/carthago/stats/etat")
    public ResponseEntity<List<Object[]>> getCarthagoStatsByEtat() {
        List<Object[]> stats = carthagoService.getStatistiquesParEtat();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/carthago/stats/situation")
    public ResponseEntity<List<Object[]>> getCarthagoStatsBySituation() {
        List<Object[]> stats = carthagoService.getStatistiquesParSituation();
        return ResponseEntity.ok(stats);
    }

    // ==================== ENDPOINTS RÉSULTATS COMPARAISON ====================

    @GetMapping("/resultats")
    public ResponseEntity<List<ResultatComparaison>> getAllResultatsComparaison() {
        List<ResultatComparaison> resultats = resultatComparaisonService.getAllResultatsComparaison();
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/resultats/{id}")
    public ResponseEntity<ResultatComparaison> getResultatComparaisonById(@PathVariable Long id) {
        return resultatComparaisonService.getResultatComparaisonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/resultats")
    public ResponseEntity<ResultatComparaison> createResultatComparaison(
            @RequestBody ResultatComparaison resultatComparaison) {
        ResultatComparaison createdResultat = resultatComparaisonService.createResultatComparaison(resultatComparaison);
        return ResponseEntity.ok(createdResultat);
    }

    @PutMapping("/resultats/{id}")
    public ResponseEntity<ResultatComparaison> updateResultatComparaison(@PathVariable Long id,
            @RequestBody ResultatComparaison resultatDetails) {
        ResultatComparaison updatedResultat = resultatComparaisonService.updateResultatComparaison(id, resultatDetails);
        if (updatedResultat != null) {
            return ResponseEntity.ok(updatedResultat);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/resultats/{id}")
    public ResponseEntity<Void> deleteResultatComparaison(@PathVariable Long id) {
        if (resultatComparaisonService.deleteResultatComparaison(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/resultats/global/{resultatGlobal}")
    public ResponseEntity<List<ResultatComparaison>> getResultatsByResultatGlobal(@PathVariable String resultatGlobal) {
        List<ResultatComparaison> resultats = resultatComparaisonService.getResultatsByResultatGlobal(resultatGlobal);
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/resultats/fichier/{fichierId}")
    public ResponseEntity<List<ResultatComparaison>> getResultatsByFichier(@PathVariable Long fichierId) {
        List<ResultatComparaison> resultats = resultatComparaisonService.getResultatsByFichier(fichierId);
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/resultats/ctr/{ctrId}")
    public ResponseEntity<List<ResultatComparaison>> getResultatsByCtr(@PathVariable Long ctrId) {
        List<ResultatComparaison> resultats = resultatComparaisonService.getResultatsByCtr(ctrId);
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/resultats/carthago/{carthagoId}")
    public ResponseEntity<List<ResultatComparaison>> getResultatsByCarthago(@PathVariable Long carthagoId) {
        List<ResultatComparaison> resultats = resultatComparaisonService.getResultatsByCarthago(carthagoId);
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/resultats/recents")
    public ResponseEntity<List<ResultatComparaison>> getResultatsRecents() {
        List<ResultatComparaison> resultats = resultatComparaisonService.getResultatsRecents();
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/resultats/differences")
    public ResponseEntity<List<ResultatComparaison>> getResultatsAvecDifferences(@RequestParam Double seuil) {
        List<ResultatComparaison> resultats = resultatComparaisonService.getResultatsAvecDifferences(seuil);
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/resultats/stats/global")
    public ResponseEntity<Map<String, Object>> getResultatsGlobalStats() {
        Map<String, Object> stats = resultatComparaisonService.getStatistiquesGlobales();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/resultats/stats/comparaison")
    public ResponseEntity<Map<String, Object>> getResultatsComparaisonStats() {
        Map<String, Object> stats = resultatComparaisonService.getStatistiquesComparaison();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/resultats/stats/detaillees")
    public ResponseEntity<Map<String, Object>> getResultatsDetailleesStats() {
        Map<String, Object> stats = resultatComparaisonService.getStatistiquesDetaillees();
        return ResponseEntity.ok(stats);
    }

    // ==================== ENDPOINTS DASHBOARD GLOBAL ====================

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> dashboard = new HashMap<>();

        // Statistiques CTR
        Map<String, Object> ctrStats = ctrService.getStatistiquesGlobales();
        dashboard.put("ctrStats", ctrStats);

        // Statistiques Carthago
        Map<String, Object> carthagoStats = carthagoService.getStatistiquesGlobales();
        dashboard.put("carthagoStats", carthagoStats);

        // Statistiques Résultats
        Map<String, Object> resultatsStats = resultatComparaisonService.getStatistiquesDetaillees();
        dashboard.put("resultatsStats", resultatsStats);

        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/dashboard/date-range")
    public ResponseEntity<Map<String, Object>> getDashboardDataByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        Map<String, Object> dashboard = new HashMap<>();

        // Statistiques CTR par plage de dates
        Map<String, Object> ctrStats = ctrService.getStatistiquesByDateRange(dateDebut, dateFin);
        dashboard.put("ctrStats", ctrStats);

        // Statistiques Carthago par plage de dates
        Map<String, Object> carthagoStats = carthagoService.getStatistiquesByDateRange(dateDebut, dateFin);
        dashboard.put("carthagoStats", carthagoStats);

        return ResponseEntity.ok(dashboard);
    }

    // ==================== NOUVEAUX ENDPOINTS POUR LES CARTES ====================

    // Statistiques des Remises Saisies
    @GetMapping("/stats/remises")
    public ResponseEntity<Map<String, Object>> getRemisesStats() {
        Map<String, Object> stats = new HashMap<>();

        // Statistiques des remises client externe et web
        long nombreRemises = ctrService.countAllCTR() + carthagoService.countAllCarthago();
        BigDecimal montantTotal = ctrService.getTotalMontantCTR().add(carthagoService.getTotalMontantCarthago());
        long fichiersValides = carthagoService.countCarthagoByEtat("Valide"); // Seulement CARTHAGO pour l'instant
        long remisesErreur = carthagoService.countCarthagoByEtat("Non Valide"); // Seulement CARTHAGO pour l'instant

        stats.put("nombreRemises", nombreRemises);
        stats.put("montantTotal", montantTotal);
        stats.put("fichiersValides", fichiersValides);
        stats.put("remisesErreur", remisesErreur);

        return ResponseEntity.ok(stats);
    }

    // Statistiques CARTHAGO détaillées
    @GetMapping("/stats/carthago")
    public ResponseEntity<Map<String, Object>> getCarthagoStats() {
        Map<String, Object> stats = carthagoService.getCarthagoStatsForCards();
        return ResponseEntity.ok(stats);
    }

    // Statistiques CTR détaillées
    @GetMapping("/stats/ctr")
    public ResponseEntity<Map<String, Object>> getCTRStats() {
        Map<String, Object> stats = ctrService.getCTRStatsForCards();
        return ResponseEntity.ok(stats);
    }

        // Statistiques des Fichiers par Code Valeur
    @GetMapping("/stats/fichiers-code-valeur")
    public ResponseEntity<Map<String, Object>> getFichiersCodeValeurStats() {
        Map<String, Object> stats = fichierService.getStatsForAllCodeValeurs();
        return ResponseEntity.ok(stats);
    }

    // Action de régénération CARTHAGO
    @PostMapping("/carthago/regenerer")
    public ResponseEntity<Map<String, Object>> regenererCarthago() {
        Map<String, Object> response = new HashMap<>();

        try {
            // Logique de régénération CARTHAGO
            // carthagoService.regenererCarthago();

            response.put("success", true);
            response.put("message", "Régénération CARTHAGO initiée avec succès");
            response.put("timestamp", java.time.LocalDateTime.now());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la régénération CARTHAGO: " + e.getMessage());
            response.put("timestamp", java.time.LocalDateTime.now());

            return ResponseEntity.status(500).body(response);
        }
    }

    // Résumé global
    @GetMapping("/stats/resume-global")
    public ResponseEntity<Map<String, Object>> getResumeGlobal() {
        Map<String, Object> stats = new HashMap<>();

        long totalRemises = ctrService.countAllCTR() + carthagoService.countAllCarthago();
        long totalCheques = ctrService.getTotalChequesCTR() + carthagoService.getTotalChequesControles()
                + carthagoService.getTotalChequesVerifies();
        BigDecimal totalMontant = ctrService.getTotalMontantCTR().add(carthagoService.getTotalMontantCarthago());
        long totalImages = carthagoService.countCarthagoWithImages();

        stats.put("totalRemises", totalRemises);
        stats.put("totalCheques", totalCheques);
        stats.put("totalMontant", totalMontant);
        stats.put("totalImages", totalImages);

        return ResponseEntity.ok(stats);
    }
}