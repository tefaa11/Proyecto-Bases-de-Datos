package com.ternurines.controller;

import com.ternurines.model.Adopcion;
import com.ternurines.repository.AdopcionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adopciones")
public class AdopcionController {

    private final AdopcionRepository adopcionRepository;

    public AdopcionController(AdopcionRepository adopcionRepository) {
        this.adopcionRepository = adopcionRepository;
    }

    // GET LISTA
    @GetMapping
    public ResponseEntity<List<Adopcion>> findAll() {
        return ResponseEntity.ok(adopcionRepository.findAll());
    }

    // GET POR ID
    @GetMapping("/{idAdoptante}/{idMascotaAdopcion}")
    public ResponseEntity<Adopcion> findById(
            @PathVariable int idAdoptante,
            @PathVariable int idMascotaAdopcion) {

        return adopcionRepository.findById(idAdoptante, idMascotaAdopcion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST -> 201 CREATED
    @PostMapping
    public ResponseEntity<Adopcion> save(@RequestBody Adopcion adopcion) {

        adopcionRepository.save(adopcion);

        return ResponseEntity.status(201).body(adopcion);
    }

    // PUT -> 200 OK o 404
    @PutMapping("/{idAdoptante}/{idMascotaAdopcion}")
    public ResponseEntity<Adopcion> update(
            @PathVariable int idAdoptante,
            @PathVariable int idMascotaAdopcion,
            @RequestBody Adopcion adopcion) {

        adopcion.setIdAdoptante(idAdoptante);
        adopcion.setIdMascotaAdopcion(idMascotaAdopcion);

        int rows = adopcionRepository.update(adopcion);

        if (rows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(adopcion);
    }

    // DELETE -> 204 NO CONTENT
    @DeleteMapping("/{idAdoptante}/{idMascotaAdopcion}")
    public ResponseEntity<Void> delete(
            @PathVariable int idAdoptante,
            @PathVariable int idMascotaAdopcion) {

        int rows = adopcionRepository.delete(idAdoptante, idMascotaAdopcion);

        if (rows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}