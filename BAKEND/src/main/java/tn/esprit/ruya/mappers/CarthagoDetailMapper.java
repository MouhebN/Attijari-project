package tn.esprit.ruya.mappers;



import tn.esprit.ruya.models.CarthagoDetail;
import tn.esprit.ruya.models.CarthagoDetailDto;


public class CarthagoDetailMapper {
    public static CarthagoDetailDto toDto(CarthagoDetail entity) {
        CarthagoDetailDto dto = new CarthagoDetailDto();
        dto.setId(entity.getId());
        dto.setCarthagoId(entity.getCarthago() != null ? entity.getCarthago().getId() : null);
        dto.setNumeroCheque(entity.getNumeroCheque());
        dto.setMontant(entity.getMontant());
        dto.setEtatCheque(entity.getEtatCheque());
        dto.setLigneOrigine(entity.getLigneOrigine());
        return dto;
    }
}
