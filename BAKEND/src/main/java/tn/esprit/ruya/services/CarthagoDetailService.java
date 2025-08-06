package tn.esprit.ruya.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.ruya.models.CarthagoDetail;
import tn.esprit.ruya.repositories.CarthagoDetailRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CarthagoDetailService {
    private final CarthagoDetailRepository carthagoDetailRepository;

    public List<CarthagoDetail> getAllCarthagoDetails() {
        return carthagoDetailRepository.findAll();
    }
    public long countRemis() {
        return carthagoDetailRepository.countByEtatCheque("DEPOSE");
    }

    public double sumMontantRemis() {
        Double result = carthagoDetailRepository.sumMontantByEtat("DEPOSE");
        return result != null ? result : 0.0;
    }


}
