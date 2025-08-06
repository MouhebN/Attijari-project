package tn.esprit.ruya.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "CTR", schema = "RUYA")
public class CTR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FICHIER_ID")
    private Fichier fichier;

    @OneToMany(mappedBy = "ctr", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultatComparaison> resultatComparaisons = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARTHAGO_ID")
    private Carthago carthago;
    @Column(name = "NOMBRE_TOTAL_CARTHAGO")
    private Integer nombreTotalCarthago;

    @Column(name = "MONTANT_TOTAL_CARTHAGO", precision = 15, scale = 2)
    private BigDecimal montantTotalCarthago;

    @Column(name = "NOMBRE_DEP_CARTHAGO")
    private Integer nombreDepCarthago; // chèques déposés

    @Column(name = "MONTANT_DEP_CARTHAGO", precision = 15, scale = 2)
    private BigDecimal montantDepCarthago;

    @Column(name = "NOMBRE_REJ_CARTHAGO")
    private Integer nombreRejCarthago; // chèques rejetés

    @Column(name = "MONTANT_REJ_CARTHAGO", precision = 15, scale = 2)
    private BigDecimal montantRejCarthago;

    // Résumé Fichier (Encaisse Valeur)
    @Column(name = "NOMBRE_FICHIER")
    private Integer nombreFichier;

    @Column(name = "MONTANT_FICHIER", precision = 20, scale = 6)
    private BigDecimal montantFichier;

    // Comparaison
    @Column(name = "NOMBRE_OK", length = 1)
    private String nombreOk; // 'Y' ou 'N'

    @Column(name = "MONTANT_OK", length = 1)
    private String montantOk; // 'Y' ou 'N'

    @Column(name = "CREATED_AT")
    private LocalDate createdAt = LocalDate.now();

    // Constructeur
    public CTR(Fichier fichier, Carthago carthago) {
        this.fichier = fichier;
        this.carthago = carthago;
        this.createdAt = LocalDate.now();
    }

}