/**
 * ag_cita_usuario.js — Ternurines
 * Gestiona el formulario de agendar citas
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
                window.location.href = '/login.html';
                return false;
            }
            const data = await response.json();
            if (data.userType !== 'cliente') {
                window.location.href = '/login.html';
                return false;
            }
            currentUser = data;
            return true;
        } catch (error) {
            console.error('Error verificando sesión:', error);
            window.location.href = '/login.html';
            return false;
        }
    }

    /**
     * Cargar mascotas del usuario
     */
    async function loadMascotas() {
        try {
            const response = await fetch(`/mascotas/cliente/${currentUser.userId}`);
            if (!response.ok) {
                console.warn('No se encontraron mascotas');
                return;
            }

            mascotasDisponibles = await response.json();
            renderMascotasSelect();
        } catch (error) {
            console.error('Error cargando mascotas:', error);
        }
    }

    /**
     * Renderizar select de mascotas
     */
    function renderMascotasSelect() {
        const select = document.getElementById('mascota');
        select.innerHTML = '';

        if (mascotasDisponibles.length === 0) {
            select.innerHTML = '<option>No tienes mascotas registradas</option>';
            select.disabled = true;
            return;
        }

        mascotasDisponibles.forEach(mascota => {
            const option = document.createElement('option');
            option.value = mascota.idMascota;
            option.textContent = `${mascota.nombre} (${mascota.especie})`;
            select.appendChild(option);
        });
    }

    /**
     * Obtener el tipo de servicio seleccionado
     */
    function getServicioSeleccionado() {
        const servicios = document.querySelectorAll('input[name="servicio"]');
        for (let servicio of servicios) {
            if (servicio.checked) {
                return servicio.parentElement.querySelector('strong').textContent;
            }
        }
        return 'Consulta general';
    }

    function parseAppointmentTime(label) {
        if (!label) {
            return '';
        }

        const normalized = label.toLowerCase().replace(/\s+/g, '');
        const time = normalized.substring(0, 5);

        if (normalized.includes('p.m.') || normalized.includes('pm')) {
            let [hour, minute] = time.split(':');
            const hourInt = parseInt(hour, 10);
            if (hourInt < 12) {
                hour = String(hourInt + 12).padStart(2, '0');
            }
            return `${hour}:${minute}:00`;
        }

        if (normalized.includes('a.m.') || normalized.includes('am')) {
            let [hour, minute] = time.split(':');
            if (hour === '12') {
                hour = '00';
            }
            return `${hour}:${minute}:00`;
        }

        return normalized.length === 5 ? `${time}:00` : label;
    }

    /**
     * Validar formulario
     */
    function validarFormulario() {
        const mascota = document.getElementById('mascota').value;
        const fecha = document.getElementById('fecha').value;
        const hora = document.getElementById('hora').value;
        const motivo = document.getElementById('motivo').value.trim();

        if (!mascota) {
            alert('Por favor selecciona una mascota.');
            return false;
        }

        if (!fecha) {
            alert('Por favor selecciona una fecha.');
            return false;
        }

        if (!hora) {
            alert('Por favor selecciona una hora.');
            return false;
        }

        // Validar que la fecha no sea en el pasado
        const fechaSeleccionada = new Date(fecha);
        const hoy = new Date();
        hoy.setHours(0, 0, 0, 0);

        if (fechaSeleccionada < hoy) {
            alert('Por favor selecciona una fecha futura.');
            return false;
        }

        return true;
    }

    /**
     * Enviar cita al backend
     */
    async function enviarCita(e) {
        e.preventDefault();

        if (!validarFormulario()) {
            return;
        }

        const btn = e.target;
        btn.disabled = true;
        btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Procesando...';

        try {
            const mascotaId = parseInt(document.getElementById('mascota').value, 10);
            const fecha = document.getElementById('fecha').value;
            const hora = parseAppointmentTime(document.getElementById('hora').value);
            const motivo = document.getElementById('motivo').value.trim();
            const servicio = getServicioSeleccionado();

            // Buscar un veterinario (por ahora, el primero disponible)
            const vetResponse = await fetch('/veterinarios');
            if (!vetResponse.ok) {
                throw new Error('No se pudieron cargar veterinarios');
            }

            const veterinarios = await vetResponse.json();
            if (veterinarios.length === 0) {
                throw new Error('No hay veterinarios disponibles');
            }

            const veterinarioId = veterinarios[0].idVeterinario;

            const cita = {
                idMascota: mascotaId,
                idVeterinario: veterinarioId,
                idRecepcionista: 1,
                fecha: fecha,
                hora: hora,
                motivo: motivo || servicio,
                estado: 'Pendiente'
            };

            // Enviar al backend
            const response = await fetch('/citas', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(cita)
            });

            if (!response.ok) {
                throw new Error('Error al crear la cita');
            }

            const citaCreada = await response.json();

            // Éxito
            alert('¡Cita agendada exitosamente!\nFecha: ' + fecha + '\nHora: ' + hora);
            
            // Limpiar formulario
            document.getElementById('fecha').value = '';
            document.getElementById('hora').value = document.getElementById('hora').options[0].value;
            document.getElementById('motivo').value = '';

            // Redirigir al dashboard después de 2 segundos
            setTimeout(() => {
                window.location.href = '/usuario.html';
            }, 2000);

        } catch (error) {
            console.error('Error:', error);
            alert('Error al agendar la cita: ' + error.message);
        } finally {
            btn.disabled = false;
            btn.innerHTML = '<i class="fas fa-calendar-plus"></i> Confirmar cita';
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

        // Cargar mascotas
        await loadMascotas();

        // Asignar manejador al botón
        const btnAgendar = document.querySelector('.btn-login');
        if (btnAgendar) {
            btnAgendar.addEventListener('click', enviarCita);
        }

        // Establecer fecha mínima en el input date (hoy)
        const inputFecha = document.getElementById('fecha');
        if (inputFecha) {
            const hoy = new Date();
            const fechaMin = hoy.toISOString().split('T')[0];
            inputFecha.min = fechaMin;
        }
    });

})();
