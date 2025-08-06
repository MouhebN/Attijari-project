package tn.esprit.ruya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ruya.models.CTR;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CTRRepository extends JpaRepository<CTR, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM CTR c WHERE c.fichier.id = :fichierId")
    void deleteByFichierId(Long fichierId);

    // Trouver tous les CTR par date de création
    List<CTR> findByCreatedAt(LocalDate createdAt);

    // Trouver tous les CTR entre deux dates de création
    List<CTR> findByCreatedAtBetween(LocalDate dateDebut, LocalDate dateFin);

    // Trouver CTR par fichier de référence
    @Query("SELECT c FROM CTR c WHERE c.fichier.nomFichier LIKE %:fichierRef%")
    List<CTR> findByFichierNomFichierContainingIgnoreCase(@Param("fichierRef") String fichierRef);

    // Vérifier si un CTR existe déjà pour un fichier et un carthago
    @Query("SELECT COUNT(c) > 0 FROM CTR c WHERE c.fichier.id = :fichierId AND c.carthago.id = :carthagoId")
    boolean existsByFichierIdAndCarthagoId(@Param("fichierId") Long fichierId, @Param("carthagoId") Long carthagoId);

    // Trouver CTR par nombre OK ou montant OK
    @Query("SELECT c FROM CTR c WHERE c.nombreOk = :nombreOk OR c.montantOk = :montantOk")
    List<CTR> findByNombreOkOrMontantOk(@Param("nombreOk") String nombreOk, @Param("montantOk") String montantOk);

    // Trouver CTR avec nombre total de chèques Carthago supérieur à une valeur
    List<CTR> findByNombreTotalCarthagoGreaterThan(Integer nombreCheques);

    // Trouver CTR avec montant total Carthago supérieur à une valeur
    List<CTR> findByMontantTotalCarthagoGreaterThan(Double montant);

    // Requête personnalisée pour obtenir les statistiques
    @Query("SELECT COUNT(c), SUM(c.nombreTotalCarthago), SUM(c.montantTotalCarthago) FROM CTR c WHERE c.createdAt BETWEEN :dateDebut AND :dateFin")
    Object[] getStatistiquesByDateRange(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    // Requête pour obtenir le total des montants par date
    @Query("SELECT c.createdAt, SUM(c.montantTotalCarthago) FROM CTR c GROUP BY c.createdAt ORDER BY c.createdAt DESC")
    List<Object[]> getMontantsTotauxParDate();

    // Requête pour obtenir le nombre de chèques par date
    @Query("SELECT c.createdAt, SUM(c.nombreTotalCarthago) FROM CTR c GROUP BY c.createdAt ORDER BY c.createdAt DESC")
    List<Object[]> getNombreChequesParDate();

    @Query("SELECT c FROM CTR c JOIN FETCH c.fichier")
    List<CTR> findAllWithFichier();

    List<CTR> findByNombreOkNotOrMontantOkNot(String nombreOk, String montantOk);
    @Query("SELECT c FROM CTR c JOIN FETCH c.fichier WHERE c.nombreOk <> 'Y' OR c.montantOk <> 'Y'")
    List<CTR> findAnomaliesWithFichier();


}