package tn.esprit.ruya.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.ruya.models.ResultatComparaison;
import tn.esprit.ruya.models.Carthago;
import tn.esprit.ruya.models.Fichier;
import tn.esprit.ruya.models.CTR;
import tn.esprit.ruya.repositories.ResultatComparaisonRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Service
public class ResultatComparaisonService {

    @Autowired
    private ResultatComparaisonRepository resultatComparaisonRepository;

    // Récupérer tous les résultats de comparaison
    public List<ResultatComparaison> getAllResultatsComparaison() {
        return resultatComparaisonRepository.findAll();
    }

    // Récupérer un résultat de comparaison par ID
    public Optional<ResultatComparaison> getResultatComparaisonById(Long id) {
        return resultatComparaisonRepository.findById(id);
    }

    // Créer un nouveau résultat de comparaison
    public ResultatComparaison createResultatComparaison(ResultatComparaison resultatComparaison) {
        return resultatComparaisonRepository.save(resultatComparaison);
    }

    // Mettre à jour un résultat de comparaison
    public ResultatComparaison updateResultatComparaison(Long id, ResultatComparaison resultatDetails) {
        Optional<ResultatComparaison> resultatOptional = resultatComparaisonRepository.findById(id);
        if (resultatOptional.isPresent()) {
            ResultatComparaison resultat = resultatOptional.get();
            resultat.setCarthago(resultatDetails.getCarthago());
            resultat.setFichier(resultatDetails.getFichier());
            resultat.setCtr(resultatDetails.getCtr());
            resultat.setMontantCarthago(resultatDetails.getMontantCarthago());
            resultat.setMontantFichier(resultatDetails.getMontantFichier());
            resultat.setMontantCtr(resultatDetails.getMontantCtr());
            resultat.setNombreChequesCarthago(resultatDetails.getNombreChequesCarthago());
            resultat.setNombreChequesFichier(resultatDetails.getNombreChequesFichier());
            resultat.setNombreChequesCtr(resultatDetails.getNombreChequesCtr());
            resultat.setResultatGlobal(resultatDetails.getResultatGlobal());
            resultat.setRemarques(resultatDetails.getRemarques());
            return resultatComparaisonRepository.save(resultat);
        }
        return null;
    }

    // Supprimer un résultat de comparaison
    public boolean deleteResultatComparaison(Long id) {
        if (resultatComparaisonRepository.existsById(id)) {
            resultatComparaisonRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Récupérer les résultats par résultat global
    public List<ResultatComparaison> getResultatsByResultatGlobal(String resultatGlobal) {
        return resultatComparaisonRepository.findByResultatGlobal(resultatGlobal);
    }

    // Récupérer les résultats par fichier
    public List<ResultatComparaison> getResultatsByFichier(Long fichierId) {
        return resultatComparaisonRepository.findResultatsByFichier(fichierId);
    }

    // Récupérer les résultats par CTR
    public List<ResultatComparaison> getResultatsByCtr(Long ctrId) {
        return resultatComparaisonRepository.findResultatsByCtr(ctrId);
    }

    // Récupérer les résultats par Carthago
    public List<ResultatComparaison> getResultatsByCarthago(Long carthagoId) {
        return resultatComparaisonRepository.findResultatsByCarthago(carthagoId);
    }

    // Récupérer les résultats récents
    public List<ResultatComparaison> getResultatsRecents() {
        return resultatComparaisonRepository.findResultatsRecents();
    }

    // Récupérer les résultats avec différences importantes
    public List<ResultatComparaison> getResultatsAvecDifferences(Double seuil) {
        return resultatComparaisonRepository.findResultatsAvecDifferences(seuil);
    }

    // Récupérer les statistiques globales
    public Map<String, Object> getStatistiquesGlobales() {
        Object[] stats = resultatComparaisonRepository.getStatistiquesGlobales();
        Map<String, Object> result = new HashMap<>();

        if (stats != null && stats.length >= 7) {
            result.put("nombreResultats", stats[0]);
            result.put("totalMontantCarthago", stats[1]);
            result.put("totalMontantFichier", stats[2]);
            result.put("totalMontantCtr", stats[3]);
            result.put("totalChequesCarthago", stats[4]);
            result.put("totalChequesFichier", stats[5]);
            result.put("totalChequesCtr", stats[6]);
        }

        return result;
    }

    // Récupérer les statistiques par résultat
    public List<Object[]> getStatistiquesParResultat() {
        return resultatComparaisonRepository.getStatistiquesParResultat();
    }

    // Récupérer les statistiques de comparaison
    public Map<String, Object> getStatistiquesComparaison() {
        Object[] stats = resultatComparaisonRepository.getStatistiquesComparaison();
        Map<String, Object> result = new HashMap<>();

        if (stats != null && stats.length >= 5) {
            result.put("valides", stats[0]);
            result.put("nonValides", stats[1]);
            result.put("avgMontantCarthago", stats[2]);
            result.put("avgMontantFichier", stats[3]);
            result.put("avgMontantCtr", stats[4]);
        }

        return result;
    }

    // Obtenir les statistiques détaillées
    public Map<String, Object> getStatistiquesDetaillees() {
        List<ResultatComparaison> allResultats = resultatComparaisonRepository.findAll();
        Map<String, Object> stats = new HashMap<>();

        long totalResultats = allResultats.size();
        long valides = allResultats.stream().filter(r -> "valide".equals(r.getResultatGlobal())).count();
        long nonValides = allResultats.stream().filter(r -> "non valide".equals(r.getResultatGlobal())).count();

        BigDecimal totalMontantCarthago = allResultats.stream()
                .map(ResultatComparaison::getMontantCarthago)
                .filter(montant -> montant != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalMontantFichier = allResultats.stream()
                .map(ResultatComparaison::getMontantFichier)
                .filter(montant -> montant != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalMontantCtr = allResultats.stream()
                .map(ResultatComparaison::getMontantCtr)
                .filter(montant -> montant != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalChequesCarthago = allResultats.stream().mapToInt(ResultatComparaison::getNombreChequesCarthago).sum();
        int totalChequesFichier = allResultats.stream().mapToInt(ResultatComparaison::getNombreChequesFichier).sum();
        int totalChequesCtr = allResultats.stream().mapToInt(ResultatComparaison::getNombreChequesCtr).sum();

        double tauxValidation = totalResultats > 0 ? (double) valides / totalResultats * 100 : 0;

        stats.put("totalResultats", totalResultats);
        stats.put("valides", valides);
        stats.put("nonValides", nonValides);
        stats.put("tauxValidation", tauxValidation);
        stats.put("totalMontantCarthago", totalMontantCarthago);
        stats.put("totalMontantFichier", totalMontantFichier);
        stats.put("totalMontantCtr", totalMontantCtr);
        stats.put("totalChequesCarthago", totalChequesCarthago);
        stats.put("totalChequesFichier", totalChequesFichier);
        stats.put("totalChequesCtr", totalChequesCtr);

        return stats;
    }

    // Méthode pour comparer les données entre Carthago, Fichier et CTR
    public ResultatComparaison comparerDonnees(Carthago carthago, Fichier fichier, CTR ctr) {
        ResultatComparaison resultat = new ResultatComparaison();

        // Assigner les entités
        resultat.setCarthago(carthago);
        resultat.setFichier(fichier);
        resultat.setCtr(ctr);

        // Extraire les montants - utiliser les nouvelles méthodes
        BigDecimal montantCarthago = null;
        BigDecimal montantFichier = fichier.getMontant() != null ? fichier.getMontant() : null;
        BigDecimal montantCtr = ctr.getMontantTotalCarthago();

        // Calculer le montant total de Carthago à partir des détails
        if (carthago.getCarthagoDetails() != null && !carthago.getCarthagoDetails().isEmpty()) {
            montantCarthago = carthago.getCarthagoDetails().stream()
                    .map(detail -> detail.getMontant() != null ? new BigDecimal(detail.getMontant().toString())
                            : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        }

        // Extraire les nombres de chèques - utiliser les nouvelles méthodes
        Integer nombreChequesCarthago = null;
        Integer nombreChequesFichier = fichier.getNombre();
        Integer nombreChequesCtr = ctr.getNombreTotalCarthago();

        // Calculer le nombre de chèques de Carthago à partir des détails
        if (carthago.getCarthagoDetails() != null) {
            nombreChequesCarthago = carthago.getCarthagoDetails().size();
        }

        // Assigner les valeurs
        resultat.setMontantCarthago(montantCarthago != null ? montantCarthago : BigDecimal.ZERO);
        resultat.setMontantFichier(montantFichier != null ? montantFichier : BigDecimal.ZERO);
        resultat.setMontantCtr(montantCtr != null ? montantCtr : BigDecimal.ZERO);
        resultat.setNombreChequesCarthago(nombreChequesCarthago != null ? nombreChequesCarthago : 0);
        resultat.setNombreChequesFichier(nombreChequesFichier != null ? nombreChequesFichier : 0);
        resultat.setNombreChequesCtr(nombreChequesCtr != null ? nombreChequesCtr : 0);

        // Effectuer la comparaison et déterminer le résultat
        String resultatGlobal = determinerResultatGlobal(montantCarthago, montantFichier, montantCtr,
                nombreChequesCarthago, nombreChequesFichier, nombreChequesCtr);
        resultat.setResultatGlobal(resultatGlobal);

        // Générer les remarques
        String remarques = genererRemarques(montantCarthago, montantFichier, montantCtr,
                nombreChequesCarthago, nombreChequesFichier, nombreChequesCtr);
        resultat.setRemarques(remarques);

        // Sauvegarder le résultat
        return resultatComparaisonRepository.save(resultat);
    }

    // Méthode privée pour déterminer le résultat global
    private String determinerResultatGlobal(BigDecimal montantCarthago, BigDecimal montantFichier,
            BigDecimal montantCtr,
            Integer nombreChequesCarthago, Integer nombreChequesFichier, Integer nombreChequesCtr) {

        // Seuil de tolérance pour les différences (5%)
        BigDecimal seuilMontant = new BigDecimal("0.05");
        int seuilCheques = 5;

        // Vérifier les montants
        boolean montantsValides = true;
        if (montantCarthago != null && montantFichier != null && montantFichier.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal differenceMontant = montantCarthago.subtract(montantFichier).abs();
            BigDecimal pourcentageDifference = differenceMontant.divide(montantFichier, 4, BigDecimal.ROUND_HALF_UP);
            if (pourcentageDifference.compareTo(seuilMontant) > 0) {
                montantsValides = false;
            }
        }

        // Vérifier les nombres de chèques
        boolean chequesValides = true;
        if (nombreChequesCarthago != null && nombreChequesFichier != null) {
            int differenceCheques = Math.abs(nombreChequesCarthago - nombreChequesFichier);
            if (differenceCheques > seuilCheques) {
                chequesValides = false;
            }
        }

        return (montantsValides && chequesValides) ? "valide" : "non valide";
    }

    // Méthode privée pour générer les remarques
    private String genererRemarques(BigDecimal montantCarthago, BigDecimal montantFichier, BigDecimal montantCtr,
            Integer nombreChequesCarthago, Integer nombreChequesFichier, Integer nombreChequesCtr) {

        StringBuilder remarques = new StringBuilder();

        // Comparaison des montants
        if (montantCarthago != null && montantFichier != null) {
            BigDecimal differenceMontant = montantCarthago.subtract(montantFichier).abs();
            if (differenceMontant.compareTo(BigDecimal.ZERO) > 0) {
                remarques.append(String.format("Différence de montant: %.2f DT. ", differenceMontant));
            }
        }

        // Comparaison des nombres de chèques
        if (nombreChequesCarthago != null && nombreChequesFichier != null) {
            int differenceCheques = Math.abs(nombreChequesCarthago - nombreChequesFichier);
            if (differenceCheques > 0) {
                remarques.append(String.format("Différence de chèques: %d. ", differenceCheques));
            }
        }

        // Vérification de la correspondance parfaite
        if (montantCarthago != null && montantFichier != null &&
                montantCarthago.subtract(montantFichier).abs().compareTo(new BigDecimal("0.01")) < 0 &&
                nombreChequesCarthago != null && nombreChequesFichier != null &&
                nombreChequesCarthago.equals(nombreChequesFichier)) {
            remarques.append("Correspondance parfaite entre les données.");
        } else if (remarques.length() == 0) {
            remarques.append("Comparaison effectuée avec succès.");
        }

        return remarques.toString();
    }
}