package com.ternurines.controller;

import com.ternurines.model.HistorialMedico;
import com.ternurines.repository.HistorialMedicoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historiales-medicos")
public class HistorialMedicoController {

    private final HistorialMedicoRepository historialMedicoRepository;

    public HistorialMedicoController(HistorialMedicoRepository historialMedicoRepository) {
        this.historialMedicoRepository = historialMedicoRepository;
    }

    @GetMapping
    public ResponseEntity<List<HistorialMedico>> findAll() {
        return ResponseEntity.ok(historialMedicoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialMedico> findById(@PathVariable int id) {

        return historialMedicoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<HistorialMedico> save(
            @RequestBody HistorialMedico historialMedico) {

        historialMedicoRepository.saveAndReturn(historialMedico);

        return ResponseEntity.status(201).body(historialMedico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistorialMedico> update(
            @PathVariable int id,
            @RequestBody HistorialMedico historialMedico) {

        historialMedico.setIdHistorial(id);

        int rows = historialMedicoRepository.update(historialMedico);

        if (rows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(historialMedico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {

        int rows = historialMedicoRepository.delete(id);

        if (rows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
