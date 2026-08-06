@echo off
title SemarangCar Application Launcher
color 0A
echo ========================================================
echo          MEMULAI APLIKASI RENTAL MOBIL SEMARANGCAR
echo ========================================================
echo.
cd /d "%~dp0backend"
echo [1/2] Memeriksa Direktori Backend: %CD%
echo [2/2] Menjalankan Server Backend Spring Boot...
echo.
call mvn spring-boot:run
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Terjadi kesalahan saat menjalankan backend.
)
echo.
echo ========================================================
echo   Server dihentikan. Tekan tombol apa saja untuk keluar.
echo ========================================================
pause
