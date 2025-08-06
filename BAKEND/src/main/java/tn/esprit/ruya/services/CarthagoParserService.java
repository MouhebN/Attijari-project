package tn.esprit.ruya.services;

import org.springframework.stereotype.Service;
import tn.esprit.ruya.models.Carthago;
import tn.esprit.ruya.models.CarthagoDetail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarthagoParserService {

    /**
     * Parse le contenu d'un fichier Carthago et extrait l'entête et les détails.
     */
    public CarthagoParseResult parseCarthagoFile(String contenu) {
        String[] lignes = contenu.split("\n");
        CarthagoParseResult result = new CarthagoParseResult();

        // --- Lire l'entête (première ligne)
        CarthagoEntete entete = parseEntete(lignes[0]);
        result.entete = entete;

        List<CarthagoDetail> details = new ArrayList<>();
        double totalMontant = 0.0;

        // --- Lire les détails (lignes suivantes)
        for (int i = 1; i < lignes.length; i++) {
            CarthagoDetail detail = parseLigneDetail(lignes[i]);
            if (detail != null) {
                details.add(detail);
                totalMontant += detail.getMontant();
            }
        }

        result.details = details;
        result.stats = calculateStats(details);

        // --- Comparaison interne (optionnel, à gérer dans Carthago principal)
        result.stats.nombreAttendu = entete != null ? entete.nombreTotal : 0;
        result.stats.montantAttendu = entete != null ? entete.montantTotal : BigDecimal.ZERO;
        result.stats.nombreOk = (entete != null && details.size() == entete.nombreTotal) ? "Y" : "N";
        result.stats.montantOk = (entete != null &&
                entete.montantTotal != null &&
                result.stats.montantTotal != null &&
                entete.montantTotal.subtract(result.stats.montantTotal).abs().compareTo(BigDecimal.valueOf(0.01)) < 0)
                ? "Y" : "N";

        return result;
    }

    /**
     * Parse l'entête du fichier Carthago (première ligne).
     * - Nombre total : position 29 (index 28), longueur 10
     * - Montant total : position 39 (index 38), longueur 15
     */
    public CarthagoEntete parseEntete(String ligneEntete) {
        try {
            if (ligneEntete == null || ligneEntete.length() < 53) {
                return null;
            }
            // Nombre total
            String nombreTotalStr = ligneEntete.substring(28, 38).trim();
            int nombreTotal = Integer.parseInt(nombreTotalStr);

            // Montant total
            String montantTotalStr = ligneEntete.substring(38, 53).trim();
            BigDecimal montantTotal = new BigDecimal(montantTotalStr).divide(BigDecimal.valueOf(100)); // si c'est en centimes

            return new CarthagoEntete(nombreTotal, montantTotal);
        } catch (Exception e) {
            System.err.println("Erreur lors du parsing de l'entête: " + ligneEntete + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Parse une ligne de détail du fichier Carthago.
     * - Montant du chèque : position 36 (index 35), longueur 15
     */
    private CarthagoDetail parseLigneDetail(String ligne) {
        try {
            if (ligne == null || ligne.length() < 50) return null;

            // Montant du chèque
            String montantStr = ligne.substring(35, 50).trim();
            BigDecimal montant = new BigDecimal(montantStr).divide(BigDecimal.valueOf(100)); // si en centimes

            // Déterminer l'état du chèque
            String etatCheque = determineEtatCheque(ligne);

            // Extraire le numéro de chèque si dispo (ex : 20 premiers caractères, à ajuster si besoin)
            String numeroCheque = extractNumeroCheque(ligne);

            CarthagoDetail detail = new CarthagoDetail();
            detail.setMontant(montant.doubleValue());
            detail.setEtatCheque(etatCheque);
            detail.setNumeroCheque(numeroCheque);
            detail.setLigneOrigine(ligne);

            return detail;

        } catch (Exception e) {
            System.err.println("Erreur lors du parsing de la ligne: " + ligne + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Détermine l'état du chèque selon la documentation Carthago.
     * À ajuster selon la vraie doc !
     */
    private String determineEtatCheque(String ligne) {
        if (ligne.length() >= 80) {
            String statut = ligne.substring(79, 80);
            if ("A".equals(statut) || "D".equals(statut)) return "DEPOSE";
            else if ("R".equals(statut) || "J".equals(statut)) return "REJETE";
        }
        return "DEPOSE";
    }

    /**
     * Extrait le numéro de chèque (ex : premiers caractères).
     */
    private String extractNumeroCheque(String ligne) {
        try {
            if (ligne.length() >= 20) {
                return ligne.substring(0, 20).trim();
            }
        } catch (Exception ignored) {}
        return null;
    }

    public CarthagoStats calculateStats(List<CarthagoDetail> details) {
        CarthagoStats stats = new CarthagoStats();
        for (CarthagoDetail detail : details) {
            stats.nombreTotal++;
            stats.montantTotal = stats.montantTotal.add(BigDecimal.valueOf(detail.getMontant()));

            if ("DEPOSE".equals(detail.getEtatCheque())) {
                stats.nombreDeposes++;
                stats.montantDeposes = stats.montantDeposes.add(BigDecimal.valueOf(detail.getMontant()));
            } else {
                stats.nombreRejetes++;
                stats.montantRejetes = stats.montantRejetes.add(BigDecimal.valueOf(detail.getMontant()));
            }
        }
        return stats;
    }

    // ----- Classes internes (inchangées) -----

    public static class CarthagoStats {
        public int nombreTotal = 0;
        public BigDecimal montantTotal = BigDecimal.ZERO;
        public int nombreDeposes = 0;
        public BigDecimal montantDeposes = BigDecimal.ZERO;
        public int nombreRejetes = 0;
        public BigDecimal montantRejetes = BigDecimal.ZERO;

        // Ajouts pour la concordance
        public int nombreAttendu = 0;
        public BigDecimal montantAttendu = BigDecimal.ZERO;
        public String nombreOk = "N";
        public String montantOk = "N";

        @Override
        public String toString() {
            return String.format(
                    "CarthagoStats{nombreTotal=%d, montantTotal=%s, nombreDeposes=%d, montantDeposes=%s, nombreRejetes=%d, montantRejetes=%s, nombreOk=%s, montantOk=%s}",
                    nombreTotal, montantTotal, nombreDeposes, montantDeposes, nombreRejetes, montantRejetes, nombreOk, montantOk
            );
        }
    }

    public static class CarthagoEntete {
        public int nombreTotal;
        public BigDecimal montantTotal;

        public CarthagoEntete(int nombreTotal, BigDecimal montantTotal) {
            this.nombreTotal = nombreTotal;
            this.montantTotal = montantTotal;
        }

        @Override
        public String toString() {
            return String.format("CarthagoEntete{nombreTotal=%d, montantTotal=%s}", nombreTotal, montantTotal);
        }
    }

    public static class CarthagoParseResult {
        public CarthagoEntete entete;
        public List<CarthagoDetail> details = new ArrayList<>();
        public CarthagoStats stats;

        @Override
        public String toString() {
            return String.format("CarthagoParseResult{entete=%s, details=%d, stats=%s}",
                    entete, details.size(), stats);
        }
    }
}