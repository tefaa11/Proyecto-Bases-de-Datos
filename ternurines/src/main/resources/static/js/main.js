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

/* ══════════════════════════════════════════════
   INDEX — Funciones que requieren jQuery + plugins
   Solo se ejecutan si jQuery está disponible
══════════════════════════════════════════════ */
document.addEventListener('DOMContentLoaded', function () {

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