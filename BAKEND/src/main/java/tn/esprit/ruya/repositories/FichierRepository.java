package tn.esprit.ruya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.ruya.models.Fichier;

import java.util.List;
import java.util.Optional;

@Repository
public interface FichierRepository extends JpaRepository<Fichier, Long> {
    
    /**
     * Trouver un fichier par nom
     */
    Optional<Fichier> findByNomFichier(String nomFichier);
    
    /**
     * Trouver les fichiers par type
     */
    List<Fichier> findByTypeFichier(String typeFichier);
    
    /**
     * Trouver les fichiers par nature
     */
    List<Fichier> findByNatureFichier(String natureFichier);
    
    /**
     * Trouver les fichiers par code valeur
     */
    List<Fichier> findByCodeValeur(String codeValeur);
    
    /**
     * Trouver les fichiers par utilisateur
     */
    List<Fichier> findByUserId(Long userId);
    
    /**
     * Rechercher des fichiers par nom (contient)
     */
    List<Fichier> findByNomFichierContainingIgnoreCase(String nomFichier);
    
    /**
     * Compter le nombre de fichiers par type
     */
    @Query("SELECT f.typeFichier, COUNT(f) FROM Fichier f GROUP BY f.typeFichier")
    List<Object[]> countByTypeFichier();
    
    /**
     * Calculer la somme des montants par type de fichier
     */
    @Query("SELECT f.typeFichier, SUM(f.montant) FROM Fichier f GROUP BY f.typeFichier")
    List<Object[]> sumMontantByTypeFichier();
} 