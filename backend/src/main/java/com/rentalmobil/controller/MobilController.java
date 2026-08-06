package com.rentalmobil.controller;

import com.rentalmobil.entity.MobilEntity;
import com.rentalmobil.service.MobilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mobil")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MobilController {

    private final MobilService mobilService;

    @GetMapping
    public ResponseEntity<List<MobilEntity>> getAllMobil(
            @RequestParam(required = false) String transmisi,
            @RequestParam(required = false) Integer kapasitas,
            @RequestParam(required = false) Double maxHarga,
            @RequestParam(required = false) String search) {
        List<MobilEntity> list = mobilService.getAllMobil(transmisi, kapasitas, maxHarga, search);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MobilEntity> getMobilById(@PathVariable Long id) {
        MobilEntity mobil = mobilService.getMobilById(id);
        return ResponseEntity.ok(mobil);
    }

    @PostMapping
    public ResponseEntity<MobilEntity> createMobil(@RequestBody MobilEntity mobil) {
        MobilEntity created = mobilService.createMobil(mobil);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MobilEntity> updateMobil(@PathVariable Long id, @RequestBody MobilEntity mobil) {
        MobilEntity updated = mobilService.updateMobil(id, mobil);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMobil(@PathVariable Long id) {
        mobilService.deleteMobil(id);
        return ResponseEntity.noContent().build();
    }
}
