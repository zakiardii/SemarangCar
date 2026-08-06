const API_BASE_URL = window.location.origin.includes('localhost') || window.location.origin.includes('127.0.0.1')
    ? 'http://localhost:8080/api'
    : '/api';

document.addEventListener('DOMContentLoaded', () => {
    localStorage.removeItem('theme');
    document.documentElement.removeAttribute('data-bs-theme');
    setupNavbar();
    setupBackToTop();
});

function escapeHTML(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

function getAuthHeaders() {
    const token = localStorage.getItem('token');
    const headers = { 'Content-Type': 'application/json' };
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    return headers;
}

function setupNavbar() {
    const currentPath = window.location.pathname.split('/').pop() || 'index.html';
    const navLinks = document.querySelectorAll('.nav-link');

    navLinks.forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        }
    });

    const currentUser = JSON.parse(localStorage.getItem('user'));
    const authNav = document.getElementById('authNavLinks');

    if (authNav) {
        if (currentUser) {
            const isAdmin = currentUser.role === 'ADMIN';
            const adminMenuItem = isAdmin ? `<li><a class="dropdown-item fw-semibold text-indigo" href="admin.html"><i class="bi bi-speedometer2 me-2"></i>Dashboard Admin</a></li><li><hr class="dropdown-divider"></li>` : '';

            authNav.innerHTML = `
                <div class="dropdown">
                    <button class="btn btn-secondary-custom dropdown-toggle px-3 py-2 d-flex align-items-center gap-2" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-person-circle fs-5 text-primary"></i>
                        <span class="fw-semibold">${escapeHTML(currentUser.nama)}</span>
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end border-0 shadow-lg p-2 rounded-4">
                        ${adminMenuItem}
                        <li><a class="dropdown-item rounded-3 py-2" href="riwayat.html"><i class="bi bi-clock-history me-2"></i>Riwayat Booking</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item rounded-3 py-2 text-danger fw-semibold" href="#" onclick="logout()"><i class="bi bi-box-arrow-right me-2"></i>Logout</a></li>
                    </ul>
                </div>
            `;
        } else {
            authNav.innerHTML = `
                <div class="d-flex align-items-center gap-2">
                    <a href="login.html" class="btn btn-secondary-custom px-4">Masuk</a>
                    <a href="register.html" class="btn btn-primary-custom px-4">Daftar</a>
                </div>
            `;
        }
    }
}

function logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    showToast('Berhasil logout dari SemarangCar', 'success');
    setTimeout(() => {
        window.location.href = 'index.html';
    }, 800);
}

function setupBackToTop() {
    const btn = document.getElementById('btnBackToTop');
    if (!btn) return;

    window.addEventListener('scroll', () => {
        if (window.scrollY > 300) {
            btn.style.display = 'flex';
            btn.style.alignItems = 'center';
            btn.style.justifyContent = 'center';
        } else {
            btn.style.display = 'none';
        }
    });

    btn.addEventListener('click', () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
}

function showToast(message, type = 'info') {
    const toastContainer = document.getElementById('toastContainer');
    if (!toastContainer) return;

    const toastId = 'toast-' + Date.now();
    let bgClass = 'bg-dark text-white';
    let icon = '<i class="bi bi-info-circle-fill me-2 fs-5"></i>';

    if (type === 'success') {
        bgClass = 'bg-success text-white';
        icon = '<i class="bi bi-check-circle-fill me-2 fs-5"></i>';
    } else if (type === 'danger') {
        bgClass = 'bg-danger text-white';
        icon = '<i class="bi bi-exclamation-triangle-fill me-2 fs-5"></i>';
    }

    const toastHTML = `
        <div id="${toastId}" class="toast align-items-center ${bgClass} border-0 shadow-lg rounded-4 mb-2" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex p-3">
                <div class="toast-body d-flex align-items-center fw-semibold">
                    ${icon} ${escapeHTML(message)}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    `;

    toastContainer.insertAdjacentHTML('beforeend', toastHTML);
    const toastElement = document.getElementById(toastId);
    const bsToast = new bootstrap.Toast(toastElement, { delay: 3500 });
    bsToast.show();

    toastElement.addEventListener('hidden.bs.toast', () => {
        toastElement.remove();
    });
}

function formatRupiah(number) {
    if (number === null || number === undefined) return 'Rp0';
    return new Intl.NumberFormat('id-ID', { style: 'currency', currency: 'IDR', maximumFractionDigits: 0 }).format(number);
}
