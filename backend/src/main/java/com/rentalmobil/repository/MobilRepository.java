package com.rentalmobil.repository;

import com.rentalmobil.entity.MobilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MobilRepository extends JpaRepository<MobilEntity, Long> {

    @Query("SELECT m FROM MobilEntity m WHERE " +
           "(m.isDeleted = false OR m.isDeleted IS NULL) AND " +
           "(:transmisi IS NULL OR m.transmisi = :transmisi) AND " +
           "(:kapasitas IS NULL OR m.kapasitas >= :kapasitas) AND " +
           "(:maxHarga IS NULL OR m.harga <= :maxHarga) AND " +
           "(:search IS NULL OR LOWER(m.nama) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<MobilEntity> filterMobil(@Param("transmisi") String transmisi,
                                 @Param("kapasitas") Integer kapasitas,
                                 @Param("maxHarga") Double maxHarga,
                                 @Param("search") String search);
}
