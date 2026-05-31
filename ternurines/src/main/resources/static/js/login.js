/**
 * login.js — Ternurines
 * Gestiona el inicio de sesión para clientes y veterinarios
 */

(function () {
    'use strict';

    let rolActual = 'cliente';

    function seleccionarRol(element, rol) {
        // Actualizar botones de rol
        document.querySelectorAll('.rol-tab').forEach(btn => {
            btn.classList.remove('active');
            btn.setAttribute('aria-pressed', 'false');
        });
        element.classList.add('active');
        element.setAttribute('aria-pressed', 'true');

        rolActual = rol;
        document.getElementById('rolSeleccionado').value = rol;

        // Cambiar label y placeholder según el rol
        const tituloRol = document.querySelector('.form-header h1');
        if (tituloRol) {
            if (rol === 'cliente') {
                tituloRol.textContent = 'Cliente - Iniciar sesión';
            } else if (rol === 'veterinario') {
                tituloRol.textContent = 'Veterinario - Iniciar sesión';
            }
        }
    }

    function togglePassword() {
        const input = document.getElementById('contrasena');
        const icon = document.getElementById('eyeIcon');
        
        if (input.type === 'password') {
            input.type = 'text';
            icon.classList.remove('fa-eye');
            icon.classList.add('fa-eye-slash');
        } else {
            input.type = 'password';
            icon.classList.remove('fa-eye-slash');
            icon.classList.add('fa-eye');
        }
    }

    async function handleLogin(e) {
        e.preventDefault();

        const correo = document.getElementById('correo').value.trim();
        const contrasena = document.getElementById('contrasena').value;

        if (!correo || !contrasena) {
            alert('Por favor completa todos los campos.');
            return;
        }

        try {
            const endpoint = rolActual === 'cliente' 
                ? '/auth/login-cliente' 
                : '/auth/login-veterinario';

            const params = new URLSearchParams();
            params.append('correo', correo);
            params.append('contrasena', contrasena);

            const response = await fetch(endpoint, {
                method: 'POST',
                body: params,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

            const data = await response.json();

            if (!response.ok) {
                alert('Error: ' + (data.error || 'No se pudo iniciar sesión'));
                return;
            }

            // Login exitoso
            if (data.success) {
                // Guardar info en localStorage (opcional)
                localStorage.setItem('userId', data.userId);
                localStorage.setItem('userType', data.userType);
                localStorage.setItem('userName', data.userName);

                // Redirigir según el rol
                if (data.userType === 'cliente') {
                    window.location.href = '/usuario.html';
                } else if (data.userType === 'veterinario') {
                    window.location.href = '/veterinario.html';
                }
            }
        } catch (error) {
            console.error('Error en login:', error);
            alert('Hubo un error al procesar tu solicitud. Intenta nuevamente.');
        }
    }

    // Inicialización
    document.addEventListener('DOMContentLoaded', function () {
        // Exponer funciones globales para los botones onclick del HTML
        window.seleccionarRol = seleccionarRol;
        window.togglePassword = togglePassword;

        // Asignar manejador al formulario
        const btnLogin = document.querySelector('.btn-login');
        if (btnLogin) {
            btnLogin.addEventListener('click', handleLogin);
        }

        // También permitir Enter en el campo de contraseña
        const inputContrasena = document.getElementById('contrasena');
        if (inputContrasena) {
            inputContrasena.addEventListener('keypress', function (e) {
                if (e.key === 'Enter') {
                    handleLogin(e);
                }
            });
        }
    });

})();
