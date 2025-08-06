package tn.esprit.ruya.models;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FichierUpdateDTO {
    public String nomFichier;
    public String typeFichier;
    public String natureFichier;
    public String codeValeur;
    public String codEn;
    public String sens;
    public Integer nombre;
    public Double montant;
    public String formatFichier;
    public LocalDateTime updatedAt;
}
