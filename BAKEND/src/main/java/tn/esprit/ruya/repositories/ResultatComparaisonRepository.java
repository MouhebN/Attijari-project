package tn.esprit.ruya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ruya.models.ResultatComparaison;

import java.util.List;

@Repository
public interface ResultatComparaisonRepository extends JpaRepository<ResultatComparaison, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM ResultatComparaison r WHERE r.fichier.id = :fichierId")
    void deleteByFichierId(Long fichierId);

    // Trouver tous les résultats de comparaison par résultat global
    List<ResultatComparaison> findByResultatGlobal(String resultatGlobal);

    // Trouver tous les résultats de comparaison par fichier
    List<ResultatComparaison> findByFichierId(Long fichierId);

    // Trouver tous les résultats de comparaison par CTR
    List<ResultatComparaison> findByCtrId(Long ctrId);

    // Trouver tous les résultats de comparaison par Carthago
    List<ResultatComparaison> findByCarthagoId(Long carthagoId);

    // Trouver résultats avec montant Carthago supérieur à une valeur
    List<ResultatComparaison> findByMontantCarthagoGreaterThan(Double montant);

    // Trouver résultats avec montant fichier supérieur à une valeur
    List<ResultatComparaison> findByMontantFichierGreaterThan(Double montant);

    // Trouver résultats avec montant CTR supérieur à une valeur
    List<ResultatComparaison> findByMontantCtrGreaterThan(Double montant);

    // Trouver résultats avec nombre de chèques Carthago supérieur à une valeur
    List<ResultatComparaison> findByNombreChequesCarthagoGreaterThan(Integer nombre);

    // Trouver résultats avec nombre de chèques fichier supérieur à une valeur
    List<ResultatComparaison> findByNombreChequesFichierGreaterThan(Integer nombre);

    // Trouver résultats avec nombre de chèques CTR supérieur à une valeur
    List<ResultatComparaison> findByNombreChequesCtrGreaterThan(Integer nombre);

    // Requête personnalisée pour obtenir les statistiques globales
    @Query("SELECT COUNT(r), SUM(r.montantCarthago), SUM(r.montantFichier), SUM(r.montantCtr), " +
            "SUM(r.nombreChequesCarthago), SUM(r.nombreChequesFichier), SUM(r.nombreChequesCtr) " +
            "FROM ResultatComparaison r")
    Object[] getStatistiquesGlobales();

    // Requête pour obtenir les statistiques par résultat global
    @Query("SELECT r.resultatGlobal, COUNT(r), SUM(r.montantCarthago), SUM(r.montantFichier), SUM(r.montantCtr) " +
            "FROM ResultatComparaison r GROUP BY r.resultatGlobal")
    List<Object[]> getStatistiquesParResultat();

    // Requête pour obtenir les résultats avec différences importantes
    @Query("SELECT r FROM ResultatComparaison r WHERE " +
            "ABS(r.montantCarthago - r.montantFichier) > :seuil OR " +
            "ABS(r.montantCarthago - r.montantCtr) > :seuil OR " +
            "ABS(r.montantFichier - r.montantCtr) > :seuil")
    List<ResultatComparaison> findResultatsAvecDifferences(@Param("seuil") Double seuil);

    // Requête pour obtenir les statistiques de comparaison
    @Query("SELECT " +
            "COUNT(CASE WHEN r.resultatGlobal = 'valide' THEN 1 END) as valides, " +
            "COUNT(CASE WHEN r.resultatGlobal = 'non valide' THEN 1 END) as nonValides, " +
            "AVG(r.montantCarthago) as avgMontantCarthago, " +
            "AVG(r.montantFichier) as avgMontantFichier, " +
            "AVG(r.montantCtr) as avgMontantCtr " +
            "FROM ResultatComparaison r")
    Object[] getStatistiquesComparaison();

    // Requête pour obtenir les résultats récents
    @Query("SELECT r FROM ResultatComparaison r ORDER BY r.id DESC")
    List<ResultatComparaison> findResultatsRecents();

    // Requête pour obtenir les résultats par fichier avec détails
    @Query("SELECT r FROM ResultatComparaison r WHERE r.fichier.id = :fichierId")
    List<ResultatComparaison> findResultatsByFichier(@Param("fichierId") Long fichierId);

    // Requête pour obtenir les résultats par CTR avec détails
    @Query("SELECT r FROM ResultatComparaison r WHERE r.ctr.id = :ctrId")
    List<ResultatComparaison> findResultatsByCtr(@Param("ctrId") Long ctrId);

    // Requête pour obtenir les résultats par Carthago avec détails
    @Query("SELECT r FROM ResultatComparaison r WHERE r.carthago.id = :carthagoId")
    List<ResultatComparaison> findResultatsByCarthago(@Param("carthagoId") Long carthagoId);
}