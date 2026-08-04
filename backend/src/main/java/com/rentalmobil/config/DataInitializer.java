package com.rentalmobil.config;

import com.rentalmobil.entity.MobilEntity;
import com.rentalmobil.entity.UserEntity;
import com.rentalmobil.repository.MobilRepository;
import com.rentalmobil.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(MobilRepository mobilRepository, UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@semarangcar.com").isEmpty()) {
                userRepository.save(UserEntity.builder()
                        .nama("Administrator Rental")
                        .email("admin@semarangcar.com")
                        .noHp("08111111111")
                        .password("admin123")
                        .role("ADMIN")
                        .build());
            }

            if (userRepository.findByEmail("bebas@gmail.com").isEmpty()) {
                userRepository.save(UserEntity.builder()
                        .id(1L)
                        .nama("Pelanggan Semarang")
                        .email("bebas@gmail.com")
                        .noHp("08123456789")
                        .password("12345")
                        .role("USER")
                        .build());
            }

            mobilRepository.deleteAll();

            mobilRepository.save(MobilEntity.builder()
                    .nama("Avanza Grand New")
                    .merk("Toyota")
                    .harga(350000.0)
                    .transmisi("Manual")
                    .kapasitas(7)
                    .bahanBakar("Bensin")
                    .statusTersedia(true)
                    .deskripsi("Mobil keluarga irit dan nyaman untuk keliling Semarang.")
                    .gambarUrl("https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&q=80&w=400")
                    .build());

            mobilRepository.save(MobilEntity.builder()
                    .nama("Innova Reborn")
                    .merk("Toyota")
                    .harga(600000.0)
                    .transmisi("Otomatis")
                    .kapasitas(7)
                    .bahanBakar("Diesel")
                    .statusTersedia(true)
                    .deskripsi("Nyaman, berkelas, cocok untuk perjalanan bisnis atau keluarga.")
                    .gambarUrl("https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&q=80&w=400")
                    .build());

            mobilRepository.save(MobilEntity.builder()
                    .nama("Brio RS")
                    .merk("Honda")
                    .harga(300000.0)
                    .transmisi("Otomatis")
                    .kapasitas(5)
                    .bahanBakar("Bensin")
                    .statusTersedia(true)
                    .deskripsi("Lincah dan gampang parkir di area perkotaan.")
                    .gambarUrl("https://images.unsplash.com/photo-1590362891991-f776e747a588?auto=format&fit=crop&q=80&w=400")
                    .build());
        };
    }
}