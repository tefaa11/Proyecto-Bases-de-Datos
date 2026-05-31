package com.ternurines.controller;

import com.ternurines.model.Administrador;
import com.ternurines.repository.AdministradorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    private final AdministradorRepository administradorRepository;

    public AdministradorController(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @GetMapping
    public ResponseEntity<List<Administrador>> findAll() {
        return ResponseEntity.ok(administradorRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrador> findById(@PathVariable int id) {
        return administradorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Administrador> save(@RequestBody Administrador administrador) {
        administradorRepository.save(administrador);
        return ResponseEntity.status(201).body(administrador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Administrador> update(
            @PathVariable int id,
            @RequestBody Administrador administrador) {

        if (administradorRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        administrador.setIdAdministrador(id);
        administradorRepository.update(administrador);
        return ResponseEntity.ok(administrador);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (administradorRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        administradorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
