package tn.esprit.ruya.models;


import lombok.Data;

@Data
public class CarthagoDetailDto {
    private Long id;
    private Long carthagoId;
    private String numeroCheque;
    private Double montant;
    private String etatCheque;
    private String ligneOrigine;
}
