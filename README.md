# 🚗 SemarangCar - Web Rental Mobil

Aplikasi web sistem penyewaan mobil modern berbasis Spring Boot 3 & Vanilla Web.

---

## 🛠️ Tech Stack
* **Backend**: Java 17, Spring Boot 3.2.3, Spring Data JPA, Spring Security (BCrypt), Maven
* **Frontend**: HTML5, CSS3, JavaScript ES6, Bootstrap 5.3
* **Database**: H2 Database (Dev) / MySQL & PostgreSQL (Prod)
* **DevOps**: Docker, Docker Compose

---

## 💻 Prasyarat (Harus Di-install)
1. **Java JDK 17** atau versi lebih baru
2. **Apache Maven 3.8+** *(atau jalankan via script run.bat)*
3. **Web Browser** (Chrome / Edge / Firefox)

---

## 🚀 Cara Menjalankan Aplikasi

### Opsi 1: Pakai `run.bat` (Windows)
Klik ganda file **`run.bat`** di direktori utama proyek.

### Opsi 2: Via Terminal (CMD / PowerShell)
```bash
cd backend
mvn spring-boot:run
```

Buka browser di: **`http://localhost:8080`**

---

## 🔑 Akun Login Demo

| Role | Email | Password | Akses Utama |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@semarangcar.com` | `admin123` | Dashboard Admin & Kelola Armada |
| **User** | `bebas@gmail.com` | `12345` | Reservasi Sewa & Riwayat |

---

## ⭐ Fitur Utama
* **Anti Double-Booking**: Mencegah mobil disewa di tanggal yang sama.
* **Keamanan BCrypt**: Password terenkripsi & *Soft Delete* armada.
* **Metode Pembayaran**: COD, Transfer BCA, Mandiri, & QRIS.
* **Integrasi WhatsApp**: Pesan konfirmasi otomatis ke WA Admin.
* **Cetak E-Receipt PDF**: Cetak invoice sewa resmi.
* **Laporan Keuangan Admin**: Rekapitulasi omset & laporan cetak.
* **Reset Password**: Pembaruan password mandiri jika lupa.
