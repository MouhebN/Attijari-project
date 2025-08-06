package tn.esprit.ruya.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ruya.mappers.CarthagoDetailMapper;
import tn.esprit.ruya.models.CarthagoDetailDto;
import tn.esprit.ruya.services.CarthagoDetailService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carthago-detail")
@RequiredArgsConstructor
public class CarthagoDetailController {
    private final CarthagoDetailService carthagoDetailService;

    @GetMapping
    public ResponseEntity<List<CarthagoDetailDto>> getAll() {
        var entities = carthagoDetailService.getAllCarthagoDetails();
        var dtos = entities.stream()
                .map(CarthagoDetailMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }
    @GetMapping("/stats/remis")
    public ResponseEntity<Map<String, Object>> getRemisStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRemis", carthagoDetailService.countRemis());
        stats.put("montantRemis", carthagoDetailService.sumMontantRemis());
        return ResponseEntity.ok(stats);
    }

}
