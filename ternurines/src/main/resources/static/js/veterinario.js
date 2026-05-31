/**
 * veterinario.js — Ternurines
 * Carga datos del veterinario y sus citas del día en el dashboard
 */

(function () {
    'use strict';

    let currentVeterinario = null;
    let citasHoy = [];
    let mascotasPorId = new Map();

    /**
     * Verificar si hay sesión activa
     */
    async function checkSession() {
        try {
            const response = await fetch('/auth/usuario-actual');
            if (!response.ok) {
                window.location.href = '/login.html';
                return false;
            }
            const data = await response.json();
            if (data.userType !== 'veterinario') {
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
     * Cargar datos del veterinario actual
     */
    async function loadVeterinarioData() {
        try {
            const response = await fetch('/auth/usuario-actual');
            if (!response.ok) {
                throw new Error('No se pudo obtener los datos del veterinario');
            }

            const sessionData = await response.json();
            currentVeterinario = sessionData;

            // Obtener datos completos del veterinario
            const vetResponse = await fetch(`/veterinarios/${sessionData.userId}`);
            if (vetResponse.ok) {
                const vetData = await vetResponse.json();
                currentVeterinario.details = vetData;
            }

            renderVeterinarioData();
        } catch (error) {
            console.error('Error cargando datos del veterinario:', error);
            alert('Error al cargar los datos del veterinario');
        }
    }

    /**
     * Cargar citas del veterinario para hoy
     */
    async function loadCitasHoy() {
        try {
            // Obtener todas las citas del veterinario
            const response = await fetch(`/citas/veterinario/${currentVeterinario.userId}`);
            if (!response.ok) {
                console.warn('No se encontraron citas');
                citasHoy = [];
                return;
            }

            const todasLasCitas = await response.json();

            // Filtrar citas de hoy
            const hoy = new Date().toISOString().split('T')[0];
            citasHoy = todasLasCitas.filter(cita => {
                return cita.fecha === hoy;
            });

            await cargarMascotasDeCitas(citasHoy);
            renderCitasConectadas();
            renderVeterinarioData();
        } catch (error) {
            console.error('Error cargando citas:', error);
            citasHoy = [];
            renderCitasConectadas();
            renderVeterinarioData();
        }
    }

    async function cargarMascotasDeCitas(citas) {
        mascotasPorId = new Map();
        await Promise.all(citas.map(async cita => {
            try {
                const response = await fetch(`/mascotas/${cita.idMascota}`);
                if (response.ok) {
                    mascotasPorId.set(cita.idMascota, await response.json());
                }
            } catch (error) {
                console.warn('No se pudo cargar mascota', cita.idMascota, error);
            }
        }));
    }

    /**
     * Renderizar datos del veterinario
     */
    function renderVeterinarioData() {
        const details = currentVeterinario.details;

        // Avatar con iniciales
        const initials = details.nombre
            .split(' ')
            .map(part => part[0])
            .join('')
            .toUpperCase()
            .substring(0, 2);

        // Actualizar welcome card
        document.querySelector('.eyebrow').innerHTML = '<i class="fas fa-stethoscope"></i> Panel veterinario';
        document.querySelector('.welcome-card h1').innerHTML = `Bienvenido, <span>${details.nombre.split(' ')[0]}</span>.`;

        // Quick grid
        document.querySelectorAll('.quick-item')[0].innerHTML = `
            <small>Citas hoy</small>
            <strong>${citasHoy.length} agendadas</strong>
        `;
        document.querySelectorAll('.quick-item')[1].innerHTML = `
            <small>Especialidad</small>
            <strong>${details.especialidad || 'No especificada'}</strong>
        `;
        document.querySelectorAll('.quick-item')[2].innerHTML = `
            <small>Próxima cita</small>
            <strong>${citasHoy.length > 0 ? (citasHoy[0].hora || 'N/A') : 'Sin citas'}</strong>
        `;

        // Profile card
        document.querySelector('.avatar').textContent = initials;
        document.querySelector('.profile-top h2').textContent = 'Tu perfil';

        document.querySelectorAll('.data-row')[0].innerHTML = `
            <span>Nombre</span>
            <strong>${details.nombre}</strong>
        `;
        document.querySelectorAll('.data-row')[1].innerHTML = `
            <span>Tarjeta profesional</span>
            <strong>${details.numLicencia}</strong>
        `;
        document.querySelectorAll('.data-row')[2].innerHTML = `
            <span>Correo</span>
            <strong>${details.correo}</strong>
        `;
        document.querySelectorAll('.data-row')[3].innerHTML = `
            <span>Teléfono</span>
            <strong>${details.telefono}</strong>
        `;
        document.querySelectorAll('.data-row')[4].innerHTML = `
            <span>Estado</span>
            <strong>Activo · En línea</strong>
        `;
    }

    /**
     * Renderizar citas del día
     */
    function renderCitas() {
        const citasContainer = document.querySelector('.citas-list');
        const hoy = new Date();
        const fechaActual = hoy.toLocaleDateString('es-ES', { 
            weekday: 'long', 
            year: 'numeric', 
            month: 'long', 
            day: 'numeric' 
        });

        // Actualizar fecha en el header
        const sectionHead = document.querySelector('.section-head p');
        if (sectionHead) {
            sectionHead.textContent = fechaActual;
        }

        if (citasHoy.length === 0) {
            citasContainer.innerHTML = `
                <div style="padding: 36px; text-align: center; color: var(--text-light);">
                    <i class="fas fa-calendar-times" style="font-size: 2.5rem; margin-bottom: 16px; display: block;"></i>
                    <p style="font-size: 1rem;">No tienes citas agendadas para hoy.</p>
                </div>
            `;
            return;
        }

        // Ordenar citas por hora
        citasHoy.sort((a, b) => (a.hora || '').localeCompare(b.hora || ''));

        citasContainer.innerHTML = citasHoy.map(cita => {
            const tipoTag = getTagByCita(cita.motivo);
            const estado = getEstadoByCita(cita.estado);
            const [hora, minuto] = (cita.hora || '09:00').split(':');
            const ampm = parseInt(hora) >= 12 ? 'PM' : 'AM';

            return `
                <article class="cita-item">
                    <div class="cita-hora-box" style="background: linear-gradient(135deg, var(--terracota), #d26d56);">
                        <span class="hora">${hora}:${minuto}</span>
                        <span class="ampm">${ampm}</span>
                    </div>
                    <div class="cita-info">
                        <h4>${cita.mascota?.nombre || 'Mascota'} — ${cita.mascota?.raza || 'Raza desconocida'}</h4>
                        <p>Dueño: ${cita.mascota?.cliente?.nombre || 'Cliente desconocido'}</p>
                        <div class="cita-meta">
                            <span class="tag ${tipoTag.class}">${tipoTag.text}</span>
                        </div>
                    </div>
                    <span class="estado-badge ${estado.class}">${estado.text}</span>
                </article>
            `;
        }).join('');
    }

    /**
     * Obtener clase y texto del tag según tipo de cita
     */
    function getTagByCita(motivo) {
        if (!motivo) return { class: 'tag-consulta', text: 'Consulta general' };

        const motivoLower = motivo.toLowerCase();
        if (motivoLower.includes('vacun')) return { class: 'tag-vacuna', text: 'Vacunación' };
        if (motivoLower.includes('cirug')) return { class: 'tag-cirugia', text: 'Cirugía' };
        if (motivoLower.includes('urgenc')) return { class: 'tag-urgencia', text: 'Urgencia' };
        if (motivoLower.includes('control')) return { class: 'tag-control', text: 'Control' };
        return { class: 'tag-consulta', text: 'Consulta general' };
    }

    function renderCitasConectadas() {
        const citasContainer = document.querySelector('.citas-list');
        if (!citasContainer) return;

        const hoy = new Date();
        const fechaActual = hoy.toLocaleDateString('es-ES', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });

        const sectionHead = document.querySelector('.section-head p');
        if (sectionHead) {
            sectionHead.textContent = fechaActual;
        }

        if (citasHoy.length === 0) {
            citasContainer.innerHTML = `
                <div style="padding: 36px; text-align: center; color: var(--text-light);">
                    <i class="fas fa-calendar-times" style="font-size: 2.5rem; margin-bottom: 16px; display: block;"></i>
                    <p style="font-size: 1rem;">No tienes citas agendadas para hoy.</p>
                </div>
            `;
            return;
        }

        citasHoy.sort((a, b) => (a.hora || '').localeCompare(b.hora || ''));

        citasContainer.innerHTML = citasHoy.map(cita => {
            const mascota = mascotasPorId.get(cita.idMascota);
            const tipoTag = getTagByCita(cita.motivo);
            const estado = getEstadoByCita(cita.estado);
            const [hora, minuto] = (cita.hora || '09:00').split(':');
            const ampm = parseInt(hora, 10) >= 12 ? 'PM' : 'AM';

            return `
                <article class="cita-item" data-cita-id="${cita.idCita}">
                    <div class="cita-hora-box" style="background: linear-gradient(135deg, var(--terracota), #d26d56);">
                        <span class="hora">${hora}:${minuto}</span>
                        <span class="ampm">${ampm}</span>
                    </div>
                    <div class="cita-info">
                        <h4>${mascota?.nombre || 'Mascota'} - ${mascota?.raza || 'Raza desconocida'}</h4>
                        <p>Dueño: ${mascota?.cliente?.nombre || 'Cliente desconocido'}</p>
                        <div class="cita-meta">
                            <span class="tag ${tipoTag.class}">${tipoTag.text}</span>
                            <a class="tag tag-control" href="/rg_cita.html?cita=${cita.idCita}">Registrar atención</a>
                        </div>
                    </div>
                    <span class="estado-badge ${estado.class}">${estado.text}</span>
                </article>
            `;
        }).join('');
    }

    /**
     * Obtener clase y texto del estado de cita
     */
    function getEstadoByCita(estado) {
        if (!estado) return { class: 'estado-confirmada', text: 'Confirmada' };

        const estadoLower = estado.toLowerCase();
        if (estadoLower.includes('urgente')) return { class: 'estado-urgente', text: 'Urgente' };
        if (estadoLower.includes('pendiente')) return { class: 'estado-pendiente', text: 'Pendiente' };
        return { class: 'estado-confirmada', text: 'Confirmada' };
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
        await loadVeterinarioData();
        await loadCitasHoy();

        // Exponer logout globalmente
        window.logout = logout;

        // Agregar botón de logout en navbar si no existe
        const navbar = document.querySelector('.navbar-role');
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
                margin-left: 20px;
            `;
            logoutBtn.innerHTML = '<i class="fas fa-sign-out-alt"></i> Cerrar sesión';
            logoutBtn.addEventListener('click', logout);
            logoutBtn.onmouseover = () => logoutBtn.style.color = '#fac3a5';
            logoutBtn.onmouseout = () => logoutBtn.style.color = 'rgba(255,255,255,0.75)';
            navbar.appendChild(logoutBtn);
        }
    });

})();
