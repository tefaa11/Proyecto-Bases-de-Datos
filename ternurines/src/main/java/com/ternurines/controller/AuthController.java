package com.ternurines.controller;

import com.ternurines.model.Cliente;
import com.ternurines.model.Veterinario;
import com.ternurines.repository.ClienteRepository;
import com.ternurines.repository.VeterinarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ClienteRepository clienteRepository;
    private final VeterinarioRepository veterinarioRepository;

    public AuthController(ClienteRepository clienteRepository, VeterinarioRepository veterinarioRepository) {
        this.clienteRepository = clienteRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    /**
     * Login para Cliente
     */
    @PostMapping("/login-cliente")
    public ResponseEntity<?> loginCliente(
            @RequestParam String correo,
            @RequestParam(name = "contrasena", required = false) String contrasena,
            @RequestParam(name = "contrasenia", required = false) String contrasenia,
            HttpSession session) {
        String password = contrasena != null ? contrasena : contrasenia;
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Contraseña requerida"));
        }
        
        var cliente = clienteRepository.findByCorreo(correo);
        
        if (cliente.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Correo no encontrado"));
        }
        
        Cliente c = cliente.get();
        if (!c.getContrasenia().equals(password)) {
            return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));
        }
        
        // Guardar en sesión
        session.setAttribute("userId", c.getIdCliente());
        session.setAttribute("userType", "cliente");
        session.setAttribute("userName", c.getNombre());
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "userId", c.getIdCliente(),
            "userType", "cliente",
            "userName", c.getNombre()
        ));
    }

    /**
     * Login para Veterinario
     */
    @PostMapping("/login-veterinario")
    public ResponseEntity<?> loginVeterinario(
            @RequestParam String correo,
            @RequestParam String contrasena,
            HttpSession session) {
        
        var veterinario = veterinarioRepository.findByCorreo(correo);
        
        if (veterinario.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Correo no encontrado"));
        }
        
        Veterinario v = veterinario.get();
        if (!v.getContrasena().equals(contrasena)) {
            return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));
        }
        
        // Guardar en sesión
        session.setAttribute("userId", v.getIdVeterinario());
        session.setAttribute("userType", "veterinario");
        session.setAttribute("userName", v.getNombre());
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "userId", v.getIdVeterinario(),
            "userType", "veterinario",
            "userName", v.getNombre()
        ));
    }

    /**
     * Obtener usuario actual de la sesión
     */
    @GetMapping("/usuario-actual")
    public ResponseEntity<?> usuarioActual(HttpSession session) {
        Object userId = session.getAttribute("userId");
        Object userType = session.getAttribute("userType");
        Object userName = session.getAttribute("userName");
        
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No hay sesión activa"));
        }
        
        return ResponseEntity.ok(Map.of(
            "userId", userId,
            "userType", userType,
            "userName", userName
        ));
    }

    /**
     * Logout
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("success", true, "message", "Sesión cerrada"));
    }
}
