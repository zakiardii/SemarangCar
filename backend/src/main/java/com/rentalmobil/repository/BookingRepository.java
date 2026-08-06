package com.rentalmobil.repository;

import com.rentalmobil.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT b FROM BookingEntity b WHERE b.mobil.id = :mobilId AND b.status != 'REJECTED'")
    List<BookingEntity> findActiveBookingsByMobilId(@Param("mobilId") Long mobilId);
}
