package com.rentalmobil.repository;

import com.rentalmobil.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
}
