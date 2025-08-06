package tn.esprit.ruya.Fichier.service;

import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ruya.Fichier.repository.IFichierrepo;
import tn.esprit.ruya.models.Fichier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.ruya.models.FichierUpdateDTO;
import tn.esprit.ruya.repositories.CTRRepository;
import tn.esprit.ruya.repositories.ResultatComparaisonRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class FichierServ implements IFichierser {

    @Autowired
    private IFichierrepo fichierRepo;
    @Autowired
    private CTRRepository ctrRepo;
    @Autowired
    private ResultatComparaisonRepository resultatRepo;
    @Override
    public List<Fichier> getAllFichiers() {
        return (List<Fichier>) fichierRepo.findAll();
    }

    @Override
    public Optional<Fichier> getFichierById(Long id) {
        return fichierRepo.findById(id);
    }

    @Override
    public Fichier createFichier(Fichier fichier) {
        return fichierRepo.save(fichier);
    }

    public Fichier updateFichier(Long id, FichierUpdateDTO dto) {
        Optional<Fichier> opt = fichierRepo.findById(id);
        if (opt.isEmpty()) return null;

        Fichier f = opt.get();

        // Only update if not null
        if (dto.nomFichier != null) f.setNomFichier(dto.nomFichier);
        if (dto.typeFichier != null) f.setTypeFichier(dto.typeFichier);
        if (dto.natureFichier != null) f.setNatureFichier(dto.natureFichier);
        if (dto.codeValeur != null) f.setCodeValeur(dto.codeValeur);
        if (dto.codEn != null) f.setCodEn(dto.codEn);
        if (dto.sens != null) f.setSens(dto.sens);
        if (dto.nombre != null) f.setNombre(dto.nombre);
        if (dto.montant != null) f.setMontant(BigDecimal.valueOf(dto.montant));
        if (dto.formatFichier != null) f.setFormatFichier(dto.formatFichier);

        f.setUpdatedAt(LocalDateTime.now());

        return fichierRepo.save(f);
    }


    @Transactional
    public void deleteFichier(Long id) {
        fichierRepo.deleteById(id);
    }



    // ==================== NOUVELLES MÉTHODES POUR LES STATISTIQUES
    // ====================

    // Obtenir les fichiers par code valeur
    public List<Fichier> getFichiersByCodeValeur(String codeValeur) {
        return getAllFichiers().stream()
                .filter(fichier -> codeValeur.equals(fichier.getCodeValeur()))
                .collect(Collectors.toList());
    }

    // Compter les fichiers par code valeur
    public long countFichiersByCodeValeur(String codeValeur) {
        return getAllFichiers().stream()
                .filter(fichier -> codeValeur.equals(fichier.getCodeValeur()))
                .count();
    }


    // Obtenir le nombre total de chèques par code valeur
    public long getTotalNombreByCodeValeur(String codeValeur) {
        return getAllFichiers().stream()
                .filter(fichier -> codeValeur.equals(fichier.getCodeValeur()))
                .mapToLong(fichier -> fichier.getNombre() != null ? fichier.getNombre() : 0)
                .sum();
    }

    // Obtenir les statistiques complètes par code valeur
    public Map<String, Object> getStatsByCodeValeur(String codeValeur) {
        List<Fichier> fichiers = getFichiersByCodeValeur(codeValeur);
        Map<String, Object> stats = new HashMap<>();

        long nombreFichiers = fichiers.size();
        long totalNombre = fichiers.stream()
                .mapToLong(fichier -> fichier.getNombre() != null ? fichier.getNombre() : 0)
                .sum();

        // Use BigDecimal for totalMontant!
        BigDecimal totalMontant = fichiers.stream()
                .map(fichier -> fichier.getMontant() != null ? fichier.getMontant() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        stats.put("nombreFichiers", nombreFichiers);
        stats.put("totalNombre", totalNombre);
        stats.put("totalMontant", totalMontant); // Now it's a BigDecimal

        return stats;
    }


    // Obtenir les statistiques pour tous les codes valeur (30, 31, 32, 33)
    public Map<String, Object> getStatsForAllCodeValeurs() {
        Map<String, Object> stats = new HashMap<>();

        // Statistiques pour chaque code valeur
        Map<String, Object> code30 = getStatsByCodeValeur("30");
        Map<String, Object> code31 = getStatsByCodeValeur("31");
        Map<String, Object> code32 = getStatsByCodeValeur("32");
        Map<String, Object> code33 = getStatsByCodeValeur("33");

        stats.put("code30", code30);
        stats.put("code31", code31);
        stats.put("code32", code32);
        stats.put("code33", code33);

        // Statistiques globales
        long totalFichiers = (Long) code30.get("nombreFichiers") + (Long) code31.get("nombreFichiers") +
                (Long) code32.get("nombreFichiers") + (Long) code33.get("nombreFichiers");
        long totalNombre = (Long) code30.get("totalNombre") + (Long) code31.get("totalNombre") +
                (Long) code32.get("totalNombre") + (Long) code33.get("totalNombre");
        double totalMontant = (Double) code30.get("totalMontant") + (Double) code31.get("totalMontant") +
                (Double) code32.get("totalMontant") + (Double) code33.get("totalMontant");

        stats.put("totalFichiers", totalFichiers);
        stats.put("totalNombre", totalNombre);
        stats.put("totalMontant", totalMontant);

        return stats;
    }
}
