package com.rentalmobil.controller;

import com.rentalmobil.entity.MobilEntity;
import com.rentalmobil.repository.MobilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mobil")
public class MobilController {

    @Autowired
    private MobilRepository mobilRepository;

    @GetMapping
    public ResponseEntity<List<MobilEntity>> getAllMobil(
            @RequestParam(required = false) String transmisi,
            @RequestParam(required = false) Integer kapasitas,
            @RequestParam(required = false) Double maxHarga,
            @RequestParam(required = false) String search) {
        
        List<MobilEntity> result = mobilRepository.filterMobil(transmisi, kapasitas, maxHarga, search);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MobilEntity> getMobilById(@PathVariable Long id) {
        return mobilRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
