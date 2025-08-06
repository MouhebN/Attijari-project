package tn.esprit.ruya.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.ruya.models.CTR;
import tn.esprit.ruya.models.Carthago;
import tn.esprit.ruya.repositories.CTRRepository;
import tn.esprit.ruya.repositories.CarthagoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Service
public class CTRService {


    private final CTRRepository ctrRepository;
    private final  CarthagoRepository carthagoRepository;

    public CTRService(CTRRepository ctrRepository, CarthagoRepository carthagoRepository) {
        this.ctrRepository = ctrRepository;
        this.carthagoRepository = carthagoRepository;
    }
    public double tauxConcordanceCTR() {
        List<CTR> all = ctrRepository.findAll();
        long total = all.size();
        long concordants = all.stream()
                .filter(c -> "Y".equals(c.getNombreOk()) && "Y".equals(c.getMontantOk()))
                .count();

        return total > 0 ? (concordants * 100.0) / total : 0.0;
    }

    public double tauxConcordanceInterneCarthago() {
        List<Carthago> all = carthagoRepository.findAll();
        long total = all.size();
        long concordants = all.stream()
                .filter(c -> "Y".equals(c.getNombreOk()) && "Y".equals(c.getMontantOk()))
                .count();

        return total > 0 ? (concordants * 100.0) / total : 0.0;
    }

    public List<CTR> getAllCTR() {
        return ctrRepository.findAllWithFichier();
    }


    // Récupérer un CTR par ID
    public Optional<CTR> getCTRById(Long id) {
        return ctrRepository.findById(id);
    }

    // Créer un nouveau CTR
    public CTR createCTR(CTR ctr) {
        return ctrRepository.save(ctr);
    }

    // Mettre à jour un CTR
    public CTR updateCTR(Long id, CTR ctrDetails) {
        Optional<CTR> ctrOptional = ctrRepository.findById(id);
        if (ctrOptional.isPresent()) {
            CTR ctr = ctrOptional.get();
            ctr.setFichier(ctrDetails.getFichier());
            ctr.setCarthago(ctrDetails.getCarthago());
            ctr.setNombreTotalCarthago(ctrDetails.getNombreTotalCarthago());
            ctr.setMontantTotalCarthago(ctrDetails.getMontantTotalCarthago());
            ctr.setNombreDepCarthago(ctrDetails.getNombreDepCarthago());
            ctr.setMontantDepCarthago(ctrDetails.getMontantDepCarthago());
            ctr.setNombreRejCarthago(ctrDetails.getNombreRejCarthago());
            ctr.setMontantRejCarthago(ctrDetails.getMontantRejCarthago());
            ctr.setNombreFichier(ctrDetails.getNombreFichier());
            ctr.setMontantFichier(ctrDetails.getMontantFichier());
            ctr.setNombreOk(ctrDetails.getNombreOk());
            ctr.setMontantOk(ctrDetails.getMontantOk());
            ctr.setCreatedAt(ctrDetails.getCreatedAt());
            return ctrRepository.save(ctr);
        }
        return null;
    }

    // Supprimer un CTR
    public boolean deleteCTR(Long id) {
        if (ctrRepository.existsById(id)) {
            ctrRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Récupérer les CTR par date
    public List<CTR> getCTRByDate(LocalDate date) {
        return ctrRepository.findByCreatedAt(date);
    }

    // Récupérer les CTR entre deux dates
    public List<CTR> getCTRBetweenDates(LocalDate dateDebut, LocalDate dateFin) {
        return ctrRepository.findByCreatedAtBetween(dateDebut, dateFin);
    }

    // Récupérer les CTR par référence de fichier
    public List<CTR> getCTRByFichierRef(String fichierRef) {
        return ctrRepository.findAll().stream()
                .filter(ctr -> ctr.getFichier() != null &&
                        ctr.getFichier().getNomFichier() != null &&
                        ctr.getFichier().getNomFichier().toLowerCase().contains(fichierRef.toLowerCase()))
                .toList();
    }

    // Récupérer les statistiques par plage de dates
    public Map<String, Object> getStatistiquesByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        Object[] stats = ctrRepository.getStatistiquesByDateRange(dateDebut, dateFin);
        Map<String, Object> result = new HashMap<>();

        if (stats != null && stats.length >= 4) {
            result.put("nombreCTR", stats[0]);
            result.put("totalCheques", stats[1]);
            result.put("totalMontant", stats[2]);
            result.put("montantMoyen", stats[3]);
        }

        return result;
    }

    // Récupérer les montants totaux par date
    public List<Object[]> getMontantsTotauxParDate() {
        return ctrRepository.getMontantsTotauxParDate();
    }

    // Récupérer le nombre de chèques par date
    public List<Object[]> getNombreChequesParDate() {
        return ctrRepository.getNombreChequesParDate();
    }

    // Récupérer les CTR avec nombre de chèques supérieur à une valeur
    public List<CTR> getCTRWithNbChequesGreaterThan(Integer nbCheques) {
        return ctrRepository.findByNombreTotalCarthagoGreaterThan(nbCheques);
    }

    // Récupérer les CTR avec montant supérieur à une valeur
    public List<CTR> getCTRWithMontantGreaterThan(Double montant) {
        return ctrRepository.findByMontantTotalCarthagoGreaterThan(montant);
    }

    // Obtenir les statistiques globales
    public Map<String, Object> getStatistiquesGlobales() {
        List<CTR> allCTR = ctrRepository.findAll();
        Map<String, Object> stats = new HashMap<>();

        long totalCTR = allCTR.size();
        int totalCheques = allCTR.stream().mapToInt(CTR::getNombreTotalCarthago).sum();
        BigDecimal totalMontant = allCTR.stream()
                .map(CTR::getMontantTotalCarthago)
                .filter(montant -> montant != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        double montantMoyen = totalCTR > 0 ? totalMontant.doubleValue() / totalCTR : 0;
        int nombreMoyenCheques = totalCTR > 0 ? totalCheques / (int) totalCTR : 0;

        stats.put("totalCTR", totalCTR);
        stats.put("totalCheques", totalCheques);
        stats.put("totalMontant", totalMontant);
        stats.put("montantMoyen", montantMoyen);
        stats.put("nombreMoyenCheques", nombreMoyenCheques);

        return stats;
    }

    // ==================== NOUVELLES MÉTHODES POUR LES CARTES ====================

    // Compter tous les CTR
    public long countAllCTR() {
        return ctrRepository.count();
    }

    // Obtenir le montant total des CTR
    public BigDecimal getTotalMontantCTR() {
        return ctrRepository.findAll().stream()
                .map(CTR::getMontantTotalCarthago)
                .filter(montant -> montant != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Obtenir le nombre total de chèques CTR
    public long getTotalChequesCTR() {
        return ctrRepository.findAll().stream()
                .mapToLong(ctr -> ctr.getNombreTotalCarthago() != null ? ctr.getNombreTotalCarthago() : 0L)
                .sum();
    }

    // Compter les CTR par statut (si le modèle CTR a un champ statut)
    public long countCTRByStatut(String statut) {
        // Cette méthode nécessite un champ statut dans le modèle CTR
        // Pour l'instant, on retourne 0 en attendant l'ajout du champ
        return 0;
    }

    // Compter les CTR par code valeur
    public long countCTRByCodeValeur(String codeValeur) {
        return ctrRepository.findAll().stream()
                .filter(ctr -> ctr.getFichier() != null &&
                        ctr.getFichier().getNomFichier() != null &&
                        ctr.getFichier().getNomFichier().contains(codeValeur))
                .count();
    }

    // Obtenir le montant total des CTR par code valeur
    public BigDecimal getTotalMontantCTRByCodeValeur(String codeValeur) {
        return ctrRepository.findAll().stream()
                .filter(ctr -> ctr.getFichier() != null &&
                        ctr.getFichier().getNomFichier() != null &&
                        ctr.getFichier().getNomFichier().contains(codeValeur))
                .map(CTR::getMontantTotalCarthago)
                .filter(montant -> montant != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Obtenir les statistiques CTR pour les cartes
    public Map<String, Object> getCTRStatsForCards() {
        List<CTR> allCTR = ctrRepository.findAll();
        Map<String, Object> stats = new HashMap<>();

        long totalCheques = allCTR.stream()
                .mapToLong(ctr -> ctr.getNombreTotalCarthago() != null ? ctr.getNombreTotalCarthago() : 0L)
                .sum();
        BigDecimal totalMontant = allCTR.stream()
                .map(CTR::getMontantTotalCarthago)
                .filter(montant -> montant != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Pour les statistiques par statut, on simule des données basées sur des
        // critères
        long chequesVerifies = allCTR.stream()
                .filter(ctr -> ctr.getMontantTotalCarthago() != null
                        && ctr.getMontantTotalCarthago().compareTo(BigDecimal.ZERO) > 0 &&
                        ctr.getNombreTotalCarthago() != null && ctr.getNombreTotalCarthago() > 0)
                .count();

        long chequesControles = allCTR.stream()
                .filter(ctr -> ctr.getMontantTotalCarthago() != null
                        && ctr.getMontantTotalCarthago().compareTo(new BigDecimal("1000")) > 0)
                .count();

        long chequesValides = allCTR.stream()
                .filter(ctr -> ctr.getMontantTotalCarthago() != null
                        && ctr.getMontantTotalCarthago().compareTo(new BigDecimal("5000")) > 0)
                .count();

        long chequesACorriger = allCTR.stream()
                .filter(ctr -> (ctr.getMontantTotalCarthago() == null
                        || ctr.getMontantTotalCarthago().compareTo(BigDecimal.ZERO) == 0) ||
                        (ctr.getNombreTotalCarthago() == null || ctr.getNombreTotalCarthago() == 0))
                .count();

        stats.put("totalCheques", totalCheques);
        stats.put("montantTotal", totalMontant);
        stats.put("chequesVerifies", chequesVerifies);
        stats.put("chequesControles", chequesControles);
        stats.put("chequesValides", chequesValides);
        stats.put("chequesACorriger", chequesACorriger);

        return stats;
    }
}