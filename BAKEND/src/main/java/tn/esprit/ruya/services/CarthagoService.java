package tn.esprit.ruya.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.ruya.models.Carthago;
import tn.esprit.ruya.models.CarthagoDetail;
import tn.esprit.ruya.repositories.CarthagoDetailRepository;
import tn.esprit.ruya.repositories.CarthagoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;

@Service
public class CarthagoService {
    private final CarthagoRepository carthagoRepository;
    private final CarthagoDetailRepository carthagoDetailRepository;

    public CarthagoService(CarthagoRepository carthagoRepository, CarthagoDetailRepository carthagoDetailRepository) {
        this.carthagoRepository = carthagoRepository;
        this.carthagoDetailRepository = carthagoDetailRepository;
    }


    public Carthago saveParsedCarthago(Carthago carthago, List<CarthagoDetail> details) {
        Carthago savedCarthago = carthagoRepository.save(carthago);

        for (CarthagoDetail detail : details) {
            detail.setCarthago(savedCarthago); // lier à l'objet parent
        }

        carthagoDetailRepository.saveAll(details);
        return savedCarthago;
    }


    public List<Carthago> getAllCarthago() {
        return carthagoRepository.findAll();
    }

    public Optional<Carthago> getCarthagoById(Long id) {
        return carthagoRepository.findById(id);
    }

    public Carthago createCarthago(Carthago carthago) {
        return carthagoRepository.save(carthago);
    }

    public Carthago updateCarthago(Long id, Carthago carthagoDetails) {
        Optional<Carthago> carthagoOptional = carthagoRepository.findById(id);
        if (carthagoOptional.isPresent()) {
            Carthago carthago = carthagoOptional.get();
            // Utiliser les nouvelles propriétés
            carthago.setNomFichier(carthagoDetails.getNomFichier());
            carthago.setDateImport(carthagoDetails.getDateImport());
            carthago.setContenu(carthagoDetails.getContenu());
            return carthagoRepository.save(carthago);
        }
        return null;
    }


    public boolean deleteCarthago(Long id) {
        if (carthagoRepository.existsById(id)) {
            carthagoRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public List<Carthago> getCarthagoByDate(LocalDate date) {
        return carthagoRepository.findByDateImport(date);
    }


    public List<Carthago> getCarthagoBetweenDates(LocalDate dateDebut, LocalDate dateFin) {
        return carthagoRepository.findByDateImportBetween(dateDebut, dateFin);
    }


    public List<Carthago> getCarthagoByEtatRemise(String etatRemise) {
        return carthagoRepository.findByCarthagoDetailsEtatCheque(etatRemise);
    }

    public List<Carthago> getCarthagoBySituationFichier(String situationFichier) {
        return carthagoRepository.findByNomFichierContainingIgnoreCase(situationFichier);
    }

    public List<Carthago> getCarthagoByFichierUpload(String fichierUpload) {
        return carthagoRepository.findByNomFichierContainingIgnoreCase(fichierUpload);
    }

    public Map<String, Object> getStatistiquesByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        Object[] stats = carthagoRepository.getStatistiquesByDateRange(dateDebut, dateFin);
        Map<String, Object> result = new HashMap<>();

        if (stats != null && stats.length >= 4) {
            result.put("nombreCarthago", stats[0]);
            result.put("totalChequesControles", stats[1]);
            result.put("totalChequesVerifies", stats[2]);
            result.put("totalMontant", stats[3]);
        }

        return result;
    }

    public List<Object[]> getMontantsTotauxParDate() {
        return carthagoRepository.getMontantsTotauxParDate();
    }

    public List<Object[]> getNombreChequesControlesParDate() {
        return carthagoRepository.getNombreChequesControlesParDate();
    }

    public List<Object[]> getNombreChequesVerifiesParDate() {
        return carthagoRepository.getNombreChequesVerifiesParDate();
    }

    public List<Object[]> getStatistiquesParEtat() {
        return carthagoRepository.getStatistiquesParEtat();
    }

    public List<Object[]> getStatistiquesParSituation() {
        return carthagoRepository.getStatistiquesParSituation();
    }


    public List<Carthago> getCarthagoWithChequesControlesGreaterThan(Integer nbCheques) {
        return carthagoRepository.findByCarthagoDetailsSizeGreaterThan(nbCheques);
    }

    public List<Carthago> getCarthagoWithChequesVerifiesGreaterThan(Integer nbCheques) {
        return carthagoRepository.findByCarthagoDetailsEtatChequeAndSizeGreaterThan("DEPOSE", nbCheques);
    }

    public List<Carthago> getCarthagoWithMontantGreaterThan(Double montant) {
        return carthagoRepository.findByCarthagoDetailsMontantSumGreaterThan(montant);
    }

    public List<Carthago> getCarthagoByHeureGeneration(LocalDateTime heureDebut, LocalDateTime heureFin) {
        return carthagoRepository.findByDateImportBetween(heureDebut.toLocalDate(), heureFin.toLocalDate());
    }

    public Map<String, Object> getStatistiquesGlobales() {
        List<Carthago> allCarthago = carthagoRepository.findAll();
        Map<String, Object> stats = new HashMap<>();

        long totalCarthago = allCarthago.size();

        // Calculer les statistiques à partir des détails
        int totalChequesControles = 0;
        int totalChequesVerifies = 0;
        BigDecimal totalMontant = BigDecimal.ZERO;

        for (Carthago carthago : allCarthago) {
            if (carthago.getCarthagoDetails() != null) {
                totalChequesControles += carthago.getCarthagoDetails().size();
                totalMontant = totalMontant.add(carthago.getCarthagoDetails().stream()
                        .map(detail -> detail.getMontant() != null ? new BigDecimal(detail.getMontant().toString())
                                : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
            }
        }

        double montantMoyen = totalCarthago > 0 ? totalMontant.doubleValue() / totalCarthago : 0;
        int nombreMoyenChequesControles = totalCarthago > 0 ? totalChequesControles / (int) totalCarthago : 0;

        // Statistiques par état (basées sur les détails)
        long valides = allCarthago.stream()
                .filter(c -> c.getCarthagoDetails() != null &&
                        c.getCarthagoDetails().stream().anyMatch(d -> "DEPOSE".equals(d.getEtatCheque())))
                .count();
        long nonValides = allCarthago.stream()
                .filter(c -> c.getCarthagoDetails() != null &&
                        c.getCarthagoDetails().stream().anyMatch(d -> "REJETE".equals(d.getEtatCheque())))
                .count();

        // Statistiques par situation (basées sur la date d'import)
        long generes = allCarthago.stream()
                .filter(c -> c.getDateImport() != null)
                .count();
        long nonGeneres = allCarthago.stream()
                .filter(c -> c.getDateImport() == null)
                .count();

        stats.put("totalCarthago", totalCarthago);
        stats.put("totalChequesControles", totalChequesControles);
        stats.put("totalChequesVerifies", totalChequesVerifies);
        stats.put("totalMontant", totalMontant);
        stats.put("montantMoyen", montantMoyen);
        stats.put("nombreMoyenChequesControles", nombreMoyenChequesControles);
        stats.put("nombreMoyenChequesVerifies", totalChequesVerifies);
        stats.put("valides", valides);
        stats.put("nonValides", nonValides);
        stats.put("generes", generes);
        stats.put("nonGeneres", nonGeneres);

        return stats;
    }

    // ==================== NOUVELLES MÉTHODES POUR LES CARTES ====================

    // Compter tous les Carthago
    public long countAllCarthago() {
        return carthagoRepository.count();
    }

    // Obtenir le montant total des Carthago
    public BigDecimal getTotalMontantCarthago() {
        return carthagoRepository.findAll().stream()
                .map(carthago -> {
                    if (carthago.getCarthagoDetails() != null) {
                        return carthago.getCarthagoDetails().stream()
                                .map(detail -> detail.getMontant() != null
                                        ? new BigDecimal(detail.getMontant().toString())
                                        : BigDecimal.ZERO)
                                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
    }

    // Obtenir le nombre total de chèques contrôlés
    public long getTotalChequesControles() {
        return carthagoRepository.findAll().stream()
                .mapToLong(carthago -> {
                    if (carthago.getCarthagoDetails() != null) {
                        return carthago.getCarthagoDetails().size();
                    }
                    return 0L;
                })
                .sum();
    }

    // Obtenir le nombre total de chèques vérifiés
    public long getTotalChequesVerifies() {
        return carthagoRepository.findAll().stream()
                .mapToLong(carthago -> {
                    if (carthago.getCarthagoDetails() != null) {
                        return carthago.getCarthagoDetails().stream()
                                .filter(detail -> "DEPOSE".equals(detail.getEtatCheque()))
                                .count();
                    }
                    return 0L;
                })
                .sum();
    }

    // Compter les Carthago par état
    public long countCarthagoByEtat(String etat) {
        return carthagoRepository.findAll().stream()
                .filter(c -> c.getCarthagoDetails() != null &&
                        c.getCarthagoDetails().stream().anyMatch(d -> etat.equalsIgnoreCase(d.getEtatCheque())))
                .count();
    }

    // Compter les Carthago avec des images (basé sur le contenu)
    public long countCarthagoWithImages() {
        return carthagoRepository.findAll().stream()
                .filter(c -> c.getContenu() != null && !c.getContenu().trim().isEmpty())
                .count();
    }

    // Obtenir les statistiques CARTHAGO pour les cartes
    public Map<String, Object> getCarthagoStatsForCards() {
        List<Carthago> allCarthago = carthagoRepository.findAll();
        Map<String, Object> stats = new HashMap<>();

        long totalCheques = allCarthago.stream()
                .mapToLong(c -> {
                    if (c.getCarthagoDetails() != null) {
                        return c.getCarthagoDetails().size();
                    }
                    return 0L;
                })
                .sum();

        BigDecimal totalMontant = allCarthago.stream()
                .map(carthago -> {
                    if (carthago.getCarthagoDetails() != null) {
                        return carthago.getCarthagoDetails().stream()
                                .map(detail -> detail.getMontant() != null
                                        ? new BigDecimal(detail.getMontant().toString())
                                        : BigDecimal.ZERO)
                                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        long nombreImages = allCarthago.stream()
                .filter(c -> c.getContenu() != null && !c.getContenu().trim().isEmpty())
                .count();

        // Obtenir la prochaine génération (basé sur la date d'import)
        String prochaineGeneration = allCarthago.stream()
                .filter(c -> c.getDateImport() != null)
                .map(Carthago::getDateImport)
                .min(LocalDate::compareTo)
                .map(Object::toString)
                .orElse(LocalDate.now().toString());

        // Obtenir la dernière mise à jour (la plus récente)
        String derniereMiseAJour = allCarthago.stream()
                .filter(c -> c.getDateImport() != null)
                .map(c -> c.getDateImport().toString())
                .max(String::compareTo)
                .orElse(LocalDate.now().toString());

        stats.put("totalCheques", totalCheques);
        stats.put("montantTotal", totalMontant);
        stats.put("nombreImages", nombreImages);
        stats.put("prochaineGeneration", prochaineGeneration);
        stats.put("derniereMiseAJour", derniereMiseAJour);

        return stats;
    }
    public Map<String, Object> getEnteteStats() {
        Object[] result = carthagoRepository.getTotalEnteteStats();
        System.out.println("Query result (inside): " + java.util.Arrays.toString(result));
        if (result.length > 0 && result[0] instanceof Object[]) {
            Object[] sums = (Object[]) result[0];
            System.out.println("Query inner values: " + java.util.Arrays.toString(sums));
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalNombreEntete", sums[0] != null ? sums[0] : 0L);
            stats.put("totalMontantEntete", sums[1] != null ? sums[1] : 0.0);
            return stats;
        } else {
            // fallback in case of unexpected structure
            return Map.of("totalNombreEntete", 0, "totalMontantEntete", 0.0);
        }
    }

}