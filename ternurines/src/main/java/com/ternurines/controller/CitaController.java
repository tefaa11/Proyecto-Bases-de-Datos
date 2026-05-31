package com.ternurines.controller;

import com.ternurines.model.Cita;
import com.ternurines.repository.CitaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citas")
public class CitaController {

    private final CitaRepository citaRepository;

    public CitaController(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Cita>> findAll() {
        return ResponseEntity.ok(citaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> findById(@PathVariable int id) {
        return citaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cita> save(@RequestBody Cita cita) {
        citaRepository.save(cita);
        return ResponseEntity.status(201).body(cita);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cita> update(
            @PathVariable int id,
            @RequestBody Cita cita) {

        if (citaRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        cita.setIdCita(id);
        citaRepository.update(cita);
        return ResponseEntity.ok(cita);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (citaRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        citaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
