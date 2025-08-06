package tn.esprit.ruya.Fichier.service;

import tn.esprit.ruya.models.Fichier;

import java.util.List;
import java.util.Optional;

public interface IFichierser {
    List<Fichier> getAllFichiers();
    Optional<Fichier> getFichierById(Long id);
    Fichier createFichier(Fichier fichier);
    void deleteFichier(Long id);
}
