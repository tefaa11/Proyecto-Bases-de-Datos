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
       Los productos ahora se obtienen desde el backend y el catálogo se
       actualiza dinámicamente para reflejar stock real y compras.
    ══════════════════════════════════════════════ */
    let PRODUCTS = [];

    function loadProducts() {
        return fetch('/productos')
            .then(response => {
                if (!response.ok) {
                    throw new Error('No se pudo cargar el catálogo.');
                }
                return response.json();
            })
            .then(products => {
                PRODUCTS = products.map(producto => ({
                    id: producto.idProducto,
                    img: '/img/producto1.jpg',
                    alt: producto.nombre,
                    category: producto.descripcion || 'Producto',
                    icon: 'fas fa-box-open',
                    title: producto.nombre,
                    desc: producto.descripcion || 'Descripción no disponible.',
                    meta: [
                        { label: 'Stock', value: `${producto.stock} unidades` },
                        { label: 'Vence', value: producto.fechaVencimiento || 'N/A' }
                    ],
                    price: new Intl.NumberFormat('es-CO', {
                        style: 'currency',
                        currency: 'COP'
                    }).format(producto.precio).replace('COP', '$'),
                    stock: producto.stock
                }));
            });
        }

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

        grid.addEventListener('click', async function (e) {
            const btn = e.target.closest('.catalog-buy-btn');
            if (!btn) return;

            const id = parseInt(btn.dataset.id, 10);
            const product = PRODUCTS.find(p => p.id === id);
            if (!product) return;

            try {
                const response = await fetch(`/productos/${id}/comprar`, {
                    method: 'POST'
                });

                if (!response.ok) {
                    if (response.status === 400) {
                        alert('No hay stock disponible para este producto.');
                        return;
                    }
                    throw new Error('Error al procesar la compra.');
                }

                const updatedProducto = await response.json();
                PRODUCTS = PRODUCTS.map(p => p.id === updatedProducto.idProducto
                    ? Object.assign({}, p, {
                        stock: updatedProducto.stock,
                        meta: [
                            { label: 'Stock', value: `${updatedProducto.stock} unidades` },
                            { label: 'Vence', value: updatedProducto.fechaVencimiento || 'N/A' }
                        ]
                    })
                    : p
                );

                render();
                alert(`Compra registrada correctamente para ${product.title}.`);
            } catch (error) {
                console.error('Compra fallida:', error);
                alert('No se pudo completar la compra. Intenta de nuevo.');
            }
        });
    }

    /* ══════════════════════════════════════════════
       INIT
    ══════════════════════════════════════════════ */
    document.addEventListener('DOMContentLoaded', function () {
        loadProducts()
            .then(render)
            .catch(error => {
                console.error(error);
                const grid = document.getElementById('catalog-grid');
                if (grid) {
                    grid.innerHTML = '<p class="catalog-error">No se pudo cargar el catálogo de productos.</p>';
                }
            });

        initChips();
        initSearch();
        initResetEmpty();
        initBuyButtons();
    });

})();