package tn.esprit.ruya.models;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FichierDTO {
    private Long id;
    private String nomFichier;
    private String typeFichier;
    private String natureFichier;
    private String codeValeur;
    private String codEn;
    private String sens;
    private Integer nombre;
    private BigDecimal montant;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FichierDTO(Fichier f) {
        this.id = f.getId();
        this.nomFichier = f.getNomFichier();
        this.typeFichier = f.getTypeFichier();
        this.natureFichier = f.getNatureFichier();
        this.codeValeur = f.getCodeValeur();
        this.codEn = f.getCodEn();
        this.sens = f.getSens();
        this.nombre = f.getNombre();
        this.montant = f.getMontant();
        this.username = f.getUser().getUsername();
        this.createdAt = f.getCreatedAt();
        this.updatedAt = f.getUpdatedAt();

    }
}
