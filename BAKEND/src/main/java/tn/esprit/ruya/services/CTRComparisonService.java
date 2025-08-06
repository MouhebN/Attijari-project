package tn.esprit.ruya.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.ruya.models.CTR;
import tn.esprit.ruya.models.Carthago;
import tn.esprit.ruya.models.CarthagoDetail;
import tn.esprit.ruya.models.Fichier;
import tn.esprit.ruya.repositories.CTRRepository;
import tn.esprit.ruya.repositories.CarthagoDetailRepository;
import tn.esprit.ruya.repositories.CarthagoRepository;
import tn.esprit.ruya.repositories.FichierRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CTRComparisonService {

    @Autowired
    private CTRRepository ctrRepository;

    @Autowired
    private CarthagoRepository carthagoRepository;

    @Autowired
    private CarthagoDetailRepository carthagoDetailRepository;

    @Autowired
    private FichierRepository fichierRepository;

    @Autowired
    private CarthagoParserService carthagoParserService;

    public CTR compareFichiers(Long fichierId, Long carthagoId) {
        Optional<Fichier> fichierOpt = fichierRepository.findById(fichierId);
        if (fichierOpt.isEmpty()) {
            throw new IllegalArgumentException("Fichier Encaisse Valeur non trouvé: " + fichierId);
        }
        Fichier fichier = fichierOpt.get();

        Optional<Carthago> carthagoOpt = carthagoRepository.findById(carthagoId);
        if (carthagoOpt.isEmpty()) {
            throw new IllegalArgumentException("Fichier Carthago non trouvé: " + carthagoId);
        }
        Carthago carthago = carthagoOpt.get();

        List<CarthagoDetail> details = carthagoDetailRepository.findByCarthagoId(carthagoId);
        CarthagoParserService.CarthagoStats stats = carthagoParserService.calculateStats(details);

        CTR ctr = new CTR(fichier, carthago);

        ctr.setNombreTotalCarthago(stats.nombreTotal);
        ctr.setMontantTotalCarthago(stats.montantTotal);
        ctr.setNombreDepCarthago(stats.nombreDeposes);
        ctr.setMontantDepCarthago(stats.montantDeposes);
        ctr.setNombreRejCarthago(stats.nombreRejetes);
        ctr.setMontantRejCarthago(stats.montantRejetes);

        ctr.setNombreFichier(fichier.getNombre());
        ctr.setMontantFichier(fichier.getMontant());

        ctr.setNombreOk(stats.nombreTotal == fichier.getNombre() ? "Y" : "N");
        ctr.setMontantOk(stats.montantTotal.compareTo(fichier.getMontant()) == 0 ? "Y" : "N");

        return ctrRepository.save(ctr);
    }

    public int compareAllUnprocessed() {
        int count = 0;
        List<Fichier> fichiers = fichierRepository.findAll();
        List<Carthago> carthagos = carthagoRepository.findAll();

        for (Fichier fichier : fichiers) {
            for (Carthago carthago : carthagos) {
                boolean exists = ctrRepository.findAll().stream()
                        .anyMatch(ctr -> ctr.getFichier() != null && ctr.getCarthago() != null &&
                                ctr.getFichier().getId().equals(fichier.getId()) &&
                                ctr.getCarthago().getId().equals(carthago.getId()));

                if (!exists) {
                    try {
                        compareFichiers(fichier.getId(), carthago.getId());
                        count++;
                    } catch (Exception e) {
                        System.err.println("Erreur lors de la comparaison: " + e.getMessage());
                    }
                }
            }
        }

        return count;
    }

    public DashboardStats calculateDashboardStats() {
        DashboardStats stats = new DashboardStats();
        List<CTR> ctrs = ctrRepository.findAll();

        for (CTR ctr : ctrs) {
            stats.nombreTotalComparaisons++;
            if ("Y".equals(ctr.getNombreOk()) && "Y".equals(ctr.getMontantOk())) {
                stats.nombreComparaisonsOK++;
            } else {
                stats.nombreComparaisonsKO++;
            }

            if (ctr.getMontantTotalCarthago() != null) {
                stats.montantTotalCarthago = stats.montantTotalCarthago.add(ctr.getMontantTotalCarthago());
            }
            if (ctr.getMontantFichier() != null) {
                stats.montantTotalEncaisse = stats.montantTotalEncaisse.add(ctr.getMontantFichier());
            }
        }

        if (stats.nombreTotalComparaisons > 0) {
            stats.tauxConcordance = (double) stats.nombreComparaisonsOK / stats.nombreTotalComparaisons * 100;
        }

        return stats;
    }

    public List<CTR> getFichiersAvecAnomalies() {
        return ctrRepository.findByNombreOkOrMontantOk("N", "N");
    }

    public DateStats getStatsByDate(LocalDate date) {
        DateStats stats = new DateStats();
        stats.date = date;

        List<CTR> ctrs = ctrRepository.findByCreatedAt(date);
        for (CTR ctr : ctrs) {
            stats.nombreComparaisons++;
            if ("Y".equals(ctr.getNombreOk()) && "Y".equals(ctr.getMontantOk())) {
                stats.nombreOK++;
            } else {
                stats.nombreKO++;
            }
        }

        return stats;
    }

    public static class DashboardStats {
        public int nombreTotalComparaisons = 0;
        public int nombreComparaisonsOK = 0;
        public int nombreComparaisonsKO = 0;
        public double tauxConcordance = 0.0;
        public BigDecimal montantTotalCarthago = BigDecimal.ZERO;
        public BigDecimal montantTotalEncaisse = BigDecimal.ZERO;
    }

    public static class DateStats {
        public LocalDate date;
        public int nombreComparaisons = 0;
        public int nombreOK = 0;
        public int nombreKO = 0;

        public double getTauxOK() {
            return nombreComparaisons > 0 ? (double) nombreOK / nombreComparaisons * 100 : 0.0;
        }
    }
}
