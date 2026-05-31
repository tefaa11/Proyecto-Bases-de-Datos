/**
 * main.js — Ternurines
 * Las funciones de login se definen de forma global (fuera de jQuery)
 * para que los onclick del HTML las encuentren sin importar si jQuery
 * está cargado o no. Las funciones que dependen de jQuery se inicializan
 * dentro de $(document).ready solo si jQuery existe en la página.
 */

/* ══════════════════════════════════════════════
   LOGIN — Selección de rol (no depende de jQuery)
══════════════════════════════════════════════ */
function seleccionarRol(el, rol) {
    document.querySelectorAll('.rol-tab').forEach(function (tab) {
        tab.classList.remove('active');
        tab.setAttribute('aria-pressed', 'false');
    });
    el.classList.add('active');
    el.setAttribute('aria-pressed', 'true');

    var rolInput = document.getElementById('rolSeleccionado');
    if (rolInput) rolInput.value = rol;
}

/* ══════════════════════════════════════════════
   LOGIN — Toggle visibilidad contraseña (no depende de jQuery)
══════════════════════════════════════════════ */
function togglePassword() {
    var input = document.getElementById('contrasena');
    var icon  = document.getElementById('eyeIcon');
    if (!input || !icon) return;

    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.replace('fa-eye', 'fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.replace('fa-eye-slash', 'fa-eye');
    }
}

function getStoredUser() {
    try {
        return JSON.parse(sessionStorage.getItem('ternurinesUser')) || null;
    } catch (error) {
        return null;
    }
}

function setStoredUser(user) {
    sessionStorage.setItem('ternurinesUser', JSON.stringify(user));
}

async function authenticateUser(role, correo, contrasena) {
    const endpoint = role === 'cliente'
        ? `/clientes/correo/${encodeURIComponent(correo)}`
        : `/veterinarios/correo/${encodeURIComponent(correo)}`;

    const response = await fetch(endpoint);
    if (!response.ok) {
        return null;
    }

    const user = await response.json();
    const passwordField = role === 'cliente' ? user.contrasenia : user.contrasena;
    return passwordField === contrasena ? user : null;
}

function initLoginPage() {
    const loginButton = document.querySelector('.btn-login');
    if (!loginButton || !document.getElementById('rolSeleccionado')) {
        return;
    }

    loginButton.type = 'button';
    loginButton.addEventListener('click', async function () {
        const role = document.getElementById('rolSeleccionado').value;
        const correo = document.getElementById('correo').value.trim();
        const contrasena = document.getElementById('contrasena').value;

        if (!correo || !contrasena) {
            alert('Por favor ingresa correo y contraseña.');
            return;
        }

        if (role !== 'cliente' && role !== 'veterinario') {
            alert('Por ahora solo los roles Cliente y Veterinario tienen acceso directo.');
            return;
        }

        try {
            const user = await authenticateUser(role, correo, contrasena);
            if (!user) {
                alert('Credenciales incorrectas. Verifica correo y contraseña.');
                return;
            }

            const userId = role === 'cliente' ? user.idCliente : user.idVeterinario;
            setStoredUser({
                role,
                id: userId,
                nombre: user.nombre,
                correo: user.correo
            });

            window.location.href = role === 'cliente' ? 'usuario.html' : 'veterinario.html';
        } catch (error) {
            console.error('Login error:', error);
            alert('No se pudo iniciar sesión. Intenta de nuevo más tarde.');
        }
    });
}

function parseAppointmentTime(label) {
    if (!label) return '09:00:00';
    const normalized = label.toLowerCase().replace(/\s+/g, '');
    if (normalized.includes('a.m.') || normalized.includes('am')) {
        return normalized.substring(0, 5) + ':00';
    }
    if (normalized.includes('p.m.') || normalized.includes('pm')) {
        let [hour, minute] = normalized.substring(0, 5).split(':');
        const hourInt = parseInt(hour, 10);
        if (hourInt < 12) {
            hour = String(hourInt + 12).padStart(2, '0');
        }
        return `${hour}:${minute}:00`;
    }
    return normalized.substring(0, 5) + ':00';
}

function initBookingPage() {
    const petSelect = document.getElementById('mascota');
    const bookingButton = document.querySelector('.btn-login');
    if (!petSelect || !bookingButton) {
        return;
    }

    const user = getStoredUser();
    const clienteId = user && user.role === 'cliente' ? user.id : 1;

    fetch(`/mascotas/cliente/${clienteId}`)
        .then(response => response.ok ? response.json() : [])
        .then(mascotas => {
            if (!Array.isArray(mascotas) || mascotas.length === 0) {
                petSelect.innerHTML = '<option value="">No hay mascotas registradas</option>';
                return;
            }
            petSelect.innerHTML = mascotas.map(mascota =>
                `<option value="${mascota.idMascota}">${mascota.nombre} (${mascota.especie})</option>`
            ).join('');
        })
        .catch(error => {
            console.error('Error cargando mascotas:', error);
            petSelect.innerHTML = '<option value="">No se pudo cargar mascotas</option>';
        });

    bookingButton.type = 'button';
    bookingButton.addEventListener('click', async function () {
        const idMascota = parseInt(petSelect.value, 10);
        const fecha = document.getElementById('fecha').value;
        const hora = parseAppointmentTime(document.getElementById('hora').value);
        const motivo = document.getElementById('motivo').value.trim();

        if (!idMascota || !fecha || !hora || !motivo) {
            alert('Completa todos los datos de la cita antes de confirmar.');
            return;
        }

        const cita = {
            idMascota,
            idVeterinario: 1,
            idRecepcionista: 1,
            fecha,
            hora,
            motivo,
            estado: 'Pendiente'
        };

        try {
            const response = await fetch('/citas', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(cita)
            });

            if (!response.ok) {
                throw new Error('No se pudo guardar la cita.');
            }

            alert('Cita agendada correctamente.');
            window.location.href = 'usuario.html';
        } catch (error) {
            console.error('Error guardando cita:', error);
            alert('No se pudo agendar la cita. Intenta de nuevo.');
        }
    });
}

function updatePageGreeting() {
    const profileName = document.querySelector('[data-user-name]');
    const user = getStoredUser();
    if (!profileName || !user) {
        return;
    }
    profileName.textContent = user.nombre;
}

/* ══════════════════════════════════════════════
   INDEX — Funciones que requieren jQuery + plugins
   Solo se ejecutan si jQuery está disponible
══════════════════════════════════════════════ */
document.addEventListener('DOMContentLoaded', function () {
    initLoginPage();
    initBookingPage();
    updatePageGreeting();

    /* ── Back to Top (solo en páginas que tienen el botón) ── */
    var backToTopBtn = document.getElementById('backToTop');
    if (backToTopBtn) {
        window.addEventListener('scroll', function () {
            backToTopBtn.classList.toggle('visible', window.scrollY > 300);
        });
        backToTopBtn.addEventListener('click', function (e) {
            e.preventDefault();
            window.scrollTo({ top: 0, behavior: 'smooth' });
        });
    }

    /* ── Owl Carousel + Bootstrap Carousel (requieren jQuery) ── */
    if (typeof $ === 'undefined') return;

    /* Hero Carousel */
    if ($('#heroCarousel').length) {
        $('#heroCarousel').carousel({ interval: 5000 });
    }

    /* Testimonios */
    if ($('.testimonial-carousel').length) {
        $('.testimonial-carousel').owlCarousel({
            loop: true,
            margin: 0,
            nav: false,
            dots: true,
            autoplay: true,
            autoplayTimeout: 5000,
            autoplayHoverPause: true,
            responsive: {
                0:   { items: 1 },
                768: { items: 2 },
                992: { items: 3 }
            }
        });
    }

});