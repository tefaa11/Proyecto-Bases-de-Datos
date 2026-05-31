package com.ternurines.controller;

import com.ternurines.model.MascotaAdopcion;
import com.ternurines.repository.MascotaAdopcionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mascotas-adopcion")
public class MascotaAdopcionController {

    private final MascotaAdopcionRepository mascotaAdopcionRepository;

    public MascotaAdopcionController(MascotaAdopcionRepository mascotaAdopcionRepository) {
        this.mascotaAdopcionRepository = mascotaAdopcionRepository;
    }

    @GetMapping
    public ResponseEntity<List<MascotaAdopcion>> findAll() {
        return ResponseEntity.ok(mascotaAdopcionRepository.findAll());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<MascotaAdopcion>> findDisponibles() {
        return ResponseEntity.ok(mascotaAdopcionRepository.findDisponibles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaAdopcion> findById(@PathVariable int id) {

        return mascotaAdopcionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MascotaAdopcion> save(
            @RequestBody MascotaAdopcion mascotaAdopcion) {

        mascotaAdopcionRepository.save(mascotaAdopcion);

        return ResponseEntity.status(201).body(mascotaAdopcion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaAdopcion> update(
            @PathVariable int id,
            @RequestBody MascotaAdopcion mascotaAdopcion) {

        mascotaAdopcion.setIdMascotaAdopcion(id);

        int rows = mascotaAdopcionRepository.update(mascotaAdopcion);

        if (rows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mascotaAdopcion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {

        int rows = mascotaAdopcionRepository.delete(id);

        if (rows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}