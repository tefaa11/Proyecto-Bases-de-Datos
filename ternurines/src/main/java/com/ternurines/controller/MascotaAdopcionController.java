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
    public List<MascotaAdopcion> findAll() {
        return mascotaAdopcionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaAdopcion> findById(@PathVariable int id) {
        return mascotaAdopcionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody MascotaAdopcion mascotaAdopcion) {
        int rows = mascotaAdopcionRepository.save(mascotaAdopcion);
        return ResponseEntity.ok("Filas insertadas: " + rows);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody MascotaAdopcion mascotaAdopcion) {
        mascotaAdopcion.setIdMascotaAdopcion(id);
        int rows = mascotaAdopcionRepository.update(mascotaAdopcion);
        return ResponseEntity.ok("Filas actualizadas: " + rows);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        int rows = mascotaAdopcionRepository.delete(id);
        return ResponseEntity.ok("Filas eliminadas: " + rows);
    }
}