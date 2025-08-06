package tn.esprit.ruya.models;

import java.math.BigDecimal;

public record CTRDto(
        Long id,
        String nomFichier,
        Integer nombreTotalCarthago,
        BigDecimal montantTotalCarthago,
        Integer nombreFichier,
        BigDecimal montantFichier,
        String nombreOk,
        String montantOk
) {
    public static CTRDto from(CTR ctr) {
        return new CTRDto(
                ctr.getId(),
                ctr.getFichier().getNomFichier(),
                ctr.getNombreTotalCarthago(),
                ctr.getMontantTotalCarthago(),
                ctr.getNombreFichier(),
                ctr.getMontantFichier(),
                ctr.getNombreOk(),
                ctr.getMontantOk()
        );
    }
}
