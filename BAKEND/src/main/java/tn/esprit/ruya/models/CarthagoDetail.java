package tn.esprit.ruya.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CARTHAGO_DETAIL", schema = "RUYA")
public class CarthagoDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARTHAGO_ID")
    private Carthago carthago;

    @Column(name = "NUMERO_CHEQUE", length = 50)
    private String numeroCheque; // si présent dans le fichier

    @Column(name = "MONTANT")
    private Double montant; // montant du chèque

    @Column(name = "ETAT_CHEQUE", length = 10)
    private String etatCheque; // 'DEPOSE' ou 'REJETE'

    @Column(name = "LIGNE_ORIGINE", length = 500)
    private String ligneOrigine; // ligne brute du fichier

}