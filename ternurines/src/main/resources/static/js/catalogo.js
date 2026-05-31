/**
 * catalogo.js — Ternurines
 * Gestiona los datos de productos, el renderizado del grid,
 * los filtros por categoría y la búsqueda en tiempo real.
 *
 * Ruta: resources/static/js/catalogo.js
 */

(function () {
    'use strict';

    /* ══════════════════════════════════════════════
       DATOS DE PRODUCTOS
       Agrega, edita o elimina objetos de este array
       para gestionar el catálogo sin tocar el HTML.
    ══════════════════════════════════════════════ */
    const PRODUCTS = [
        {
            id: 1,
            img: '/img/producto1.jpg',
            alt: 'Concentrado Pro-Plan',
            category: 'Alimento',
            icon: 'fas fa-drumstick-bite',
            title: 'Concentrado Pro-Plan 10 kg',
            desc: 'Alimento balanceado para perros adultos, ideal para nutrición diaria y mantenimiento saludable.',
            meta: [
                { label: 'Presentación', value: 'Bolsa 10 kg' },
                { label: 'Stock',        value: '50 unidades' },
            ],
            price: '$60.000',
        },
        {
            id: 2,
            img: '/img/producto2.jpg',
            alt: 'Shampoo antipulgas',
            category: 'Higiene',
            icon: 'fas fa-soap',
            title: 'Shampoo antipulgas 250 ml',
            desc: 'Limpieza suave con fórmula especializada para ayudar al control de pulgas y mantener el pelaje limpio.',
            meta: [
                { label: 'Contenido', value: '250 ml' },
                { label: 'Stock',     value: '20 unidades' },
            ],
            price: '$15.000',
        },
        {
            id: 3,
            img: '/img/producto3.jpg',
            alt: 'Arena sanitaria premium',
            category: 'Higiene',
            icon: 'fas fa-cat',
            title: 'Arena sanitaria premium',
            desc: 'Arena aglomerante de alta absorción, pensada para reducir olores y facilitar la limpieza diaria.',
            meta: [
                { label: 'Presentación', value: '8 kg' },
                { label: 'Stock',        value: '34 unidades' },
            ],
            price: '$28.000',
        },
        {
            id: 4,
            img: '/img/producto4.jpg',
            alt: 'Juguete mordedor',
            category: 'Juguete',
            icon: 'fas fa-bone',
            title: 'Mordedor resistente',
            desc: 'Juguete ideal para entretenimiento, estimulación y cuidado dental en perros medianos y grandes.',
            meta: [
                { label: 'Material', value: 'Caucho flexible' },
                { label: 'Stock',    value: '18 unidades' },
            ],
            price: '$22.000',
        },
        {
            id: 5,
            img: '/img/producto5.jpg',
            alt: 'Collar ajustable',
            category: 'Accesorio',
            icon: 'fas fa-paw',
            title: 'Collar ajustable clásico',
            desc: 'Collar cómodo y resistente con ajuste seguro para el uso diario en paseos y actividades al aire libre.',
            meta: [
                { label: 'Talla', value: 'Mediana' },
                { label: 'Stock', value: '26 unidades' },
            ],
            price: '$18.000',
        },
        {
            id: 6,
            img: '/img/producto6.jpg',
            alt: 'Snack dental',
            category: 'Cuidado oral',
            icon: 'fas fa-tooth',
            title: 'Snack dental premium',
            desc: 'Premio funcional que ayuda a la limpieza dental y al control del mal aliento en perros adultos.',
            meta: [
                { label: 'Contenido', value: 'Paquete x 20' },
                { label: 'Stock',     value: '40 unidades' },
            ],
            price: '$19.000',
        },
        {
            id: 7,
            img: '/img/producto7.jpg',
            alt: 'Suplemento vitamínico',
            category: 'Suplemento',
            icon: 'fas fa-capsules',
            title: 'Suplemento vitamínico',
            desc: 'Apoyo nutricional para fortalecer defensas, energía y vitalidad en distintas etapas de vida.',
            meta: [
                { label: 'Presentación', value: 'Frasco x 60' },
                { label: 'Stock',        value: '15 unidades' },
            ],
            price: '$32.000',
        },
        {
            id: 8,
            img: '/img/producto8.jpg',
            alt: 'Cama acolchada',
            category: 'Descanso',
            icon: 'fas fa-bed',
            title: 'Cama acolchada suave',
            desc: 'Espacio cómodo y acogedor para el descanso diario de mascotas pequeñas y medianas.',
            meta: [
                { label: 'Tamaño', value: '70 x 50 cm' },
                { label: 'Stock',  value: '12 unidades' },
            ],
            price: '$75.000',
        },
    ];

    /* ══════════════════════════════════════════════
       ESTADO
    ══════════════════════════════════════════════ */
    let activeFilter = 'all';
    let searchQuery  = '';

    /* ══════════════════════════════════════════════
       UTILIDADES
    ══════════════════════════════════════════════ */
    /** Devuelve los productos que coinciden con el filtro y búsqueda activos */
    function getFiltered() {
        return PRODUCTS.filter(p => {
            const matchCat    = activeFilter === 'all' || p.category === activeFilter;
            const matchSearch = p.title.toLowerCase().includes(searchQuery) ||
                                p.desc.toLowerCase().includes(searchQuery)  ||
                                p.category.toLowerCase().includes(searchQuery);
            return matchCat && matchSearch;
        });
    }

    /** Genera el HTML de una tarjeta de producto */
    function buildCard(p) {
        const metaHTML = p.meta.map(m => `
            <div class="catalog-meta-box">
                <small>${m.label}</small>
                <strong>${m.value}</strong>
            </div>
        `).join('');

        return `
        <article class="catalog-product-card" data-id="${p.id}" data-category="${p.category}">
            <img
                src="${p.img}"
                alt="${p.alt}"
                class="catalog-product-img"
                onerror="this.style.display='none';this.nextElementSibling.style.display='flex';"
            >
            <div class="catalog-product-placeholder">
                <i class="fas fa-image"></i>
                <span>${p.alt}</span>
            </div>
            <div class="catalog-product-body">
                <div class="catalog-product-tag">
                    <i class="${p.icon}"></i> ${p.category}
                </div>
                <h3 class="catalog-product-title">${p.title}</h3>
                <p class="catalog-product-desc">${p.desc}</p>
                <div class="catalog-product-meta">${metaHTML}</div>
                <div class="catalog-price-row">
                    <div class="catalog-price">${p.price}</div>
                    <button class="catalog-buy-btn" type="button" data-id="${p.id}">
                        Comprar
                    </button>
                </div>
            </div>
        </article>`;
    }

    /* ══════════════════════════════════════════════
       RENDER
    ══════════════════════════════════════════════ */
    function render() {
        const grid    = document.getElementById('catalog-grid');
        const empty   = document.getElementById('catalog-empty');
        const counter = document.getElementById('catalog-count');

        const filtered = getFiltered();

        if (filtered.length === 0) {
            grid.innerHTML  = '';
            empty.style.display = 'flex';
        } else {
            grid.innerHTML  = filtered.map(buildCard).join('');
            empty.style.display = 'none';
        }

        // Actualiza contador
        if (counter) {
            counter.innerHTML = `Mostrando <strong>${filtered.length}</strong> producto${filtered.length !== 1 ? 's' : ''}`;
        }
    }

    /* ══════════════════════════════════════════════
       EVENTOS
    ══════════════════════════════════════════════ */

    /** Chips de categoría */
    function initChips() {
        const container = document.getElementById('catalog-chips');
        if (!container) return;

        container.addEventListener('click', function (e) {
            const btn = e.target.closest('.catalog-chip');
            if (!btn) return;

            // Actualiza estado activo
            container.querySelectorAll('.catalog-chip').forEach(c => c.classList.remove('active'));
            btn.classList.add('active');

            activeFilter = btn.dataset.filter;
            render();
        });
    }

    /** Buscador en tiempo real */
    function initSearch() {
        const input = document.getElementById('catalog-search');
        if (!input) return;

        input.addEventListener('input', function () {
            searchQuery = this.value.trim().toLowerCase();
            render();
        });
    }

    /** Botón "Ver todos" en estado vacío */
    function initResetEmpty() {
        const btn = document.getElementById('catalog-empty-reset');
        if (!btn) return;

        btn.addEventListener('click', function () {
            activeFilter = 'all';
            searchQuery  = '';

            // Resetea chips y buscador visualmente
            document.querySelectorAll('.catalog-chip').forEach(c => {
                c.classList.toggle('active', c.dataset.filter === 'all');
            });
            const input = document.getElementById('catalog-search');
            if (input) input.value = '';

            render();
        });
    }

    /** Delegar clic en botones "Comprar" */
    function initBuyButtons() {
        const grid = document.getElementById('catalog-grid');
        if (!grid) return;

        grid.addEventListener('click', function (e) {
            const btn = e.target.closest('.catalog-buy-btn');
            if (!btn) return;

            const id      = parseInt(btn.dataset.id, 10);
            const product = PRODUCTS.find(p => p.id === id);
            if (!product) return;

            // TODO: conectar con tu lógica de carrito / WhatsApp / pedido
            console.log('[Ternurines] Producto seleccionado:', product);
            alert(`¡Listo! Seleccionaste:\n"${product.title}" — ${product.price}`);
        });
    }

    /* ══════════════════════════════════════════════
       INIT
    ══════════════════════════════════════════════ */
    document.addEventListener('DOMContentLoaded', function () {
        render();
        initChips();
        initSearch();
        initResetEmpty();
        initBuyButtons();
    });

})();