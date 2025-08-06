package tn.esprit.ruya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.ruya.models.CarthagoDetail;

import java.util.List;

@Repository
public interface CarthagoDetailRepository extends JpaRepository<CarthagoDetail, Long> {

    @Query("SELECT SUM(cd.montant) FROM CarthagoDetail cd WHERE cd.etatCheque = :etatCheque")
    Double sumMontantByEtat(@Param("etatCheque") String etatCheque);
    @Query("SELECT COUNT(cd) FROM CarthagoDetail cd WHERE cd.etatCheque = :etatCheque")
    long countByEtatCheque(@Param("etatCheque") String etatCheque);

    /**
     * Trouver tous les détails d'un fichier Carthago
     */
    List<CarthagoDetail> findByCarthagoId(Long carthagoId);
    
    /**
     * Trouver les détails par état de chèque
     */
    List<CarthagoDetail> findByEtatCheque(String etatCheque);
    
    /**
     * Trouver les détails d'un fichier Carthago par état
     */
    List<CarthagoDetail> findByCarthagoIdAndEtatCheque(Long carthagoId, String etatCheque);
    
    /**
     * Compter le nombre de chèques par état pour un fichier Carthago
     */
    @Query("SELECT COUNT(cd) FROM CarthagoDetail cd WHERE cd.carthago.id = :carthagoId AND cd.etatCheque = :etatCheque")
    long countByCarthagoIdAndEtatCheque(@Param("carthagoId") Long carthagoId, @Param("etatCheque") String etatCheque);
    
    /**
     * Calculer la somme des montants par état pour un fichier Carthago
     */
    @Query("SELECT SUM(cd.montant) FROM CarthagoDetail cd WHERE cd.carthago.id = :carthagoId AND cd.etatCheque = :etatCheque")
    Double sumMontantByCarthagoIdAndEtatCheque(@Param("carthagoId") Long carthagoId, @Param("etatCheque") String etatCheque);
} 