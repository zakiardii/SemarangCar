package com.rentalmobil.service;

import com.rentalmobil.entity.MobilEntity;
import com.rentalmobil.exception.ResourceNotFoundException;
import com.rentalmobil.repository.MobilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MobilService {

    private final MobilRepository mobilRepository;

    @Transactional(readOnly = true)
    public List<MobilEntity> getAllMobil(String transmisi, Integer kapasitas, Double maxHarga, String search) {
        return mobilRepository.filterMobil(transmisi, kapasitas, maxHarga, search);
    }

    @Transactional(readOnly = true)
    public MobilEntity getMobilById(Long id) {
        MobilEntity mobil = mobilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mobil tidak ditemukan dengan ID: " + id));
        if (Boolean.TRUE.equals(mobil.getIsDeleted())) {
            throw new ResourceNotFoundException("Mobil tidak ditemukan atau telah dihapus");
        }
        return mobil;
    }

    @Transactional
    public MobilEntity createMobil(MobilEntity mobil) {
        if (mobil.getStatusTersedia() == null) {
            mobil.setStatusTersedia(true);
        }
        mobil.setIsDeleted(false);
        return mobilRepository.save(mobil);
    }

    @Transactional
    public MobilEntity updateMobil(Long id, MobilEntity updatedMobil) {
        MobilEntity existing = getMobilById(id);
        existing.setNama(updatedMobil.getNama());
        existing.setMerk(updatedMobil.getMerk());
        existing.setHarga(updatedMobil.getHarga());
        existing.setTransmisi(updatedMobil.getTransmisi());
        existing.setKapasitas(updatedMobil.getKapasitas());
        existing.setBahanBakar(updatedMobil.getBahanBakar());
        existing.setStatusTersedia(updatedMobil.getStatusTersedia());
        existing.setDeskripsi(updatedMobil.getDeskripsi());
        if (updatedMobil.getGambarUrl() != null && !updatedMobil.getGambarUrl().isEmpty()) {
            existing.setGambarUrl(updatedMobil.getGambarUrl());
        }
        return mobilRepository.save(existing);
    }

    @Transactional
    public void deleteMobil(Long id) {
        MobilEntity existing = getMobilById(id);
        existing.setIsDeleted(true); // Soft Delete
        existing.setStatusTersedia(false);
        mobilRepository.save(existing);
    }
}
