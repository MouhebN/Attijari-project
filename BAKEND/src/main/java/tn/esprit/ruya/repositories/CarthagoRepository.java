package tn.esprit.ruya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.ruya.models.Carthago;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarthagoRepository extends JpaRepository<Carthago, Long> {

    // Trouver tous les Carthago par date d'import
    List<Carthago> findByDateImport(LocalDate dateImport);

    // Trouver tous les Carthago entre deux dates d'import
    List<Carthago> findByDateImportBetween(LocalDate dateDebut, LocalDate dateFin);

    // Trouver Carthago par nom de fichier
    List<Carthago> findByNomFichierContainingIgnoreCase(String nomFichier);

    // Trouver Carthago avec nombre de détails supérieur à une valeur
    @Query("SELECT c FROM Carthago c WHERE SIZE(c.carthagoDetails) > :nbCheques")
    List<Carthago> findByCarthagoDetailsSizeGreaterThan(@Param("nbCheques") Integer nbCheques);

    // Trouver Carthago avec nombre de chèques déposés supérieur à une valeur
    @Query("SELECT c FROM Carthago c JOIN c.carthagoDetails cd WHERE cd.etatCheque = :etatCheque GROUP BY c HAVING COUNT(cd) > :nbCheques")
    List<Carthago> findByCarthagoDetailsEtatChequeAndSizeGreaterThan(@Param("etatCheque") String etatCheque,
            @Param("nbCheques") Integer nbCheques);

    // Trouver Carthago avec montant total supérieur à une valeur
    @Query("SELECT c FROM Carthago c JOIN c.carthagoDetails cd GROUP BY c HAVING SUM(cd.montant) > :montant")
    List<Carthago> findByCarthagoDetailsMontantSumGreaterThan(@Param("montant") Double montant);

    // Trouver Carthago par état de chèque dans les détails
    @Query("SELECT DISTINCT c FROM Carthago c JOIN c.carthagoDetails cd WHERE cd.etatCheque = :etatCheque")
    List<Carthago> findByCarthagoDetailsEtatCheque(@Param("etatCheque") String etatCheque);

    // Requête personnalisée pour obtenir les statistiques
    @Query("SELECT COUNT(c), " +
            "SUM(SIZE(c.carthagoDetails)), " +
            "SUM(CASE WHEN cd.etatCheque = 'DEPOSE' THEN 1 ELSE 0 END), " +
            "SUM(cd.montant) " +
            "FROM Carthago c LEFT JOIN c.carthagoDetails cd " +
            "WHERE c.dateImport BETWEEN :dateDebut AND :dateFin")
    Object[] getStatistiquesByDateRange(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    // Requête pour obtenir le total des montants par date
    @Query("SELECT c.dateImport, SUM(cd.montant) FROM Carthago c JOIN c.carthagoDetails cd GROUP BY c.dateImport ORDER BY c.dateImport DESC")
    List<Object[]> getMontantsTotauxParDate();

    // Requête pour obtenir le nombre de chèques contrôlés par date
    @Query("SELECT c.dateImport, COUNT(cd) FROM Carthago c JOIN c.carthagoDetails cd GROUP BY c.dateImport ORDER BY c.dateImport DESC")
    List<Object[]> getNombreChequesControlesParDate();

    // Requête pour obtenir le nombre de chèques vérifiés par date
    @Query("SELECT c.dateImport, COUNT(cd) FROM Carthago c JOIN c.carthagoDetails cd WHERE cd.etatCheque = 'DEPOSE' GROUP BY c.dateImport ORDER BY c.dateImport DESC")
    List<Object[]> getNombreChequesVerifiesParDate();

    // Requête pour obtenir les statistiques par état
    @Query("SELECT cd.etatCheque, COUNT(c), SUM(cd.montant) FROM Carthago c JOIN c.carthagoDetails cd GROUP BY cd.etatCheque")
    List<Object[]> getStatistiquesParEtat();

    // Requête pour obtenir les statistiques par situation
    @Query("SELECT c.nomFichier, COUNT(c), SUM(cd.montant) FROM Carthago c JOIN c.carthagoDetails cd GROUP BY c.nomFichier")
    List<Object[]> getStatistiquesParSituation();

    @Query("SELECT SUM(c.nombreEntete), SUM(c.montantEntete) FROM Carthago c")
    Object[] getTotalEnteteStats();

}