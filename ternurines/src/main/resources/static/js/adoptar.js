/**
 * adoptar.js — Ternurines
 * Gestiona la adopción de mascotas
 */

(function () {
    'use strict';

    let currentUser = null;
    let mascotasDisponibles = [];

    /**
     * Verificar sesión
     */
    async function checkSession() {
        try {
            const response = await fetch('/auth/usuario-actual');
            if (!response.ok) {
                console.warn('No hay sesión, permitiendo acceso de lectura');
                return false;
            }
            const data = await response.json();
            if (data.userType !== 'cliente') {
                return false;
            }
            currentUser = data;
            return true;
        } catch (error) {
            console.error('Error verificando sesión:', error);
            return false;
        }
    }

    /**
     * Cargar mascotas disponibles para adopción
     */
    async function loadAdoptionPets() {
        try {
            const response = await fetch('/mascotas-adopcion/disponibles');
            if (!response.ok) {
                throw new Error('No se encontró la lista de mascotas en adopción.');
            }

            mascotasDisponibles = await response.json();
            renderPets();
        } catch (error) {
            console.error('Error:', error);
            const grid = document.getElementById('pets-grid');
            if (grid) {
                grid.innerHTML = '<div class="pets-loading">Error al cargar las mascotas. Inténtalo de nuevo más tarde.</div>';
            }
        }
    }

    /**
     * Crear tarjeta de mascota
     */
    function createPetCard(pet) {
        const status = pet.estadoAdopcion || (pet.disponible ? 'Disponible' : 'No disponible');
        const badgeClass = status.toLowerCase().includes('disponible') ? 'available' : 'process';
        const fechaIngreso = pet.fechaIngreso || pet.ingreso || 'Sin fecha';
        const petId = pet.idMascotaAdopcion || pet.idMascota || pet.id || '';

        return `
            <article class="pet-card">
                <img src="/img/mascota${petId}.jpg" alt="${pet.nombre}" class="pet-img" onerror="this.style.display='none';this.nextElementSibling.style.display='flex';">
                <div class="pet-placeholder">
                    <i class="fas fa-image"></i>
                    <span>Imagen no disponible<br>${pet.nombre}</span>
                </div>
                <div class="pet-body">
                    <div class="pet-top">
                        <h3 class="pet-name">${pet.nombre}</h3>
                        <span class="pet-badge ${badgeClass}">${status}</span>
                    </div>
                    <p class="pet-desc">${pet.descripcion || 'Mascota lista para adopción.'}</p>
                    <div class="pet-meta">
                        <div class="meta-box">
                            <small>Especie</small>
                            <strong>${pet.especie || pet.tipo || 'N/A'}</strong>
                        </div>
                        <div class="meta-box">
                            <small>Raza</small>
                            <strong>${pet.raza || 'N/A'}</strong>
                        </div>
                        <div class="meta-box">
                            <small>Edad</small>
                            <strong>${pet.edad} años</strong>
                        </div>
                        <div class="meta-box">
                            <small>Ingreso</small>
                            <strong>${fechaIngreso}</strong>
                        </div>
                    </div>
                    <div class="pet-footer">
                        <span class="pet-health">Salud: ${pet.estadoSalud || 'Desconocido'}</span>
                        <button class="adopt-btn" type="button" data-id="${pet.idMascotaAdopcion}" onclick="adoptarMascota(event)">Adoptar</button>
                    </div>
                </div>
            </article>
        `;
    }

    /**
     * Renderizar mascotas en el grid
     */
    function renderPets() {
        const grid = document.getElementById('pets-grid');
        if (!grid) return;

        if (!Array.isArray(mascotasDisponibles) || mascotasDisponibles.length === 0) {
            grid.innerHTML = '<div class="pets-loading">No hay mascotas disponibles para adopción en este momento.</div>';
            return;
        }

        grid.innerHTML = mascotasDisponibles.map(createPetCard).join('');
    }

    /**
     * Manejar click en botón adoptar
     */
    async function adoptarMascota(event) {
        event.preventDefault();

        if (!currentUser) {
            alert('Debes iniciar sesión para adoptar una mascota.');
            window.location.href = '/login.html';
            return;
        }

        const btn = event.target;
        const mascotaAdopcionId = parseInt(btn.getAttribute('data-id'), 10);

        btn.disabled = true;
        btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Procesando...';

        try {
            const adopcion = {
                idCliente: currentUser.userId,
                idMascotaAdopcion: mascotaAdopcionId
            };

            const response = await fetch('/adopciones', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(adopcion)
            });

            if (!response.ok) {
                throw new Error('Error al registrar la adopción');
            }

            alert('¡Felicidades! Tu solicitud de adopción ha sido registrada. Nos pondremos en contacto contigo pronto.');

            // Recargar la página para actualizar el estado
            setTimeout(() => {
                location.reload();
            }, 2000);

        } catch (error) {
            console.error('Error:', error);
            alert('Error al registrar la adopción: ' + error.message);
        } finally {
            btn.disabled = false;
            btn.innerHTML = 'Adoptar';
        }
    }

    /**
     * Inicialización
     */
    document.addEventListener('DOMContentLoaded', async function () {
        // Verificar sesión (no obligatorio para ver mascotas)
        await checkSession();

        // Cargar mascotas
        await loadAdoptionPets();

        // Exponer función globalmente
        window.adoptarMascota = adoptarMascota;
    });

})();
