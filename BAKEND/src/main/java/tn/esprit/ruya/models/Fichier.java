package tn.esprit.ruya.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "FICHIERS", schema = "RUYA")
public class Fichier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FICHIER")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_USER", nullable = false)
    private User user;

    @Column(name = "NOM_FICHIER", nullable = false)
    private String nomFichier;

    @Column(name = "TYPE_FICHIER", nullable = false)
    private String typeFichier;

    @Column(name = "NATURE_FICHIER", nullable = false)
    private String natureFichier;

    @Column(name = "CODE_VALEUR", nullable = false)
    private String codeValeur;

    @Column(name = "COD_EN")
    private String codEn;

    @Column(name = "SENS")
    private String sens;

    @Column(name = "NOMBRE")
    private Integer nombre;

    @Column(name = "MONTANT")
    private BigDecimal montant;


    @Column(name = "format_fichier")
    private String formatFichier;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "fichier", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<CTR> ctrs = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "fichier", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ResultatComparaison> resultats = new java.util.ArrayList<>();


    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}