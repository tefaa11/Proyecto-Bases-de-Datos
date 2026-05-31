package com.ternurines.controller;

import com.ternurines.model.Adoptante;
import com.ternurines.repository.AdoptanteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adoptantes")
public class AdoptanteController {

    private final AdoptanteRepository adoptanteRepository;

    public AdoptanteController(AdoptanteRepository adoptanteRepository) {
        this.adoptanteRepository = adoptanteRepository;
    }

    @GetMapping
    public ResponseEntity<List<Adoptante>> findAll() {
        return ResponseEntity.ok(adoptanteRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adoptante> findById(@PathVariable int id) {
        return adoptanteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Adoptante> save(@RequestBody Adoptante adoptante) {
        adoptanteRepository.save(adoptante);
        return ResponseEntity.status(201).body(adoptante);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adoptante> update(
            @PathVariable int id,
            @RequestBody Adoptante adoptante) {

        if (adoptanteRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        adoptante.setIdAdoptante(id);
        adoptanteRepository.update(adoptante);
        return ResponseEntity.ok(adoptante);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (adoptanteRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        adoptanteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
