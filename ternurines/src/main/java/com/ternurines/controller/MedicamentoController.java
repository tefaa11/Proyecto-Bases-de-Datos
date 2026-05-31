package com.ternurines.controller;

import com.ternurines.model.Medicamento;
import com.ternurines.repository.MedicamentoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoController(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Medicamento>> findAll() {
        return ResponseEntity.ok(medicamentoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicamento> findById(@PathVariable int id) {
        return medicamentoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Medicamento> save(@RequestBody Medicamento medicamento) {
        medicamentoRepository.save(medicamento);
        return ResponseEntity.status(201).body(medicamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicamento> update(
            @PathVariable int id,
            @RequestBody Medicamento medicamento) {

        if (medicamentoRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        medicamento.setIdMedicamento(id);
        medicamentoRepository.update(medicamento);
        return ResponseEntity.ok(medicamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (medicamentoRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        medicamentoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
