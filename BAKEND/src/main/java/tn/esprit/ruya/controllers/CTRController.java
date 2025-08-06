package tn.esprit.ruya.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ruya.models.CTR;
import tn.esprit.ruya.models.CTRDto;
import tn.esprit.ruya.repositories.CTRRepository;
import tn.esprit.ruya.repositories.CarthagoDetailRepository;
import tn.esprit.ruya.services.CTRComparisonService;
import tn.esprit.ruya.services.CTRService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ctr")
@CrossOrigin(origins = "http://localhost:4200")
public class CTRController {

    private final CTRService ctrService;
    private final CTRComparisonService ctrComparisonService;
    private final CarthagoDetailRepository carthagoDetailRepository;
    private final CTRRepository ctrRepository;

    public CTRController(CTRService ctrService, CTRComparisonService ctrComparisonService, CarthagoDetailRepository carthagoDetailRepository, CTRRepository ctrRepository) {
        this.ctrService = ctrService;
        this.ctrComparisonService = ctrComparisonService;
        this.carthagoDetailRepository = carthagoDetailRepository;
        this.ctrRepository = ctrRepository;
    }


    @GetMapping
    public ResponseEntity<List<CTRDto>> getAllCTRs() {
        List<CTR> ctrs = ctrService.getAllCTR();
        List<CTRDto> dtoList = ctrs.stream().map(CTRDto::from).toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CTRDto> getCTRById(@PathVariable Long id) {
        Optional<CTR> ctr = ctrService.getCTRById(id);
        return ctr.map(c -> ResponseEntity.ok(CTRDto.from(c)))
                .orElse(ResponseEntity.notFound().build());
    }


    // Mettre à jour un CTR
    @PutMapping("/{id}")
    public ResponseEntity<CTR> updateCTR(@PathVariable Long id, @RequestBody CTR ctrDetails) {
        CTR updatedCTR = ctrService.updateCTR(id, ctrDetails);
        if (updatedCTR != null) {
            return ResponseEntity.ok(updatedCTR);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un CTR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCTR(@PathVariable Long id) {
        boolean deleted = ctrService.deleteCTR(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Compare un fichier EV avec un fichier Carthago
     */
    @PostMapping("/comparer")
    public ResponseEntity<CTRDto> comparerFichierEtCarthago(
            @RequestParam Long fichierId,
            @RequestParam Long carthagoId) {
        System.out.println("fichierId=" + fichierId + ", carthagoId=" + carthagoId);
        CTR result = ctrComparisonService.compareFichiers(fichierId, carthagoId);
        return ResponseEntity.ok(CTRDto.from(result));
    }


    /**
     * Compare tous les fichiers non traités
     */
    @PostMapping("/comparer-tous")
    public ResponseEntity<String> comparerTous() {
        int nbComparaisons = ctrComparisonService.compareAllUnprocessed();
        return ResponseEntity.ok("Comparaisons effectuées : " + nbComparaisons);
    }
    @GetMapping("/stats/concordance-interne")
    public ResponseEntity<Double> getTauxConcordanceInterne() {
        double taux = ctrService.tauxConcordanceInterneCarthago();
        return ResponseEntity.ok(taux);
    }
    @GetMapping("/stats/concordance-ev-carthago")
    public ResponseEntity<Double> getTauxConcordanceEV() {
        double taux = ctrService.tauxConcordanceCTR();
        return ResponseEntity.ok(taux);
    }
    @GetMapping("/stats/repartition-etat")
    public ResponseEntity<Map<String, Long>> getRepartitionEtat() {
        long depose = carthagoDetailRepository.countByEtatCheque("DEPOSE");
        long rejete = carthagoDetailRepository.countByEtatCheque("REJETE");

        Map<String, Long> result = new HashMap<>();
        result.put("DEPOSE", depose);
        result.put("REJETE", rejete);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/stats/montant-etat")
    public ResponseEntity<Map<String, Double>> getMontantsEtat() {
        Double depose = carthagoDetailRepository.sumMontantByEtat("DEPOSE");
        Double rejete = carthagoDetailRepository.sumMontantByEtat("REJETE");

        Map<String, Double> result = new HashMap<>();
        result.put("DEPOSE", depose != null ? depose : 0.0);
        result.put("REJETE", rejete != null ? rejete : 0.0);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/anomalies")
    public ResponseEntity<List<CTRDto>> getCTRAnomalies() {
        List<CTR> anomalies = ctrRepository.findAnomaliesWithFichier();
        List<CTRDto> dtos = anomalies.stream().map(CTRDto::from).toList();
        return ResponseEntity.ok(dtos);
    }




}