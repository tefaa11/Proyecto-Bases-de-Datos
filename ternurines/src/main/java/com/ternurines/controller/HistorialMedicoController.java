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
    public List<HistorialMedico> findAll() {
        return historialMedicoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialMedico> findById(@PathVariable int id) {
        return historialMedicoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody HistorialMedico historialMedico) {
        int rows = historialMedicoRepository.save(historialMedico);
        return ResponseEntity.ok("Filas insertadas: " + rows);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody HistorialMedico historialMedico) {
        historialMedico.setIdHistorial(id);
        int rows = historialMedicoRepository.update(historialMedico);
        return ResponseEntity.ok("Filas actualizadas: " + rows);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        int rows = historialMedicoRepository.delete(id);
        return ResponseEntity.ok("Filas eliminadas: " + rows);
    }
}