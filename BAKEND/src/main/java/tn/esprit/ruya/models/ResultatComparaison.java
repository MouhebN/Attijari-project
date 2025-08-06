package tn.esprit.ruya.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "RESULTAT_COMPARAISON", schema = "RUYA")
public class ResultatComparaison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CARTHAGO")
    private Carthago carthago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FICHIER")
    private Fichier fichier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CTR")
    private CTR ctr;

    @Column(name = "MONTANT_CARTHAGO", precision = 15, scale = 2)
    private BigDecimal montantCarthago;

    @Column(name = "MONTANT_FICHIER", precision = 15, scale = 2)
    private BigDecimal montantFichier;

    @Column(name = "MONTANT_CTR", precision = 15, scale = 2)
    private BigDecimal montantCtr;

    @Column(name = "NOMBRE_CHEQUES_CARTHAGO")
    private Integer nombreChequesCarthago;

    @Column(name = "NOMBRE_CHEQUES_FICHIER")
    private Integer nombreChequesFichier;

    @Column(name = "NOMBRE_CHEQUES_CTR")
    private Integer nombreChequesCtr;

    @Column(name = "RESULTAT_GLOBAL", length = 50)
    private String resultatGlobal;

    @Column(name = "REMARQUES", length = 1000)
    private String remarques;

}