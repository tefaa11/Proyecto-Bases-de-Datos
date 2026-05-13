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

    @GetMapping
    public List<Adopcion> findAll() {
        return adopcionRepository.findAll();
    }

    @GetMapping("/{idAdoptante}/{idMascotaAdopcion}")
    public ResponseEntity<Adopcion> findById(@PathVariable int idAdoptante,
                                             @PathVariable int idMascotaAdopcion) {
        return adopcionRepository.findById(idAdoptante, idMascotaAdopcion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody Adopcion adopcion) {
        int rows = adopcionRepository.save(adopcion);
        return ResponseEntity.ok("Filas insertadas: " + rows);
    }

    @PutMapping("/{idAdoptante}/{idMascotaAdopcion}")
    public ResponseEntity<String> update(@PathVariable int idAdoptante,
                                         @PathVariable int idMascotaAdopcion,
                                         @RequestBody Adopcion adopcion) {
        adopcion.setIdAdoptante(idAdoptante);
        adopcion.setIdMascotaAdopcion(idMascotaAdopcion);
        int rows = adopcionRepository.update(adopcion);
        return ResponseEntity.ok("Filas actualizadas: " + rows);
    }

    @DeleteMapping("/{idAdoptante}/{idMascotaAdopcion}")
    public ResponseEntity<String> delete(@PathVariable int idAdoptante,
                                         @PathVariable int idMascotaAdopcion) {
        int rows = adopcionRepository.delete(idAdoptante, idMascotaAdopcion);
        return ResponseEntity.ok("Filas eliminadas: " + rows);
    }
}