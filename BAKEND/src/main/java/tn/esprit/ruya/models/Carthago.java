package tn.esprit.ruya.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "CARTHAGO", schema = "RUYA")
public class Carthago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOM_FICHIER")
    private String nomFichier;

    @Column(name = "DATE_IMPORT")
    private LocalDate dateImport = LocalDate.now();

    @Column(name = "CONTENU", columnDefinition = "CLOB")
    private String contenu;

    @Column(name = "NOMBRE_ENTETE")
    private Integer nombreEntete;

    @Column(name = "MONTANT_ENTETE")
    private Double montantEntete;

    @Column(name = "NOMBRE_OK", length = 1)
    private String nombreOk; // 'Y' or 'N'

    @Column(name = "MONTANT_OK", length = 1)
    private String montantOk; // 'Y' or 'N'


    // Relation One-to-Many avec CarthagoDetail
    @OneToMany(mappedBy = "carthago", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CarthagoDetail> carthagoDetails;


    // Constructeur
    public Carthago(String nomFichier, String contenu) {
        this.nomFichier = nomFichier;
        this.contenu = contenu;
        this.dateImport = LocalDate.now();
    }



}