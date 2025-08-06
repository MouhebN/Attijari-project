//package tn.esprit.ruya.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import tn.esprit.ruya.services.CTRComparisonService;
//import tn.esprit.ruya.services.CarthagoParserService;
//
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/dashboard")
//public class DashboardController {
//
//    @Autowired
//    private CTRComparisonService ctrComparisonService;
//
//    @Autowired
//    private CarthagoParserService carthagoParserService;
//
//    /**
//     * Récupère les statistiques générales du dashboard
//     */
//    @GetMapping("/stats")
//    public ResponseEntity<Map<String, Object>> getDashboardStats() {
//        try {
//            CTRComparisonService.DashboardStats stats = ctrComparisonService.calculateDashboardStats();
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            Map<String, Object> statsMap = new HashMap<>();
//            statsMap.put("nombreTotalComparaisons", stats.nombreTotalComparaisons);
//            statsMap.put("nombreComparaisonsOK", stats.nombreComparaisonsOK);
//            statsMap.put("nombreComparaisonsKO", stats.nombreComparaisonsKO);
//            statsMap.put("tauxConcordance", stats.tauxConcordance);
//            statsMap.put("montantTotalCarthago", stats.montantTotalCarthago);
//            statsMap.put("montantTotalEncaisse", stats.montantTotalEncaisse);
//            response.put("stats", statsMap);
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "Erreur lors du calcul des statistiques: " + e.getMessage());
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//    }
//
//    /**
//     * Récupère les fichiers avec anomalies
//     */
//    @GetMapping("/anomalies")
//    public ResponseEntity<Map<String, Object>> getFichiersAvecAnomalies() {
//        try {
//            List<Map<String, Object>> anomalies = ctrComparisonService.getFichiersAvecAnomalies()
//                    .stream()
//                    .map(ctr -> {
//                        Map<String, Object> anomalyMap = new HashMap<>();
//                        anomalyMap.put("id", ctr.getId());
//                        anomalyMap.put("fichierNom", ctr.getFichier() != null ? ctr.getFichier().getNomFichier() : "N/A");
//                        anomalyMap.put("carthagoNom", ctr.getCarthago() != null ? ctr.getCarthago().getNomFichier() : "N/A");
//                        anomalyMap.put("nombreOk", ctr.getNombreOk());
//                        anomalyMap.put("montantOk", ctr.getMontantOk());
//                        anomalyMap.put("nombreFichier", ctr.getNombreFichier());
//                        anomalyMap.put("montantFichier", ctr.getMontantFichier());
//                        anomalyMap.put("nombreCarthago", ctr.getNombreTotalCarthago());
//                        anomalyMap.put("montantCarthago", ctr.getMontantTotalCarthago());
//                        anomalyMap.put("createdAt", ctr.getCreatedAt());
//                        return anomalyMap;
//                    })
//                    .toList();
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("anomalies", anomalies);
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "Erreur lors de la récupération des anomalies: " + e.getMessage());
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//    }
//
//    /**
//     * Récupère les statistiques par date
//     */
//    @GetMapping("/stats-by-date")
//    public ResponseEntity<Map<String, Object>> getStatsByDate(
//            @RequestParam(required = false) String date) {
//        try {
//            LocalDate targetDate = date != null ? LocalDate.parse(date) : LocalDate.now();
//            CTRComparisonService.DateStats stats = ctrComparisonService.getStatsByDate(targetDate);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            Map<String, Object> statsMap = new HashMap<>();
//            statsMap.put("date", stats.date);
//            statsMap.put("nombreComparaisons", stats.nombreComparaisons);
//            statsMap.put("nombreOK", stats.nombreOK);
//            statsMap.put("nombreKO", stats.nombreKO);
//            statsMap.put("tauxOK", stats.getTauxOK());
//            response.put("stats", statsMap);
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "Erreur lors de la récupération des statistiques par date: " + e.getMessage());
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//    }
//
//    /**
//     * Compare automatiquement tous les fichiers non traités
//     */
//    @PostMapping("/compare-all")
//    public ResponseEntity<Map<String, Object>> compareAllUnprocessed() {
//        try {
//            int count = ctrComparisonService.compareAllUnprocessed();
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("message", count + " comparaisons effectuées");
//            response.put("nombreComparaisons", count);
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "Erreur lors de la comparaison: " + e.getMessage());
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//    }
//
//    /**
//     * Récupère les indicateurs de performance
//     */
//    @GetMapping("/performance")
//    public ResponseEntity<Map<String, Object>> getPerformanceIndicators() {
//        try {
//            CTRComparisonService.DashboardStats stats = ctrComparisonService.calculateDashboardStats();
//
//            // Calculer les indicateurs de performance
//            double tauxEfficacite = stats.nombreTotalComparaisons > 0
//                    ? (double) stats.nombreComparaisonsOK / stats.nombreTotalComparaisons * 100
//                    : 0.0;
//
//            double ecartMontant = stats.montantTotalCarthago.compareTo(stats.montantTotalEncaisse) == 0 ? 0.0
//                    : Math.abs(stats.montantTotalCarthago.doubleValue() - stats.montantTotalEncaisse.doubleValue());
//
//            Map<String, Object> indicators = new HashMap<>();
//            indicators.put("tauxEfficacite", tauxEfficacite);
//            indicators.put("ecartMontant", ecartMontant);
//            indicators.put("nombreAnomalies", stats.nombreComparaisonsKO);
//            indicators.put("montantTotalTraite", stats.montantTotalCarthago.add(stats.montantTotalEncaisse).divide(java.math.BigDecimal.valueOf(2)));
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("indicators", indicators);
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "Erreur lors du calcul des indicateurs: " + e.getMessage());
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//    }
//
//    /**
//     * Récupère les données pour les graphiques
//     */
//    @GetMapping("/charts")
//    public ResponseEntity<Map<String, Object>> getChartData() {
//        try {
//            CTRComparisonService.DashboardStats stats = ctrComparisonService.calculateDashboardStats();
//
//            // Données pour le graphique camembert (dépôts vs rejets)
//            Map<String, Object> pieChartData = new HashMap<>();
//            pieChartData.put("labels", List.of("Dépôts", "Rejets"));
//            pieChartData.put("data", List.of(stats.nombreComparaisonsOK, stats.nombreComparaisonsKO));
//            pieChartData.put("backgroundColor", List.of("#28a745", "#dc3545"));
//
//            // Données pour l'histogramme (montants)
//            Map<String, Object> barChartData = new HashMap<>();
//            barChartData.put("labels", List.of("Carthago", "Encaisse Valeur"));
//            barChartData.put("data", List.of(stats.montantTotalCarthago, stats.montantTotalEncaisse));
//            barChartData.put("backgroundColor", List.of("#007bff", "#6c757d"));
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("pieChart", pieChartData);
//            response.put("barChart", barChartData);
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(Map.of(
//                    "success", false,
//                    "message", "Erreur lors de la récupération des données graphiques: " + e.getMessage()));
//        }
//    }
//}