package tn.esprit.ruya.Fichier.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.ruya.models.Fichier;

@Repository
public interface IFichierrepo extends CrudRepository<Fichier,Long> {
}
