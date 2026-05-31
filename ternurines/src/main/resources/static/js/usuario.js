/**
 * usuario.js — Ternurines
 * Carga datos del usuario y sus mascotas en el dashboard
 */

(function () {
    'use strict';

    let currentUser = null;
    let userPets = [];

    /**
     * Verificar si hay sesión activa
     */
    async function checkSession() {
        try {
            const response = await fetch('/auth/usuario-actual');
            if (!response.ok) {
                // No hay sesión, redirigir a login
                window.location.href = '/login.html';
                return false;
            }
            return true;
        } catch (error) {
            console.error('Error verificando sesión:', error);
            window.location.href = '/login.html';
            return false;
        }
    }

    /**
     * Cargar datos del usuario actual
     */
    async function loadUserData() {
        try {
            const response = await fetch('/auth/usuario-actual');
            if (!response.ok) {
                throw new Error('No se pudo obtener los datos del usuario');
            }

            currentUser = await response.json();

            // Obtener datos completos del cliente
            const clientResponse = await fetch(`/clientes/${currentUser.userId}`);
            if (clientResponse.ok) {
                const clientData = await clientResponse.json();
                currentUser.details = clientData;
            }

            renderUserData();
        } catch (error) {
            console.error('Error cargando datos del usuario:', error);
            alert('Error al cargar los datos del usuario');
        }
    }

    /**
     * Cargar mascotas del usuario
     */
    async function loadUserPets() {
        try {
            const response = await fetch(`/mascotas/cliente/${currentUser.userId}`);
            if (!response.ok) {
                console.warn('No se encontraron mascotas');
                userPets = [];
                return;
            }

            userPets = await response.json();
            renderPets();
        } catch (error) {
            console.error('Error cargando mascotas:', error);
            userPets = [];
            renderPets();
        }
    }

    /**
     * Renderizar datos del usuario en el dashboard
     */
    function renderUserData() {
        const details = currentUser.details;

        // Avatar con iniciales
        const initials = details.nombre
            .split(' ')
            .map(part => part[0])
            .join('')
            .toUpperCase()
            .substring(0, 2);

        // Actualizar welcome card
        document.querySelector('.eyebrow').innerHTML = '<i class="fas fa-heart"></i> Bienvenido a casa';
        document.querySelector('.welcome-card h1').innerHTML = `Hola, <span>${details.nombre.split(' ')[0]}</span>.`;

        // Quick grid
        const petName = userPets.length > 0 ? userPets[0].nombre : 'Sin mascota';
        document.querySelectorAll('.quick-item')[0].innerHTML = `
            <small>Cliente</small>
            <strong>${details.nombre}</strong>
        `;
        document.querySelectorAll('.quick-item')[1].innerHTML = `
            <small>Documento</small>
            <strong>${details.documento}</strong>
        `;
        document.querySelectorAll('.quick-item')[2].innerHTML = `
            <small>Mascota activa</small>
            <strong>${petName}</strong>
        `;

        // Profile card
        document.querySelector('.avatar').textContent = initials;
        document.querySelector('.profile-top h2').textContent = 'Tu perfil';

        document.querySelectorAll('.data-row')[0].innerHTML = `
            <span>Correo</span>
            <strong>${details.correo}</strong>
        `;
        document.querySelectorAll('.data-row')[1].innerHTML = `
            <span>Teléfono</span>
            <strong>${details.telefono}</strong>
        `;
        document.querySelectorAll('.data-row')[2].innerHTML = `
            <span>Dirección</span>
            <strong>${details.direccion}</strong>
        `;
        document.querySelectorAll('.data-row')[3].innerHTML = `
            <span>Estado de cuenta</span>
            <strong>Usuario activo</strong>
        `;
    }

    /**
     * Renderizar mascotas del usuario
     */
    function renderPets() {
        const petsContainer = document.querySelector('.pets-stack');

        if (userPets.length === 0) {
            petsContainer.innerHTML = `
                <div style="padding: 24px; text-align: center; color: var(--text-light);">
                    <i class="fas fa-paw" style="font-size: 2rem; margin-bottom: 12px; display: block;"></i>
                    <p>No tienes mascotas registradas aún.</p>
                </div>
            `;
            return;
        }

        petsContainer.innerHTML = userPets.map(pet => `
            <article class="pet-card">
                <div class="pet-top">
                    <div class="pet-name">
                        <div class="pet-badge">
                            ${pet.especie && pet.especie.toLowerCase() === 'gato' ? '<i class="fas fa-cat"></i>' : '<i class="fas fa-dog"></i>'}
                        </div>
                        <div>
                            <h4>${pet.nombre}</h4>
                            <p>${pet.especie} • ${pet.raza}</p>
                        </div>
                    </div>
                    <div class="status">Activo</div>
                </div>

                <div class="pet-info">
                    <div>
                        <span>Edad</span>
                        <strong>${pet.edad} años</strong>
                    </div>
                    <div>
                        <span>Peso</span>
                        <strong>${pet.peso} kg</strong>
                    </div>
                    <div>
                        <span>Sexo</span>
                        <strong>${pet.sexo}</strong>
                    </div>
                    <div>
                        <span>ID Mascota</span>
                        <strong>${pet.idMascota}</strong>
                    </div>
                </div>
            </article>
        `).join('');
    }

    /**
     * Logout
     */
    async function logout() {
        try {
            await fetch('/auth/logout', { method: 'POST' });
            localStorage.removeItem('userId');
            localStorage.removeItem('userType');
            localStorage.removeItem('userName');
            window.location.href = '/login.html';
        } catch (error) {
            console.error('Error en logout:', error);
        }
    }

    /**
     * Inicialización
     */
    document.addEventListener('DOMContentLoaded', async function () {
        // Verificar sesión
        if (!(await checkSession())) {
            return;
        }

        // Cargar datos
        await loadUserData();
        await loadUserPets();

        // Exponer logout globalmente
        window.logout = logout;

        // Agregar botón de logout en navbar si no existe
        const navbar = document.querySelector('.navbar-recepcion');
        if (navbar && !document.getElementById('logoutBtn')) {
            const logoutBtn = document.createElement('button');
            logoutBtn.id = 'logoutBtn';
            logoutBtn.style.cssText = `
                border: none;
                background: transparent;
                color: rgba(255,255,255,0.75);
                cursor: pointer;
                display: flex;
                align-items: center;
                gap: 7px;
                font-size: 0.86rem;
                font-weight: 600;
                transition: color .2s;
            `;
            logoutBtn.innerHTML = '<i class="fas fa-sign-out-alt"></i> Cerrar sesión';
            logoutBtn.addEventListener('click', logout);
            logoutBtn.onmouseover = () => logoutBtn.style.color = '#fac3a5';
            logoutBtn.onmouseout = () => logoutBtn.style.color = 'rgba(255,255,255,0.75)';
            navbar.parentNode.replaceChild(logoutBtn, navbar);
        }
    });

})();
