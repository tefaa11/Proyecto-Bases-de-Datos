package com.ternurines.controller;

import com.ternurines.model.Veterinario;
import com.ternurines.repository.VeterinarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veterinarios")
public class VeterinarioController {

    private final VeterinarioRepository veterinarioRepository;

    public VeterinarioController(VeterinarioRepository veterinarioRepository) {
        this.veterinarioRepository = veterinarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Veterinario>> findAll() {
        return ResponseEntity.ok(veterinarioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veterinario> findById(@PathVariable int id) {
        return veterinarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Veterinario> findByCorreo(@PathVariable String correo) {
        return veterinarioRepository.findByCorreo(correo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Veterinario> save(@RequestBody Veterinario veterinario) {
        veterinarioRepository.save(veterinario);
        return ResponseEntity.status(201).body(veterinario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veterinario> update(
            @PathVariable int id,
            @RequestBody Veterinario veterinario) {

        if (veterinarioRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        veterinario.setIdVeterinario(id);
        veterinarioRepository.update(veterinario);
        return ResponseEntity.ok(veterinario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (veterinarioRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        veterinarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
